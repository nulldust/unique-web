package com.test;

import java.net.URL;

import org.unique.commons.io.IOUtil;


public class TT {

	public static void main(String[] args) {
		URL url = IOUtil.class.getResource("");
		System.out.println(url);
	}
	
}
