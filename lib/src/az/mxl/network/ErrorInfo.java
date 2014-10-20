package az.mxl.network;

/**
 * 错误信息
 * @author mxl
 *
 */
public class ErrorInfo {

	/** JSON数据中，code != ok*/
	public static final int CODE_ERR = -1;				
	// public static final String //	msg是后台服务器给的
	
	/** json畸形，解析失败*/
	public static final int CODE_JSON_DEFORMITY = 0;				
	public static final String JSON_DEFORMITY = "数据错误，请联系客服";	

	/** Socket超时*/
	public static final int CODE_SERVER_TIME_OUT = 1; 				
	public static final String SERVER_TIME_OUT = "网络信号差,加载失败"; 	

	/** Socket关闭/客户端没有网络连接*/
	public static final int CODE_NO_INTERNET = 2;					
	public static final String NO_INTERNET = "网络连接失败";			

	/** 一般错误*/
	public static final int CODE_GENERIC_ERR = 3;					
	public static final String GENERIC_ERR = "网络错误,加载失败";			

	/** 服务器错误,code != 200*/
	public static final int CODE_SERVER_ERR = 4;					
	public static final String SERVER_ERR = "服务器错误，请联系客服";		
	
	
	private int code;
	private String msg;
	
	public ErrorInfo(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
}
