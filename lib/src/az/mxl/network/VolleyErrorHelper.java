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
		if (error instanceof TimeoutError) { 			// Socket����ʱ������̫æ�������ӳٻ��������쳣��Ĭ������£�Volley�ĳ�ʱʱ��Ϊ2.5�롣����õ�����������ʹ��RetryPolicy��
			NetLog.w4defualtTag(TAG, "Socket����ʱ������̫æ�������ӳ�");
			return new ErrorInfo(ErrorInfo.CODE_SERVER_TIME_OUT, ErrorInfo.SERVER_TIME_OUT);
		} else if (error instanceof ParseError) { 		// ��ʹ��JsonObjectRequest��JsonArrayRequestʱ��������յ���JSON�ǻ��Σ�������쳣��
			NetLog.w4defualtTag(TAG, "JSON����,��������");
			return new ErrorInfo(ErrorInfo.CODE_JSON_DEFORMITY, ErrorInfo.JSON_DEFORMITY);
		} else if (isServerProblem(error)) { 			// ����������
			NetLog.w4defualtTag(TAG, "����������");
			return handleServerError(error);
		} else if (isNetworkProblem(error)) { 			// Socket�ر�/�ͻ���û����������
			NetLog.w4defualtTag(TAG, "Socket�ر�/�ͻ���û����������");
			return new ErrorInfo(ErrorInfo.CODE_NO_INTERNET, ErrorInfo.NO_INTERNET);
		}
		NetLog.w4defualtTag(TAG, "һ�����");
		return new ErrorInfo(ErrorInfo.CODE_GENERIC_ERR, ErrorInfo.GENERIC_ERR); 							// һ�����
	}

	/**
	 * Determines whether the error is related to network
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isNetworkProblem(Object error) {
		// NetworkError��Socket�رգ�������崻���DNS���󶼻�����������
		// NoConnectionError����NetworkError���ƣ�����ǿͻ���û���������ӡ�
		return (error instanceof NetworkError) || (error instanceof NoConnectionError);
	}

	/**
	 * Determines whether the error is related to server
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isServerProblem(Object error) {
		// SERVERERROR������������Ӧ��һ���������п��ܵ�4xx��5xx HTTP״̬���롣
		// AuthFailureError���������һ��HTTP�������֤�����ܻᷢ���������
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
			case 401: // δ��Ȩ,��ǰ������Ҫ�û���֤
			case 404: // ����ʧ�ܣ�������ϣ���õ�����Դδ���ڷ������Ϸ���
			case 422: // �����ʽ��ȷ���������ں�����������޷���Ӧ
			case 500: // ������������һ��δ��Ԥ�ϵ�״�������������޷���ɶ�����Ĵ���һ����˵��������ⶼ���ڷ������ĳ��������ʱ���֡�
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
				
				NetLog.w4defualtTag(TAG, "����������code=" + response.statusCode);
				return new ErrorInfo(ErrorInfo.CODE_SERVER_ERR, ErrorInfo.SERVER_ERR);
			default:
				return new ErrorInfo(ErrorInfo.CODE_SERVER_TIME_OUT, ErrorInfo.SERVER_TIME_OUT); // Socket��ʱ
			}
		}
		return new ErrorInfo(ErrorInfo.CODE_GENERIC_ERR, ErrorInfo.GENERIC_ERR); // һ�����
	}
}
