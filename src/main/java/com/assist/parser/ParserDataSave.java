package com.assist.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.yicai.medialab.writingmaster.extraction.core.api.InfoExtraction;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年4月14日-上午10:17:35
 */
public class ParserDataSave {
	
	public static void saveParsedData(MongoClient mongoClient, String dbName, Map<String, Object> parsedData) {
		Map<String, Object> saveData = new HashMap<String, Object>();
		saveData.put("PARSE_TIME".toLowerCase(), new Date());
		saveData.put("ORI_TITLE".toLowerCase(), parsedData.get(InfoExtraction.HQ_RETDFTTIL_KEY));
		saveData.put("ORI_URL".toLowerCase(), parsedData.get(InfoExtraction.HQ_RETDSORIURL_KEY));
		saveData.put("ORI_COLLECTION".toLowerCase(), "...");
		saveData.put("PARSER_ID".toLowerCase(), "...");
		saveData.put("NEWS_TYPE_ID".toLowerCase(), "...");
		saveData.put("PARENT_NEWS_TYPE_ID".toLowerCase(), "...");
		saveData.put("PARSED_DATA".toLowerCase(), parsedData);

		mongoClient.getDatabase(dbName).getCollection("parse_jar").insertOne(new Document(saveData));
	}
}
