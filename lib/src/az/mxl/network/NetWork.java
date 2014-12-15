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
	
	/** 是否正在刷新 */
	private boolean refreshing;
	// 超时时间
	protected int initialTimeoutMs;

	/** 接口名称 */
	private String server;

	/**
	 * Class type for the response
	 */
	private Class<T> mClass;

	/**
	 * 生成网络工作类(GET方式)
	 * @param server 接口名
	 * @param mClass 解析类
	 */
	public NetWork(String server, Class<T> mClass) {
		this.requestQueue = NetWorkManager.getRequestQueue();
		this.server = server;
		this.mClass = mClass;
	}
	
	/**
	 * 生成网络工作类
	 * @param mMethod 请求方式
	 * @param server 接口名
	 * @param mClass 解析类
	 */
	public NetWork(int mMethod, String server, Class<T> mClass) {
		this(server, mClass);
		this.mMethod = mMethod;
	}

	/**
	 * 生成网络工作类(GET方式)
	 * @param server 接口名
	 * @param mClass 解析类
	 * @param listener 网络回调
	 */
	public NetWork(String server, Class<T> mClass, OnDataSourceListener<T> listener) {
		this(server, mClass);
		this.listener = listener;		
	}
	
	/**
	 * 生成网络工作类
	 * @param mMethod 请求方式
	 * @param server 接口名
	 * @param mClass 解析类
	 * @param listener 网络回调
	 */
	public NetWork(int mMethod, String server, Class<T> mClass, OnDataSourceListener<T> listener) {
		this(server, mClass, listener);
		this.mMethod = mMethod;
	}

	/**
	 * 生成网络工作类(GET方式)
	 * @param server 接口名
	 * @param mClass 解析类
	 * @param listener 网络"成功"回调
	 */
	public NetWork(String server, Class<T> mClass, OnDataSourceSuccessListener<T> listener) {
		this(server, mClass);
		this.sucListener = listener;
	}

	/**
	 * 生成网络工作类
	 * @param mMethod 请求方式
	 * @param server 接口名
	 * @param mClass 解析类
	 * @param listener 网络"成功"回调
	 */
	public NetWork(int mMethod, String server, Class<T> mClass, OnDataSourceSuccessListener<T> listener) {
		this(server, mClass, listener);
		this.mMethod = mMethod;
	}
	
	/**
	 * 设置超时时间
	 * @param initialTimeoutMs
	 */
	public void setInitialTimeoutMs(int initialTimeoutMs){
		this.initialTimeoutMs = initialTimeoutMs;
	}
	
	/**
	 * 设置接口名
	 * @param server
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * 设置解析类
	 * @param mClass
	 */
	public void setClass(Class<T> mClass) {
		this.mClass = mClass;
	}

	/**
	 * 是否正在刷新数据
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
		// 开始网络请求
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
	 * 获取host(根据isDebug判断----返回线上、测试host)
	 * 
	 * @return
	 */
	public abstract String getHost();

	/**
	 * 获取必要字段(每个接口需要，如：session_id=87383)
	 * @see 这里的字段无论是GET、POST都添加到了URL中（如果POST方式需添加到表单中，请自行修改源码）
	 * @return
	 */
	public String getOfenParameter() {
		return null;
	}

	/**
	 * 打印(api拼接完后会执行次方法)
	 * 
	 * @api
	 */
	public abstract void debugApi(String api);


	/**
	 * request方法
	 * @param method 请求方式
	 * @param server 接口名称
	 * @param queryParameter 参数
	 * @param tag
	 * @param mClass 解析class
	 * @param listener 监听
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
	 * 获取url
	 * @param server 接口名称
	 * @param queryParameter GET方式参数（如果是POST方式，请传Null）
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
	 * GET方式请求
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
	 * POST方式请求
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
				// 生成表单
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
	 * 开始request
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
