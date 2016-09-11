package com.assist.drupal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.yicai.medialab.writingmaster.drupalhs.constant.TextFormat;
import com.yicai.medialab.writingmaster.drupalhs.model.NewsDraft;
import com.yicai.medialab.writingmaster.drupalhs.model.NewsSourceItem;
import com.yicai.medialab.writingmaster.drupalhs.util.FreeMarkerUtil;
import com.yicai.medialab.writingmaster.extraction.core.api.InfoExtraction;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年3月10日-下午7:34:52
 */
public class SimpleDrupalTool {
	
	private String corpus="402";
	private String drupalAccount="nicsecret";
	private String drupalPassword="secret";

	private String targetPath=this.getClass().getClassLoader().getResource("").getPath().toString().substring(1);
	private String ftlDir=targetPath+"../../src/HQ_ParseFtl";
	private String ftlGernDir=targetPath+"../../src/HQ_ParseFtl/Gern";
	
	public void pushDataSet(String basePackagePath,String uniqueName, Map<String,Object> rawData) throws IOException{
			
		String news_template = (String) rawData.get(InfoExtraction.HQ_RETTMPLATENAME_KEY);
		String news_template_url = (String) rawData.get(InfoExtraction.HQ_RETTMPLATEURL_KEY);
		Date pubDate=new Date();
		
		NewsSourceItem source = new NewsSourceItem();
		source.setMongo_id(UUID.randomUUID().toString());
		source.setOrigin_url((String) rawData.get(InfoExtraction.HQ_RETDSORIURL_KEY));
		source.setPub_date(pubDate);
		source.setData_type((String)rawData.get(InfoExtraction.HQ_RETDSDATTYP_KEY));
		source.setCollection("hs");
		source.setFormat(TextFormat.FULL_HTML);
		
	    List<NewsDraft> draftList=new ArrayList<NewsDraft>();
	    Configuration cfg= FreeMarkerUtil.createConfig(basePackagePath);
	    for(Terminal t:Terminal.values()){
	    	String strIn;
	    	Template template=FreeMarkerUtil.getTemplate(cfg,"color_"+uniqueName+t.getDefFileNameSuf());
	    	
	    	strIn=FreeMarkerUtil.generateByTemplate(template, rawData);//根据模板生成文章
	    	DraftContentExtract dce=new DraftContentExtract();
	    	
	    	for(String draftContent: dce.extractDraftStrs(strIn)){		    	
    			NewsDraft draft = new NewsDraft();
		    	boolean isFullStructure=dce.isAccordFullStructure(draftContent);
		    	
		    	if(StringUtils.isEmpty(draftContent)) continue;
		    	System.out.println(t.getTerminalName()+":"+draftContent);
		    	List<String> stkCodes=isFullStructure ? dce.extractStkCodes(draftContent):null;
		    	List<String> keyWords=isFullStructure ? dce.extractKeyWords(draftContent):null;
		    	draft.setContent(isFullStructure ? dce.extractBodyStr(draftContent):draftContent);
		    	
		    	draft.setFormat(TextFormat.FULL_HTML);
		    	draft.setNews_template(news_template);
		    	draft.setNews_template_url(news_template_url);
		    	draft.setCorpus_tid(corpus);
		    	draft.setNews_terminal(t.getTerminalName());
		    	draft.setPub_date(pubDate);
		    	draft.setTitle(isFullStructure ? dce.extractTitleStr(draftContent):(String)rawData.get(InfoExtraction.HQ_RETDFTTIL_KEY));
		    	draftList.add(draft);
    		}
	    	
	    }
	    
		//WMResultImporter.importSourcesAndDrafts(drupalAccount,drupalPassword, Arrays.asList(source), draftList);
		
	}
	
	
	public static void main(String[] args){
		File file=new File("D:\\HQ_ParseFtl");
		if(!file.exists())
			file.mkdir();
		
		File srcFtl=new File("C:\\Users\\Pactera-NEN\\git\\wm-ex-social");
		SimpleDrupalTool sdt=new SimpleDrupalTool();
		sdt.fileScan(srcFtl);
		
	}
	
	public void fileScan(File dir){
		for(File ufile:dir.listFiles()){
			if(ufile.isDirectory()){
				fileScan(ufile);
			}else{
				if(ufile.getName().endsWith(".ftl")){
					System.out.println(ufile.getName()+"_"+ufile.getPath());
					try {
						FileUtils.copyFile(new File(ufile.getPath()), new File("D:\\HQ_ParseFtl\\"+ufile.getName()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
