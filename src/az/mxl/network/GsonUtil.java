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
	 * ����json����--Object
	 * 
	 * @param jsonStr
	 *            json����
	 * @param cls
	 *            ��
	 * @return ������ɵ�Model(���json���ݲ����ϸ�ʽ��Model��Ϊnull)
	 */
	public static <T> T getModleByGson(String jsonStr, Class<T> cls) {
		T t = null;
		if (gson == null) {
			gson = new Gson();
		}
		try {
			t = gson.fromJson(jsonStr, cls);
		} catch (Exception e) {
			NetLog.w4defualtTag("json���ݴ���" + jsonStr);
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * ����json����--List
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
			NetLog.w4defualtTag("json���ݴ���" + jsonStr);
			e.printStackTrace();
		}  
		return rs;
	}
	
}
