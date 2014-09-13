package org.prabu.spring;



import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SqlRowSetObjectMapper extends ObjectMapper {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlRowSetObjectMapper() {
		// TODO Auto-generated constructor stub
		
		SimpleModule module = new SimpleModule("RS");
		module.addSerializer(ResultSetWrappingSqlRowSet.class,new SqlRowSetJsonSerializer());
		registerModule(module);
	}

}
