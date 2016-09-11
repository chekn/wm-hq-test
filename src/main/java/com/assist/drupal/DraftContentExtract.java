package com.assist.drupal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年5月16日-下午2:26:03
 */
public class DraftContentExtract {
	
	private static final String DRAFT_TAG="mediaDraft";
	private static final String TITLE_TAG="mediaTitle";
	private static final String BODY_TAG="mediaBody";
	
	private static final String META_TAG="mediaMeta";
	private static final String KEYWORDS_TAG="keyWords";
	private static final String KEYWORD_TAG="keyWord";
	private static final String STKCODES_TAG="stkCodes";
	private static final String STKCODE_TAG="stkCode";
	
	//提取文章
	public List<String> extractDraftStrs(String ftlParseContent){
		List<String> draftList=new ArrayList<String>();
		
		if(ftlParseContent.contains("<"+DRAFT_TAG+">"))
			draftList.addAll(extractParseStr(ftlParseContent, DRAFT_TAG,true));
		else
			draftList.add(ftlParseContent);
		
		return draftList;
	}
	
	public boolean isAccordFullStructure(String ftlParseContent){
		
		boolean isAccordStructure= ftlParseContent.matches(
				"<"+DRAFT_TAG+">"
						+ "(" + "<"+META_TAG+">.*?</"+META_TAG+">"+ "){0,1}"
						+ "<"+TITLE_TAG+">.*?</"+TITLE_TAG+">"
						+ "<"+BODY_TAG+">.*?</"+BODY_TAG+">"
				+"</"+DRAFT_TAG+">");
		
		return isAccordStructure;
	}
	
	/**
	 * 提取Meta
	 * @param draftStr
	 * @return
	 */
	private String extractMeta(String draftStr) {
		List<String> strs= extractParseStr(draftStr,META_TAG,false);
		return strs!=null ? strs.get(0):null;
	}
	
	
	/**
	 * 取KeyWORDS
	 * @param draftStr
	 * @return
	 */
	public List<String> extractKeyWords(String draftStr) {
		List<String> keyWords=null;
		String metaStr=this.extractMeta(draftStr);
		
		if(metaStr!=null) {
			List<String> strs= extractParseStr(metaStr,KEYWORDS_TAG,false);
			keyWords= strs!=null ? extractParseStr(strs.get(0), KEYWORD_TAG, false) : null;
		}
		
		return keyWords;
	}
	
	
	/**
	 * 取StkCodes
	 * @param draftStr
	 * @return
	 */
	public List<String> extractStkCodes(String draftStr) {
		List<String> stkCodes=null;
		String metaStr=this.extractMeta(draftStr);
		
		if(metaStr!=null) {
			List<String> strs= extractParseStr(metaStr,STKCODES_TAG,false);
			stkCodes= strs!=null ? extractParseStr(strs.get(0), STKCODE_TAG, false) :null;
		}
		
		return stkCodes;
	}
	
	public String extractTitleStr(String draftStr){
		List<String> strs= extractParseStr(draftStr,TITLE_TAG,false);
		return strs.get(0);
	}

	public String extractBodyStr(String draftStr){
		List<String> cnts= extractParseStr(draftStr,BODY_TAG,false);
		return cnts.get(0);
	}
	
	private static List<String> extractParseStr(String cnt, String tagName, boolean isContainTag){
		List<String> rev=new ArrayList<String>();
		
		Pattern pattern=Pattern.compile("<"+tagName+">(.*?)</"+tagName+">");
		Matcher matcher=pattern.matcher(cnt);
		while(matcher.find()){
			String inCnt=matcher.group(isContainTag?0:1);
			rev.add(inCnt);
		}
		
		return rev.size()!=0 ? rev:null;
	}
	
	public static void main(String[] args) throws IOException{
		DraftContentExtract dce=new DraftContentExtract();
		String strIn="<mediaDraft><mediaMeta><keyWords><keyWord>ccx</keyWord></keyWords><stkCodes><stkCode>ccccc</stkCode></stkCodes></mediaMeta><mediaTitle>33344</mediaTitle><mediaBody>33444</mediaBody></mediaDraft>";
		List<String> recc=dce.extractDraftStrs(strIn);
		for(String cnt:recc){
			System.out.println(cnt);
			System.out.println(dce.extractTitleStr(cnt));
			System.out.println(dce.extractBodyStr(cnt));
			System.out.println(dce.isAccordFullStructure(cnt));
			System.out.println(dce.extractKeyWords(cnt));
			System.out.println(dce.extractStkCodes(cnt));
		}
	}
	
}
