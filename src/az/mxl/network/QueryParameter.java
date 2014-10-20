package az.mxl.network;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import android.text.TextUtils;

public class QueryParameter {

	private static QueryParameter qp;
	private HashMap<String, Object> root;

	private QueryParameter() {
		root = new HashMap<String, Object>();
	}

	public static QueryParameter Builder(){
		qp = new QueryParameter();
		return qp;
	}
	
	public HashMap<String, Object> getParame(){
		return root;
	}
	
	/**
	 * 添加字段
	 * @param key
	 * @param value
	 * @return
	 */
	public QueryParameter put(String key, Object value) {
		try {
			root.put(key, value);
			return qp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 清空字段
	 */
	public void clear(){
		root.clear();
	}
	
	@Override
	public String toString() {
		if (root == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : root.entrySet()) {
			sb.append(urlEncode(e.getKey() == null ? "" : e.getKey().toString()));
			sb.append("=");
			sb.append(urlEncode(e.getValue() == null ? "" : e.getValue().toString()));
			sb.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	/**
	 * url编码--> 只编码字段，不能编码其他的
	 * 
	 * @param str
	 * @return
	 */
	private String urlEncode(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
