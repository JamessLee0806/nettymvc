package com.baocloud.restful;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@RestController
public class Example {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping("/boot")
	public String boot() throws Exception{
		return "Hello Spring Boot !";
	}
	
	@RequestMapping("/groovy")
	public String groovy(){
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("data1", "data1");
		data.put("dat2", "groovy");
		return JSON.toJSONString(data);
	}
	
	@RequestMapping("addMongo")
	public String addMongo(){
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("data1", "data1");
		data.put("dat2", "groovy");
		mongoTemplate.save(data, "mongo");
		return "Success";
	}
	
	@RequestMapping("getMongo")
	public String getMongo(){
		DBCollection collection=mongoTemplate.getCollection("mongo");
		if(collection!=null && collection.count()>0){
			DBCursor cursor= collection.find();
			 List<Object> list=new ArrayList<>();
			 while (cursor.hasNext()) {
				list.add(cursor.next());
			}
			 cursor.close();
			 return JSON.toJSONString(list);
		}
		return "NONE";
	}
	
	@RequestMapping("dbName")
	public String dbName(){
		return mongoTemplate.getDb().getName();
	}

}
