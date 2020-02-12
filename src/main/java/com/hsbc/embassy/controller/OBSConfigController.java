package com.hsbc.embassy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.hsbc.embassy.domain.Employee;

@RestController
@RequestMapping("/config")
public class OBSConfigController {

	@RequestMapping(value = "/tables/filters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> createYaml(@RequestParam String[] tables,@RequestParam String[] filters) {
		List<Map<String, Object>> rows = null;

		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}
	/*
	 * {
  "json": [
    "rigid",
    "better for data interchange"
  ],
  "yaml": [
    "slim and flexible",
    "better for configuration"
  ],
  "object": {
    "key": "value",
    "array": [
      {
        "null_value": null
      },
      {
        "boolean": true
      },
      {
        "integer": 1
      }
    ]
  },
  "paragraph": "Blank lines denote\nparagraph breaks\n",
  "content": "Or we\ncan auto\nconvert line breaks\nto save space"
}
	 * 
	 */
	@RequestMapping(value = "/yaml", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpStatus createYaml(@RequestBody String jsonString)
			throws JsonGenerationException, JsonMappingException, IOException {
		JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		om.writeValue(new File("C:\\Users\\chenwang2017\\git\\spmia-7\\authentication-service\\output\\person3.yaml"),
				jsonNodeTree);
		return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/{yamlPath}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getYaml(@RequestParam String yamlPath) throws JsonGenerationException, JsonMappingException, IOException {
		File yaml = new File(yamlPath);
	    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
	    Object obj = yamlReader.readValue(yaml, Object.class);
	    ObjectMapper jsonWriter = new ObjectMapper();
	    return jsonWriter.writeValueAsString(obj);
		
		
	}
	
	
}
