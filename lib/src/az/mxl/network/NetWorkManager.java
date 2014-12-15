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

	/**
	 * app���ͣ����ڲ�ͬ����app���޸ı����ڵ�ĳЩ����
	 */
	public enum APPTYPE {
		TYPE_1, TYPE_2
	}

	private static boolean isDebug;

	private static APPTYPE APP_TYPE = APPTYPE.TYPE_1;

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
	 * ��ʼ�� NetWorkLib
	 * 
	 * @param context
	 *            application context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * ��ʼ�� NetWorkLib
	 * 
	 * @param context
	 *            application context
	 * @param debug
	 *            ���ÿ���log,����ʱ��ر�
	 */
	public static void init(Context context, boolean debug) {
		mRequestQueue = Volley.newRequestQueue(context);
		isDebug = debug;
	}

	public static void init(Context context, APPTYPE apptype, boolean debug) {
		mRequestQueue = Volley.newRequestQueue(context);
		setAPP_TYPE(apptype);
		isDebug = debug;
	}

	public static APPTYPE getAPP_TYPE() {
		return APP_TYPE;
	}

	public static void setAPP_TYPE(APPTYPE aPP_TYPE) {
		APP_TYPE = aPP_TYPE;
	}

	/**
	 * ���ÿ���log,����ʱ��ر�
	 * 
	 * @param debug
	 */
	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	protected static boolean isDebug() {
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
