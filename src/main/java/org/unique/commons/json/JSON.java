/**
 * Copyright (c) 2014-2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unique.commons.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSON {

	private String json = "";
	private int index = 0;

	/**
	 * Takes in a JSON string and returns a Map of strings and values
	 * 
	 * @param json
	 * @return Parsed map
	 */
	public Map<String, Object> decode(String json) {
		this.json = json;
		index = 0;
		return parse();
	}

	private Map<String, Object> parse() {
		Map<String, Object> map = new HashMap<String, Object>();
		char token = ' ';
		// {
		nextToken();

		boolean done = false;

		while (!done) {
			token = lookAhead();
			if (!Tokens.isToken(token)) {
				return null;
			} else if (token == Tokens.COMMA) {
				nextToken();
			} else if (token == Tokens.CURLY_CLOSE) {
				return map;
			} else {
				// name
				String name = parseString();
				if (name == null || name == "") {
					return null;
				}
				// :
				token = json.charAt(index);
				if (token != Tokens.COLON) {
					return null;
				}

				// value
				Object value = parseValue();

				map.put(name, value);
			}
		}

		return null;
	}

	private char nextToken() {
		index++;
		while (!Tokens.isToken(json.charAt(index)) && index < json.length()) {
			index++;
		}
		return json.charAt(index);
	}

	private char lookAhead() {
		int i = index;
		while (!Tokens.isToken(json.charAt(i)) && i < json.length()) {
			i++;
		}
		return json.charAt(i);
	}

	private String parseString() {
		// pop off "
		index++;
		StringBuffer sb = new StringBuffer();
		while (json.charAt(index) != '\"') {
			sb.append(json.charAt(index));
			index++;
		}
		// pop off tailing "
		nextToken();
		return sb.toString();
	}

	private Object parseValue() {
		char token = nextToken();
		if (token == Tokens.QUOTATION_MARK) {
			return parseString();
		} else if (token == Tokens.CURLY_OPEN) {
			return parse();
		} else if (token == Tokens.T || token == Tokens.F) {
			return parseBoolean();
		} else if (token == Tokens.N) {
			return parseNull();
		} else if (token == Tokens.DIGIT_0 || token == Tokens.DIGIT_1
				|| token == Tokens.DIGIT_2 || token == Tokens.DIGIT_3
				|| token == Tokens.DIGIT_4 || token == Tokens.DIGIT_5
				|| token == Tokens.DIGIT_6 || token == Tokens.DIGIT_7
				|| token == Tokens.DIGIT_8 || token == Tokens.DIGIT_9
				|| token == Tokens.NEGATIVE_SIGN) {
			return parseNumber();
		} else if (token == Tokens.BRACKET_OPEN) {
			return parseArray();
		}
		return null;
	}

	private boolean parseBoolean() {

		return Boolean.parseBoolean(valueToString());
	}

	private Object parseNull() {
		if ("null".equals(valueToString())) {
			return null;
		}
		// TODO: add error handling
		return null;
	}

	private Double parseNumber() {
		return Double.parseDouble(valueToString());
	}

	private String valueToString() {
		StringBuffer sb = new StringBuffer();
		while (json.charAt(index) != Tokens.CURLY_CLOSE
				&& json.charAt(index) != Tokens.COMMA
				&& json.charAt(index) != Tokens.BRACKET_CLOSE) {
			sb.append(json.charAt(index));
			index++;
		}
		return sb.toString();
	}

	private Object[] parseArray() {
		List<Object> list = new ArrayList<Object>();
		while (json.charAt(index) != Tokens.BRACKET_CLOSE) {
			list.add(parseValue());
		}
		// ]
		nextToken();
		return list.toArray();
	}
}