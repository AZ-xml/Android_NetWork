package az.mxl.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class NetWork<T> extends NetWork4Base<T> implements Response.Listener<T>, Response.ErrorListener {

	protected RequestQueue requestQueue;
	protected GsonRequest<T> request;

	/** Request method of this request.  Currently supports GET, POST. */
    private int mMethod = Method.GET;
	
	/** �Ƿ�����ˢ�� */
	private boolean refreshing;
	// ��ʱʱ��
	protected int initialTimeoutMs;

	/** �ӿ����� */
	private String server;

	/**
	 * Class type for the response
	 */
	private Class<T> mClass;

	/**
	 * �������繤����(GET��ʽ)
	 * @param server �ӿ���
	 * @param mClass ������
	 */
	public NetWork(String server, Class<T> mClass) {
		this.requestQueue = NetWorkManager.getRequestQueue();
		this.server = server;
		this.mClass = mClass;
	}
	
	/**
	 * �������繤����
	 * @param mMethod ����ʽ
	 * @param server �ӿ���
	 * @param mClass ������
	 */
	public NetWork(int mMethod, String server, Class<T> mClass) {
		this(server, mClass);
		this.mMethod = mMethod;
	}

	/**
	 * �������繤����(GET��ʽ)
	 * @param server �ӿ���
	 * @param mClass ������
	 * @param listener ����ص�
	 */
	public NetWork(String server, Class<T> mClass, OnDataSourceListener<T> listener) {
		this(server, mClass);
		this.listener = listener;		
	}
	
	/**
	 * �������繤����
	 * @param mMethod ����ʽ
	 * @param server �ӿ���
	 * @param mClass ������
	 * @param listener ����ص�
	 */
	public NetWork(int mMethod, String server, Class<T> mClass, OnDataSourceListener<T> listener) {
		this(server, mClass, listener);
		this.mMethod = mMethod;
	}

	/**
	 * �������繤����(GET��ʽ)
	 * @param server �ӿ���
	 * @param mClass ������
	 * @param listener ����"�ɹ�"�ص�
	 */
	public NetWork(String server, Class<T> mClass, OnDataSourceSuccessListener<T> listener) {
		this(server, mClass);
		this.sucListener = listener;
	}

	/**
	 * �������繤����
	 * @param mMethod ����ʽ
	 * @param server �ӿ���
	 * @param mClass ������
	 * @param listener ����"�ɹ�"�ص�
	 */
	public NetWork(int mMethod, String server, Class<T> mClass, OnDataSourceSuccessListener<T> listener) {
		this(server, mClass, listener);
		this.mMethod = mMethod;
	}
	
	/**
	 * ���ó�ʱʱ��
	 * @param initialTimeoutMs
	 */
	public void setInitialTimeoutMs(int initialTimeoutMs){
		this.initialTimeoutMs = initialTimeoutMs;
	}
	
	/**
	 * ���ýӿ���
	 * @param server
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * ���ý�����
	 * @param mClass
	 */
	public void setClass(Class<T> mClass) {
		this.mClass = mClass;
	}

	/**
	 * �Ƿ�����ˢ������
	 * 
	 * @return
	 */
	public boolean isRefreshing() {
		return refreshing;
	}

	@Override
	public void start() {
		start("start");
	}

	@Override
	public void start(Object tag) {
		// ��ʼ��������
		refreshing = true;
		if (listener != null)
			listener.onPerData();

		if (TextUtils.isEmpty(server)) {
			throw new IllegalStateException("server is null");
		}
		if (request != null) {
			request.cancel();
		}
			
		if (listener != null) {
			request = request(this.mMethod, server, listener.getQueryParameter(), tag, mClass, this, this);
		} else if (sucListener != null) {
			request = request(this.mMethod, server, sucListener.getQueryParameter(), tag, mClass, this, this);
		} else {
			throw new IllegalStateException("listener and sucListener is null");
		}
	}

	@Override
	public void startMore() {
		startMore("startMore");
	}

	public void startMore(String tag) {
		super.startMore();
		//
	}

	/**
	 * ��ȡhost(����isDebug�ж�----�������ϡ�����host)
	 * 
	 * @return
	 */
	public abstract String getHost();

	/**
	 * ��ȡ��Ҫ�ֶ�(ÿ���ӿ���Ҫ���磺session_id=87383)
	 * @see ������ֶ�������GET��POST����ӵ���URL�У����POST��ʽ����ӵ����У��������޸�Դ�룩
	 * @return
	 */
	public String getOfenParameter() {
		return null;
	}

	/**
	 * ��ӡ(apiƴ������ִ�дη���)
	 * 
	 * @api
	 */
	public abstract void debugApi(String api);


	/**
	 * request����
	 * @param method ����ʽ
	 * @param server �ӿ�����
	 * @param queryParameter ����
	 * @param tag
	 * @param mClass ����class
	 * @param listener ����
	 * @param errorListener
	 * @return
	 */
	private GsonRequest<T> request(int method, String server, QueryParameter queryParameter, Object tag, Class<T> mClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		switch (method) {
		case Request.Method.GET:
			return request4Get(getUrl(server, queryParameter), tag, mClass, listener, errorListener);
		case Request.Method.POST:
		default:
			return request4Post(getUrl(server, null), queryParameter, tag, mClass, listener, errorListener);
		}
	}
	
	/**
	 * ��ȡurl
	 * @param server �ӿ�����
	 * @param queryParameter GET��ʽ�����������POST��ʽ���봫Null��
	 * @return
	 */
	private String getUrl(String server, QueryParameter queryParameter){
		StringBuffer sb = new StringBuffer();
		sb.append(getHost()).append(server);
		if (!TextUtils.isEmpty(getOfenParameter())) {
			sb.append("?").append(getOfenParameter());
			if (queryParameter != null) {
				sb.append("&").append(queryParameter);
			}
		} else if (queryParameter != null) {
			sb.append("?").append(queryParameter);
		}
		debugApi(sb.toString());
		return sb.toString();
	}
	
	/**
	 * GET��ʽ����
	 * @param requestUrl
	 * @param tag
	 * @param mClass
	 * @param listener
	 * @param errorListener
	 * @return
	 */
	private GsonRequest<T> request4Get(String requestUrl, Object tag, Class<T> mClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		request = new GsonRequest<T>(Request.Method.GET, requestUrl, mClass, listener, errorListener);
		request.setTag(tag);
		return request(request);
	}
	
	/**
	 * POST��ʽ����
	 * @param requestUrl
	 * @param queryParameter
	 * @param tag
	 * @param mClass
	 * @param listener
	 * @param errorListener
	 * @return
	 */
	private GsonRequest<T> request4Post(String requestUrl, final QueryParameter queryParameter, Object tag, Class<T> mClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		request = new GsonRequest<T>(Request.Method.POST, requestUrl, mClass, listener, errorListener){
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				// ���ɱ�
				if (queryParameter == null) 
					return params;
				for (Entry<String, Object> e : queryParameter.getParame().entrySet()) {
					params.put(e.getKey(), e.getValue() == null ? "" : e.getValue().toString());
				}
				return params;
			}
		};
		request.setTag(tag);
		return request(request);
	}
	
	/**
	 * ��ʼrequest
	 * @param request
	 * @return
	 */
	private GsonRequest<T> request(GsonRequest<T> request){
		request.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs == 0 ? (12 * 1000) : initialTimeoutMs, 1, 1.0f));
		request.setShouldCache(false);
		requestQueue.add(request);
		return request;
	}
	
	
	@Override
	public void onResponse(T response) {
		refreshing = false;
		if (request == null) {
			return;
		}
		if (listener != null) {
			listener.onFinishData();
			listener.onSuccessData(response, request.getTag());
		}
		if (sucListener != null) {
			sucListener.onSuccessData(response, request.getTag());
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		refreshing = false;
		if (error instanceof FailError) {
			ErrorInfo msg = ((FailError) error).getMsg();
			if (listener != null)
				listener.onFailData(msg, request.getTag());
		} else {
			ErrorInfo msg = VolleyErrorHelper.getMessage(error);
			if (listener != null)
				listener.onFailData(msg, request.getTag());
		}
		if (listener != null)
			listener.onFinishData();
	}

}
