package com.assist.drupal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年5月16日-下午2:26:03
 */
public class DraftSlit {
	
	private static final String DRAFT_TAG="mediaDraft";
	private static final String TITLE_TAG="mediaTitle";
	private static final String BODY_TAG="mediaBody";
	
	public static List<String> slit(String ftlParseContent){
		List<String> draftList=new ArrayList<String>();
		
		Document doc=Jsoup.parse(ftlParseContent);
		System.out.println(">>>"+doc.html());
		Elements elements=doc.select(DRAFT_TAG);
		for(Element element:elements){
			String cnt=element.html();
			System.out.println(">>>>>>"+cnt);
			draftList.add(cnt);
		}
		
		return draftList;
	}
	
	private static List<String> parseParseStr(String cnt, String tagName){
		List<String> rev=new ArrayList<String>();
		
		Pattern pattern=Pattern.compile("<"+tagName+">(.*?)</"+tagName+">");
		Matcher matcher=pattern.matcher(cnt);
		while(matcher.find()){
			String inCnt=matcher.group(1);
			rev.add(inCnt);
		}
		
		return rev;
	}
	
	public static void main(String[] args) throws IOException{
		String strIn="<mediaDraft>sfsfs</mediaDraft><mediaDraft>sfsfs</mediaDraft>";
		File file=new File("C:\\Users\\Pactera-NEN\\Desktop\\ccs.txt");
		strIn=FileUtils.readFileToString(file);
		slit(strIn);
	}
	
}
