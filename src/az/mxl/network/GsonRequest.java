package az.mxl.network;


import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonParseException;

/**
 * Wrapper for Volley requests to facilitate parsing of json responses.
 * 
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {

	/**
	 * Class type for the response
	 */
	private final Class<T> mClass;

	/**
	 * Callback for response delivery
	 */
	private final Listener<T> mListener;

	/**
	 * @param method
	 *            Request type.. Method.GET etc
	 * @param url
	 *            path for the requests
	 * @param objectClass
	 *            expected class type for the response. Used by gson for
	 *            serialization.
	 * @param listener
	 *            handler for the response
	 * @param errorListener
	 *            handler for errors
	 */
	public GsonRequest(int method, String url, Class<T> objectClass, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mClass = objectClass;
		this.mListener = listener;
	}
	
	@Override
	protected void deliverResponse(T model) {
		mListener.onResponse(model);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			NetLog.w4defualtTag(json);
			JSONObject jsonObject = new JSONObject(json);
			JSONObject msg = jsonObject.getJSONObject("msg");
			if("ok".equals(jsonObject.getString("code"))){
				T t = GsonUtil.getModleByGson(msg.toString(), mClass);
				if(t == null){
					NetLog.w4defualtTag("bean解析失败");
					return Response.error(new FailError("数据错误"));
				} else{
					NetLog.w4defualtTag("网络数据获取成功(code = ok)");
					return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
				}
			} else 
				NetLog.w4defualtTag("code != ok");
				return Response.error(new FailError(msg.getString("alertMsg")));
		} catch (UnsupportedEncodingException e) {// 编码
			return Response.error(new ParseError(e));
		} catch (JsonParseException e) {// json解析err
			return Response.error(new ParseError(e));
		} catch (JSONException e) {// json解析err
			return Response.error(new ParseError(e));
		} 
	}
	
}
