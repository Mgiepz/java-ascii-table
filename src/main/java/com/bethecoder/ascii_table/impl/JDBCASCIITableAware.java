/**
 * Copyright (C) 2011 K Venkata Sudhakar <kvenkatasudhakar@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bethecoder.ascii_table.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This class is useful to extract the header and row data from
 * JDBC ResultSet and SQLs.
 *  
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class JDBCASCIITableAware implements IASCIITableAware {

	private List<ASCIITableHeader> headers = null;
	private List<List<Object>> data = null;
	
	public JDBCASCIITableAware(Connection connection, String sql) {
		try {
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			init(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException("Unable to get table data : " + e);
		}
	}
	
	public JDBCASCIITableAware(ResultSet resultSet) {
		try {
			init(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException("Unable to get table data : " + e);
		}
	}

	private void init(ResultSet resultSet) throws SQLException {
		
		//Populate header
		int colCount = resultSet.getMetaData().getColumnCount();
		headers = new ArrayList<ASCIITableHeader>(colCount);
		for (int i = 0 ; i < colCount ; i ++) {
			headers.add(new ASCIITableHeader(
					resultSet.getMetaData().getColumnLabel(i + 1).toUpperCase()));
		}
		
		//Populate data
		data = new ArrayList<List<Object>>();
		List<Object> rowData = null;
		
		while (resultSet.next()) {
			
			rowData = new ArrayList<Object>();
			for (int i = 0 ; i < colCount ; i ++) {
				rowData.add(resultSet.getObject(i + 1));
			}
			data.add(rowData);
		}//iterate rows
		
	}
	
	@Override
	public List<List<Object>> getData() {
		return data;
	}

	@Override
	public List<ASCIITableHeader> getHeaders() {
		return headers;
	}

	@Override
	public String formatData(ASCIITableHeader header, int row, int col, Object data) {
		//Format only numbers
		try {
			BigDecimal bd = new BigDecimal(data.toString());
			return DecimalFormat.getInstance().format(bd);
		} catch (Exception e) {
		}

		//For non-numbers return null 
		return null;
	}
}
