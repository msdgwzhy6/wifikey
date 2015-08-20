package com.wingsoft.wifikey.util;

import android.view.LayoutInflater;

import com.wingsoft.wifikey.model.Wifi;

import java.util.ArrayList;
import java.util.HashMap;





public class Parser {

	private int i=0;
	private String src;

	public Parser(String str) {
		this.src=str;
	}

	public ArrayList<Wifi> parse() throws Exception {
		ArrayList<Wifi> alni = new ArrayList<Wifi>();

		i=0;

		skipBlank();

		while (i < src.length()) {

			String k=parseKeyString();
			String vs=null;
			Wifi vni=null;
			//System.out.println("k="+k);
			if (src.charAt(i) == '{') {
				vni=mapToInfo(parseBlock());
			} else {
				vs=parseString();
			}

			if (k.toLowerCase().equals("network") && vni != null) {
				alni.add(vni);
				//System.out.println(vni.ssid);
			}

			skipBlank();
		}

		return alni;
	}

	private Wifi mapToInfo(HashMap<String, String> map) {
		Wifi ni = new Wifi();

		if (map.containsKey("ssid")) {
			ni.setSsid(map.get("ssid"));
		}
		if (map.containsKey("psk")) {
			ni.setKey(map.get("psk"));

		}
		return ni;
	}

	private void skipBlank() {
		while(i < src.length() && (src.charAt(i)==' ' || src.charAt(i)=='\n' || src.charAt(i)=='\t' || src.charAt(i)=='\r')) {
			++i;
		}
	}

	private HashMap<String, String> parseBlock() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();

		if (src.charAt(i) != '{') {
			throw new Exception("Unexpected '" + src.charAt(i) + "' at " + String.valueOf(i));
		}
		++i;

		while (i < src.length()) {
			skipBlank();
			if (src.charAt(i) == '}') {
				++i;
				break;
			}
			map.put(parseKeyString(), parseString());
		}
		return map;
	}

	private String parseKeyString() {
		ArrayList<Character> c=new ArrayList<Character>();
		c.add('=');
		return parseString(c);
	}

	private String parseString() {
		ArrayList<Character> c=new ArrayList<Character>();
		c.add('"');
		c.add('\n');
		return parseString(c);
	}

	private String parseString(ArrayList<Character> stringending) {
		StringBuilder sb = new StringBuilder();
		boolean stat = false; //是否进入转义模式
		if (src.charAt(i) == '"') { //跳过起始的引�?
			++i;
		}
		for (; i < src.length(); ++i) {
			if (!stat) {
				if (src.charAt(i) == '\\') {
					stat = true;
				} else if (stringending.indexOf(src.charAt(i))>=0) { //字符串结束符或换�? 来结束一个字符串
					++i;
					break;
				} else {
					sb.append(src.charAt(i));
				}
			} else {
				switch (src.charAt(i)) {
					case 'n':
						sb.append('\n');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'r':
						sb.append('\r');
						break;
					case '\\':
						sb.append('\\');
						break;
					case '"':
						sb.append('"');
						break;
					case '\'':
						sb.append('\'');
						break;
					default:
						break;
				}
				stat = false;
			}
		}
		return sb.toString();
	}

}
