package org.unique.commons.json;

public class Tokens {
    public static final char CURLY_OPEN = '{';
    public static final char CURLY_CLOSE = '}';
    public static final char QUOTATION_MARK = '"';
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char T = 't';
    public static final char F = 'f';
    public static final char N = 'n';
    public static final char DIGIT_0 = '0';
    public static final char DIGIT_1 = '1';
    public static final char DIGIT_2 = '2';
    public static final char DIGIT_3 = '3';
    public static final char DIGIT_4 = '4';
    public static final char DIGIT_5 = '5';
    public static final char DIGIT_6 = '6';
    public static final char DIGIT_7 = '7';
    public static final char DIGIT_8 = '8';
    public static final char DIGIT_9 = '9';
    public static final char NEGATIVE_SIGN = '-';
    public static final char BRACKET_OPEN = '[';
    public static final char BRACKET_CLOSE = ']';

    public static boolean isToken(char input) {
	if (CURLY_OPEN == input) {
	    return true;
	} else if (CURLY_CLOSE == input) {
	    return true;
	} else if (QUOTATION_MARK == input) {
	    return true;
	} else if (COMMA == input) {
	    return true;
	} else if (COLON == input) {
	    return true;
	} else if (T == input) {
	    return true;
	} else if (F == input) {
	    return true;
	} else if (N == input) {
	    return true;
	} else if (DIGIT_0 == input) {
	    return true;
	} else if (DIGIT_1 == input) {
	    return true;
	} else if (DIGIT_2 == input) {
	    return true;
	} else if (DIGIT_3 == input) {
	    return true;
	} else if (DIGIT_4 == input) {
	    return true;
	} else if (DIGIT_5 == input) {
	    return true;
	} else if (DIGIT_6 == input) {
	    return true;
	} else if (DIGIT_7 == input) {
	    return true;
	} else if (DIGIT_8 == input) {
	    return true;
	} else if (DIGIT_9 == input) {
	    return true;
	} else if (NEGATIVE_SIGN == input) {
	    return true;
	} else if (BRACKET_OPEN == input) {
	    return true;
	} else if (BRACKET_CLOSE == input) {
	    return true;
	}

	return false;
    }
}