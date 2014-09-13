package org.prabu.spring;

import java.io.IOException;
import java.sql.Types;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class SqlRowSetJsonSerializer extends JsonSerializer<ResultSetWrappingSqlRowSet> {

	private int noOfColumns = 0;
	
	private boolean isArray;
	
	@Override
	public void serialize(ResultSetWrappingSqlRowSet rowSet, JsonGenerator jsonGen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		SqlRowSetMetaData metaData = rowSet.getMetaData();  
		
		noOfColumns = metaData.getColumnCount();
		
		init(rowSet);
		if(isArray){
			jsonGen.writeStartArray();
		}
		
		String columnName;
		rowSet.beforeFirst();
		
		while (rowSet.next()) {
			
			jsonGen.writeStartObject();
			
			for (int i=1;i<=noOfColumns;i++){
					
				columnName = metaData.getColumnLabel(i);
				
				if (rowSet.getString(i)== null ) {
					jsonGen.writeNullField(columnName);
				}
				else{
				
					switch (metaData.getColumnType(i)) {
					
					case Types.NULL : 
						jsonGen.writeNullField(columnName);
						break;
					
					case Types.BIT :
					case Types.BINARY :
					case Types.VARBINARY :
					case Types.LONGVARBINARY :
					case Types.BLOB :
						jsonGen.writeNumberField(columnName, rowSet.getByte(i));
						break;
						
					case Types.BOOLEAN : 
						jsonGen.writeBooleanField(columnName, rowSet.getBoolean(i));
						break;
					
					case Types.TINYINT:
						jsonGen.writeBooleanField(columnName, BooleanUtils.toBoolean(rowSet.getInt(i)) );
						break;
					
					
					case Types.BIGINT :
					case Types.INTEGER : 
					case Types.SMALLINT : 
						jsonGen.writeNumberField(columnName, rowSet.getLong(i));
						break;
						
					case Types.FLOAT :
					case Types.REAL : 
					case Types.DECIMAL : 
					case Types.NUMERIC:
					case Types.DOUBLE:	
						jsonGen.writeNumberField(columnName, rowSet.getDouble(i));
						break;
						
					case Types.CHAR :
					case Types.VARCHAR : 
					case Types.LONGNVARCHAR : 
					case Types.CLOB:
						jsonGen.writeStringField(columnName, rowSet.getString(i));
						break;
						
					case Types.DATE :
					case Types.TIME : 
					case Types.TIMESTAMP : 
						Date date  = rowSet.getDate(i);
						if(date!=null){				
						jsonGen.writeNumberField(columnName, date.getTime());
						}
						else {
							jsonGen.writeNumberField(columnName, null);
						}
						break;
	
					default:
						jsonGen.writeStringField(columnName, rowSet.getString(i));
						break;
					}
				}
			}
			jsonGen.writeEndObject();
		}
		if(isArray){
			jsonGen.writeEndArray();
		}
	}
	
	private void init(SqlRowSet rowSet){
		Long noOfRows=0l;
		while(rowSet.next()){
			noOfRows++;
			if(noOfRows>1){
				isArray = true;
				break;
			}
		}
	}

}
