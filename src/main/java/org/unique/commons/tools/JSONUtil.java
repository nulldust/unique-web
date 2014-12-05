package org.unique.commons.tools;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unique.commons.json.JSON;
import org.unique.commons.json.Tokens;

/**
 * 简单JSON处理工具类
 * 
 * @author biezhi
 * @since 1.0
 */
public class JSONUtil {

	/**
	 * 对象zhuan'huan
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String toJSON(T obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(toJSON(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(toJSON((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(toJSON((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(toJSON((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(toJSON((Set<?>) obj));
		} else {
			json.append(toJSON(obj));
		}
		return json.toString();
	}
	
	/**
	 * @param bean
	 *            bean瀵硅薄
	 * @return String
	 */
	public static String beanToJSON(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append(Tokens.CURLY_OPEN);
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = toJSON(props[i].getName());
					String value = toJSON(props[i].getReadMethod().invoke(bean));
					json.append(name);
					json.append(Tokens.COLON);
					json.append(value);
					json.append(Tokens.COMMA);
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, Tokens.CURLY_CLOSE);
		} else {
			json.append(Tokens.CURLY_CLOSE);
		}
		return json.toString();
	}
	
	/**
	 * list转json
	 * @param list 集合对象
	 * @return json
	 */
	public static String toJSON(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append(Tokens.BRACKET_OPEN);
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(toJSON(obj));
				json.append(Tokens.COMMA);
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append(Tokens.BRACKET_CLOSE);
		}
		return json.toString();
	}
	
	/**
	 * 数组转json
	 * @param array 数组对象
	 * @return json字符串
	 */
	public static String toJSON(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append(Tokens.BRACKET_OPEN);
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(toJSON(obj));
				json.append(Tokens.COMMA);
			}
			json.setCharAt(json.length() - 1, Tokens.BRACKET_CLOSE);
		} else {
			json.append(Tokens.BRACKET_CLOSE);
		}
		return json.toString();
	}

	/**
	 * map转json
	 * @param map map对象
	 * @return
	 */
	public static String toJSON(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append(Tokens.CURLY_OPEN);
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(toJSON(key));
				json.append(Tokens.COLON);
				json.append(toJSON(map.get(key)));
				json.append(Tokens.COMMA);
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append(Tokens.CURLY_CLOSE);
		}
		return json.toString();
	}

	/**
	 * set转JSON
	 * @param set
	 * @return
	 */
	public static String toJSON(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append(Tokens.BRACKET_OPEN);
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(toJSON(obj));
				json.append(Tokens.COMMA);
			}
			json.setCharAt(json.length() - 1, Tokens.BRACKET_CLOSE);
		} else {
			json.append(Tokens.BRACKET_CLOSE);
		}
		return json.toString();
	}

	/**
	 * 格式化json
	 * @param s 要格式化的字符串
	 * @return 格式化后的json
	 */
	public static String toJSON(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	public static Map<String, Object> json2Map(final String json) {
		Map<String, Object> map = new JSON().decode(json);
		return map;
	}
	
}