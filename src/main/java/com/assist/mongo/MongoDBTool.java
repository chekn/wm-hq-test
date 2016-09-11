package com.assist.mongo;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年3月21日-下午6:42:41
 */
public class MongoDBTool {
	
	private static MongoClient mongoClient;
	
	public synchronized static MongoClient getMongoClient(){
		if(mongoClient==null){
			ServerAddress addr=new ServerAddress("121.40.81.73",27017);
			MongoCredential credential=MongoCredential.createCredential("dev", "admin", "secret".toCharArray());
			List<MongoCredential> listCredential=new ArrayList<MongoCredential>();
			listCredential.add(credential);
			mongoClient=new MongoClient(addr,listCredential);
		}
		return mongoClient;
		
	}
	
	public static void closeMongoClient(MongoClient mongoClient){
		if(mongoClient!=null)
			mongoClient.close();
	}
	
}
