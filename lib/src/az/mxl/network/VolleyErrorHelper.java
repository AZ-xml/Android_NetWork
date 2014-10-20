package az.mxl.network;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorHelper {

	static final String TAG = "VolleyErrorHelper";
	
	/**
	 * Returns appropriate message which is to be displayed to the user against
	 * the specified error object.
	 * 
	 * @param error
	 * @param context
	 * @return
	 */
	public static ErrorInfo getMessage(Object error) {
		if (error instanceof TimeoutError) { 			// Socket，超时服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
			NetLog.w4defualtTag(TAG, "Socket，超时服务器太忙或网络延迟");
			return new ErrorInfo(ErrorInfo.CODE_SERVER_TIME_OUT, ErrorInfo.SERVER_TIME_OUT);
		} else if (error instanceof ParseError) { 		// 在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
			NetLog.w4defualtTag(TAG, "JSON畸形,解析错误");
			return new ErrorInfo(ErrorInfo.CODE_JSON_DEFORMITY, ErrorInfo.JSON_DEFORMITY);
		} else if (isServerProblem(error)) { 			// 服务器错误
			NetLog.w4defualtTag(TAG, "服务器错误");
			return handleServerError(error);
		} else if (isNetworkProblem(error)) { 			// Socket关闭/客户端没有网络连接
			NetLog.w4defualtTag(TAG, "Socket关闭/客户端没有网络连接");
			return new ErrorInfo(ErrorInfo.CODE_NO_INTERNET, ErrorInfo.NO_INTERNET);
		}
		NetLog.w4defualtTag(TAG, "一般错误");
		return new ErrorInfo(ErrorInfo.CODE_GENERIC_ERR, ErrorInfo.GENERIC_ERR); 							// 一般错误
	}

	/**
	 * Determines whether the error is related to network
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isNetworkProblem(Object error) {
		// NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
		// NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
		return (error instanceof NetworkError) || (error instanceof NoConnectionError);
	}

	/**
	 * Determines whether the error is related to server
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isServerProblem(Object error) {
		// SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
		// AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
		return (error instanceof ServerError) || (error instanceof AuthFailureError);
	}

	/**
	 * Handles the server error, tries to determine whether to show a stock
	 * message or to show a message retrieved from the server.
	 * 
	 * @param err
	 * @return
	 */
	private static ErrorInfo handleServerError(Object err) {
		VolleyError error = (VolleyError) err;

		NetworkResponse response = error.networkResponse;

		if (response != null) {
			switch (response.statusCode) {
			case 401: // 未授权,当前请求需要用户验证
			case 404: // 请求失败，请求所希望得到的资源未被在服务器上发现
			case 422: // 请求格式正确，但是由于含有语义错误，无法响应
			case 500: // 服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。
//				try {
//					// server might return error like this { "error": "Some error occured" }
//					// Use "Gson" to parse the result
//					HashMap<String, String> result = new Gson().fromJson(new String(response.data), new TypeToken<Map<String, String>>() {}.getType());
//					if (result != null && result.containsKey("error")) {
//						return result.get("error");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
//				// invalid request
//				return error.getMessage();
				
				NetLog.w4defualtTag(TAG, "服务器错误，code=" + response.statusCode);
				return new ErrorInfo(ErrorInfo.CODE_SERVER_ERR, ErrorInfo.SERVER_ERR);
			default:
				return new ErrorInfo(ErrorInfo.CODE_SERVER_TIME_OUT, ErrorInfo.SERVER_TIME_OUT); // Socket超时
			}
		}
		return new ErrorInfo(ErrorInfo.CODE_GENERIC_ERR, ErrorInfo.GENERIC_ERR); // 一般错误
	}
}
