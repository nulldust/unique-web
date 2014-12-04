package org.unique.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * String Util
 * @author biezhi
 * @since 1.0
 */
public class StringUtils {

    private static final String NUM_S = "0123456789";

    private static final String STR_S = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static boolean isEmpty(final String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(final String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) return true;
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }

    public static String trimToNull(final String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(final String str) {
        return str == null ? "" : str.trim();
    }

    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 计算文字长度 无中文问题
     */
    public static int getLength(final String string) {
        if (isBlank(string)) {
            return 0;
        } else {
            char[] strChars = string.toCharArray();
            return strChars.length;
        }
    }

    /**
     * 将字符串中特定模式的字符转换成map中对应的值,
     */
    public static String replace(final String s, Map<String, String> map) {
        StringBuilder sb = new StringBuilder((int) (s.length() * 1.5));
        int cursor = 0;
        for (int start, end; (start = s.indexOf("${", cursor)) != -1 && (end = s.indexOf("}", start)) != -1;) {
            sb.append(s.substring(cursor, start));
            String key = s.substring(start + 2, end);
            sb.append(map.get(trim(key)));
            cursor = end + 1;
        }
        sb.append(s.substring(cursor, s.length()));
        return sb.toString();
    }

    /**
     * 获取ip
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Requested-For");
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * unicode编码转汉字
     */
    public static String unicodeToString(String str) {
        
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");    
        }
        return str;
    }

    /**
     * 获取unicode编码
     */
    public static String getUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toLowerCase(firstChar) + tail;
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toUpperCase(firstChar) + tail;
        return str;
    }

    /**
     * 首字母大写其余小写
     */
    public static String upperFirstLowerOther(final String str) {
        if (isEmpty(str)) return str;
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        sb.append(Character.toUpperCase(c));
        String other = str.substring(1);
        sb.append(other.toLowerCase());
        return sb.toString();
    }

    /**
     * 字符串不为 null 而且不为 "" 时返回 true
     */
    public static boolean notBlank(final String str) {
        return str == null || "".equals(str.trim()) ? false : true;
    }

    public static boolean notBlank(final String... strings) {
        if (strings == null) return false;
        for (String str : strings)
            if (str == null || "".equals(str.trim())) return false;
        return true;
    }

    public static boolean notNull(Object... paras) {
        if (paras == null) return false;
        for (Object obj : paras)
            if (obj == null) return false;
        return true;
    }

    /**
     * 截取文字safe 中文
     */
    public static String subCn(final String string, final int length, final String more) {
        if (StringUtils.isNotEmpty(string)) {
            char[] chars = string.toCharArray();
            if (chars.length > length) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    sb.append(chars[i]);
                }
                sb.append(more);
                return sb.toString();
            }
        }
        return string;
    }

    public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^-?[0-9]*$");
		return pattern.matcher(str).matches();
	}
    
    public static String join(final String[] array, final String separator) {
        if (array == null) {
            return null;
        }
        final int noOfItems = array.length;
        if (noOfItems <= 0) {
            return null;
        }
        if (noOfItems == 1) {
            return array[0].toString();
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = 0; i < noOfItems; i++) {
            buf.append(separator);
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(final Object[] array, final String separator) {
        if (array == null) {
            return null;
        }
        final int noOfItems = array.length;
        if (noOfItems <= 0) {
            return null;
        }
        if (noOfItems == 1) {
            return array[0].toString();
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = 0; i < noOfItems; i++) {
            buf.append(separator);
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static <T> String join(final List<T> array, final String separator) {
        if (array == null) {
            return null;
        }
        final int noOfItems = array.size();
        if (noOfItems <= 0) {
            return null;
        }
        if (noOfItems == 1) {
            return array.get(0).toString();
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = 0; i < noOfItems; i++) {
            buf.append(separator);
            if (array.get(i) != null) {
                buf.append(array.get(i));
            }
        }
        return buf.toString();
    }

    /**
     * 生成一个10位的tonken用于http cache
     */
    public static String getTonken() {
        return RandomStringUtils.random(10, NUM_S);
    }

    /**
     * 生成随机字符串
     */
    public static String randomStr(final int count) {
        return RandomStringUtils.random(count, STR_S);
    }

    /**
     * 生成随机数
     */
    public static String randomNum(final int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    /**
     * 是否为正确的用户名
     */
    public static boolean isAlphaUnderline(final String msg) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(msg);
        return matcher.matches();
    }
    
    /**
     * string转int
     */
    public static int toInt(final String str){
    	return Integer.valueOf(str.trim());
    }
    
    /**
     * string转long
     */
    public static long toLong(final String str){
    	return Long.valueOf(str.trim());
    }
    
    /**
     * string转boolean
     */
    public static boolean toBoolean(final String str){
    	return Boolean.valueOf(str.trim());
    }
    
    /**
     * string转double
     */
    public static double toDouble(final String str){
    	return Double.valueOf(str.trim());
    }
    
    /**
     * string转float
     */
    public static float toFloat(final String str){
    	return Float.valueOf(str.trim());
    }
    
    /**
     * 计算某个字符串出现次数
     * @param str 要统计的字符串
     * @param target 字符串
     * @return
     */
    public static int getCharCount(final String str, final String target){
    	int counts = 0;
        String temp = str;
        int needle = -1;
        while (true) {
            needle = temp.indexOf(target, needle + 1);
            if (needle >= 0)
                counts++;
            else {
                break;
            }
        }
    	return counts;
    }
    
}
