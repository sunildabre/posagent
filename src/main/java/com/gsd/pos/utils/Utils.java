package com.gsd.pos.utils;

public class Utils {
	
	public static String make4CharsLong(int in) {
		StringBuffer sb = new StringBuffer();
		String s = String.valueOf(in);
		if (s.length() > 4) {
			return "9999";
		}
		if (s.length() == 4) {
			return s;
		}
		for (int i = 0; i < (4 - s.length()); i++) {
			sb.append("0");
		}
		sb.append(s);
		System.out.println(" Length [" + sb.toString() + "]");
		return sb.toString();
	}


}
