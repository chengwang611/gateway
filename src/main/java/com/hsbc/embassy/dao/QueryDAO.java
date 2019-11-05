package com.hsbc.embassy.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hsbc.embassy.domain.Response;


@Component
public class QueryDAO {

	String[] DataBases = { "USA", "Global" };
	Map<String, String[]> Tables = new HashMap<>();
	String[] Tables1 = { "USA_Account", "USA_Transaction" };
	String[] Tables2 = { "Global_Account", "Global_Transaction" };
	String[] columns1 = { "USA_Account_column1", "USA_Account_column2", "USA_Account_column3", "USA_Account_column4" };
	String[] columns2 = { "USA_Transaction_column1", "USA_Transaction_column2", "USA_Transaction_column3",
			"USA_Transaction_column4" };
	String[] columns3 = { "Global_Account_column1", "Global_Account_column2", "Global_Account_column3",
			"Global_Account_column4" };
	String[] columns4 = { "Global_Transaction_column1", "Global_Transaction_column2", "Global_Transaction_column3",
			"Global_Transaction_column4" };
	Map<String, String[]> columns = new HashMap<>();

	@Autowired
	public QueryDAO() {
		Tables.put("USA", Tables1);
		Tables.put("Global", Tables2);
		columns.put("USA_Account", columns1);
		columns.put("USA_Transaction", columns2);
		columns.put("Global_Account", columns3);
		columns.put("Global_Transaction", columns4);
	}

	public Response getDataBases() {

		return new Response("databases", DataBases);

	}

	public Response getTables(String DataBases) {

		return new Response("tables", Tables.get(DataBases));

	}

	public Response getTableColumns(String DataBases, String TableName) {

		return new Response("columns", columns.get(DataBases + "_" + TableName));

	}

}
