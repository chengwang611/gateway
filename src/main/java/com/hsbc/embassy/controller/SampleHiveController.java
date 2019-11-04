package com.hsbc.embassy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.embassy.domain.Response;

@RestController
@RequestMapping("/hive")
public class SampleHiveController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/{schemaName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> showTables(@PathVariable String schemaName) {
		List<Map<String, Object>> rows = null;
		jdbcTemplate.execute("use " + schemaName);
		rows = jdbcTemplate.queryForList("show tables");

		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/2/{schemaName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> showTables_2(@PathVariable String schemaName) {
		
		jdbcTemplate.execute("use " + schemaName);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("show tables");
        List<Object> fields=new ArrayList<>();
        for(Map<String, Object> mp:rows) {
        	fields.add(mp.get("tab_name"));
        }
        
        
        Response response = new Response("tables", fields.toArray());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{schemaName}/{tableName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> showTableColumns(@PathVariable String schemaName,@PathVariable String tableName) {
		List<Map<String, Object>> rows = null;
		jdbcTemplate.execute("use " + schemaName);
		rows = jdbcTemplate.queryForList("describe "+schemaName+"."+tableName);

		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/list/{schemaName}/{tableName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> ListTable(@PathVariable String schemaName,@PathVariable String tableName) {
		List<Map<String, Object>> rows = null;
		jdbcTemplate.execute("use " + schemaName);
		rows = jdbcTemplate.queryForList("select * from "+schemaName+"."+tableName);

		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}

	@RequestMapping(value = "/databases", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> showDatabaeses() {
		List<Map<String, Object>> rows = null;
		rows = jdbcTemplate.queryForList("show databases");
		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}
}
