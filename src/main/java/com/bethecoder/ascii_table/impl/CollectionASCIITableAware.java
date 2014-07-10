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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bethecoder.ascii_table.ASCIITableHeader;
import com.bethecoder.ascii_table.spec.IASCIITableAware;

/**
 * This class is useful to extract the header and row data from
 * a list of java beans.
 *  
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class CollectionASCIITableAware<T> implements IASCIITableAware {

	private List<ASCIITableHeader> headers = null;
	private List<List<Object>> data = null;
	
	public CollectionASCIITableAware(List<T> objList, String ... properties) {
		this(objList, Arrays.asList(properties), Arrays.asList(properties));
	}
	
	public CollectionASCIITableAware(List<T> objList, List<String> properties, List<String> title) {
		
		if (objList != null && !objList.isEmpty() && properties != null && !properties.isEmpty()) {
			//Populate header
			String header = null;
			headers = new ArrayList<ASCIITableHeader>(properties.size());
			for (int i = 0 ; i < properties.size() ; i ++) {
				header = (i < title.size()) ? title.get(i) : properties.get(i);
				headers.add(new ASCIITableHeader(String.valueOf(header).toUpperCase()));
			}
			
			//Populate data
			data = new ArrayList<List<Object>>();
			List<Object> rowData = null;
			Class<?> dataClazz = objList.get(0).getClass();
			Map<String, Method> propertyMethodMap = new HashMap<String, Method>();
			
			for (int i = 0 ; i < objList.size() ; i ++) {
				rowData = new ArrayList<Object>();
				
				for (int j = 0 ; j < properties.size() ; j ++) {
					rowData.add(getProperty(propertyMethodMap, 
							dataClazz, objList.get(i), properties.get(j)));
				}
				
				data.add(rowData);
			}//iterate rows
			
		}
	}

	private Object getProperty(Map<String, Method> propertyMethodMap, Class<?> dataClazz, T obj, String property) {
		Object cellValue = null;
		
		try {
			Method method = null;
			
			if (propertyMethodMap.containsKey(property)) {
				method = propertyMethodMap.get(property);
			} else {
				String methodName = "get" + capitalize(property);
				method = getMethod(dataClazz, methodName);
				
				if (method == null) {
					methodName = "is" + capitalize(property);
					method = getMethod(dataClazz, methodName);
				}
				
				if (method != null) {
					propertyMethodMap.put(property, method);
				}
			}
			cellValue = method.invoke(obj, new Object [] {});
			
		} catch (Exception e) {
			//System.out.println("Unable to get cell content : " + e);
		}
		return cellValue;
	}
	
	private Method getMethod(Class<?> dataClazz, String methodName) {
		Method method = null;
		try {
			method = dataClazz.getMethod(methodName, new Class<?>[] {});
		} catch (Exception e) {
		}
		return method;
	}
	
	private String capitalize(String property) {
		return property.length() == 0 ? property : 
			property.substring(0, 1).toUpperCase() + property.substring(1).toLowerCase();
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
