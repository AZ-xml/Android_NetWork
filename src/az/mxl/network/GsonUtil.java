package az.mxl.network;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class GsonUtil {

	private static Gson gson;
	
	/**
	 * 解析json数据--Object
	 * 
	 * @param jsonStr
	 *            json数据
	 * @param cls
	 *            类
	 * @return 解析完成的Model(如果json数据不符合格式，Model会为null)
	 */
	public static <T> T getModleByGson(String jsonStr, Class<T> cls) {
		T t = null;
		if (gson == null) {
			gson = new Gson();
		}
		try {
			t = gson.fromJson(jsonStr, cls);
		} catch (Exception e) {
			NetLog.w4defualtTag("json数据错误：" + jsonStr);
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 解析json数据--List
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> List<T> getModel(String jsonStr){
		List<T> rs = null;
		if (gson == null) {
			gson = new Gson();
		}
		try {
			Type type = new TypeToken<ArrayList<T>>() {}.getType();  
			rs =gson.fromJson(jsonStr, type);
		} catch (JsonSyntaxException e) {
			NetLog.w4defualtTag("json数据错误：" + jsonStr);
			e.printStackTrace();
		}  
		return rs;
	}
	
}
