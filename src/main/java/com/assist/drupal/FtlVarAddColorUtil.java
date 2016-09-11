package com.assist.drupal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年2月26日-下午2:12:40
 */
public class FtlVarAddColorUtil {
	private static String ftlVarRegex="(\\$\\{\\b*[^\\}]*?\\b*?\\})";
	//private static Pattern var = Pattern.compile("\\$\\{\\b*[^\\}]*?\\b*?\\}");
	private static String str1 = "<font color=\"blue\">";
	private static String str2 = "</font>";


    public static void addFontColorTag(String fDir) throws IOException{
		List<String> fileNameList = new ArrayList<String>();
		getFiles(fDir, fileNameList); //返回fDir路径下的所有文件名放到fileNameList
		//		Pattern ftlPat1 = Pattern.compile("^((?!color_).)*$");
		for(int i = 0; i < fileNameList.size(); i++){
			String fileName = fileNameList.get(i);
			Pattern p = Pattern.compile("color_");
			Matcher matP = p.matcher(fileName);
			if(!matP.find()){
				String origFile = fDir + "/" + fileName;
				String outFile = fDir + "/color_" + fileName;
				createFileWithColorTag(origFile,outFile);
			}
		}
	}
	
	public static void createFileWithColorTag(String origFtl, String colorFtl) throws IOException{
		String oriMediaDraft=FileUtils.readFileToString(new File(origFtl),"UTF-8");
		
		//预处理, 去除注释  并行
		StringBuilder stp1MediaDraftStrBu=new StringBuilder();
		String[] oriMediaDraftLines=oriMediaDraft.replaceAll("<!--.*?-->", "").split("\\r\\n");
		for(String line:oriMediaDraftLines) {
			String remPrefixTabAndSuffixTab=line.replaceAll("^\t*", "").replaceAll("\t*$", "");
			stp1MediaDraftStrBu.append(remPrefixTabAndSuffixTab);
		}
		
		//添加 font 标签包围
		String stp2MediaDraft=stp1MediaDraftStrBu.toString();
		boolean isContainMediaBody=stp2MediaDraft.contains("mediaBody");
		String stp2OriMediaBody=isContainMediaBody?extractParseStr(stp2MediaDraft, "mediaBody", true).get(0) : stp2MediaDraft;
		String stp2ModMediaBody=stp2OriMediaBody;
		//Matcher m = var.matcher(stp2ModMediaBody);
		//while (m.find()){
			//String str = m.group();
			stp2ModMediaBody=stp2ModMediaBody.replaceAll(ftlVarRegex, str1+"$1"+str2);
		//}
		
		
		String modMediaDraft = isContainMediaBody ? stp2MediaDraft.replace(stp2OriMediaBody, stp2ModMediaBody) : stp2ModMediaBody;
		
		FileUtils.writeStringToFile(new File(colorFtl), modMediaDraft, "UTF-8");
	}
	
	
	private static List<String> extractParseStr(String cnt, String tagName, boolean isContainTag){
		List<String> rev=new ArrayList<String>();
		
		Pattern pattern=Pattern.compile("<"+tagName+">([\\s\\S]*?)</"+tagName+">");
		Matcher matcher=pattern.matcher(cnt);
		while(matcher.find()){
			String inCnt=matcher.group(isContainTag?0:1);
			rev.add(inCnt);
		}
		
		return rev;
	}
	
	// 获取路径path下的所有文件名，存放在List<String>
	public static List<String> getFiles(String path, List<String> data) {
		File f = new File(path);
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				data = getFiles(fs[i].getPath(), data);
			}
		} else if (f.getName().endsWith(".ftl")) {
			data.add(f.getName());
		}
		return data;
	}
	
	
}
