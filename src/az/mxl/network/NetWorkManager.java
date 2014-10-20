package az.mxl.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Manager for the queue
 * 
 * @author
 * 
 */
public class NetWorkManager {

	private static boolean isDebug;
	
	/**
	 * the queue :-)
	 */
	private static RequestQueue mRequestQueue;

	/**
	 * Nothing to see here.
	 */
	private NetWorkManager() {
		// no instances
	}
	
	/**
	 * 初始化 NetWorkLib
	 * @param context
	 *            application context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * 设置开启log,发布时请关闭
	 * @param debug
	 */
	public static void setDebug(boolean debug){
		isDebug = debug;
	}

	protected static boolean isDebug(){
		return isDebug;
	}
	
	/**
	 * @return instance of the queue
	 * @throws IllegalStatException
	 *             if init has not yet been called
	 */
	public synchronized static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("Not initialized");
		}
	}

}
