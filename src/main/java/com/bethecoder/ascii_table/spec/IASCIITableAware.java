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
package com.bethecoder.ascii_table.spec;

import java.util.List;

import com.bethecoder.ascii_table.ASCIITableHeader;

/**
 * An ASCII table interface for extracting the header, data
 * and formatted cell data.
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public interface IASCIITableAware {

	/**
	 * Returns the list of headers to render.
	 */
	public List<ASCIITableHeader> getHeaders();
	
	/**
	 * Returns the data items to render. 
	 */
	public List<List<Object>> getData();
	
	/**
	 * Returns the formatted data item for a particular cell.
	 * Return <null> if you don't want to format the data.
	 * It uses String.valueOf(data) as cell value in that case.
	 * 
	 * @param header
	 * @param data
	 * @return
	 */
	public String formatData(ASCIITableHeader header, int row, int col, Object data);
	
}
