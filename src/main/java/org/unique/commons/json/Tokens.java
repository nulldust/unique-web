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