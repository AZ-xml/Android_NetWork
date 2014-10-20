package az.mxl.network;

/**
 * ������Ϣ
 * @author mxl
 *
 */
public class ErrorInfo {

	/** JSON�����У�code != ok*/
	public static final int CODE_ERR = -1;				
	// public static final String //	msg�Ǻ�̨����������
	
	/** json���Σ�����ʧ��*/
	public static final int CODE_JSON_DEFORMITY = 0;				
	public static final String JSON_DEFORMITY = "���ݴ�������ϵ�ͷ�";	

	/** Socket��ʱ*/
	public static final int CODE_SERVER_TIME_OUT = 1; 				
	public static final String SERVER_TIME_OUT = "�����źŲ�,����ʧ��"; 	

	/** Socket�ر�/�ͻ���û����������*/
	public static final int CODE_NO_INTERNET = 2;					
	public static final String NO_INTERNET = "��������ʧ��";			

	/** һ�����*/
	public static final int CODE_GENERIC_ERR = 3;					
	public static final String GENERIC_ERR = "�������,����ʧ��";			

	/** ����������,code != 200*/
	public static final int CODE_SERVER_ERR = 4;					
	public static final String SERVER_ERR = "��������������ϵ�ͷ�";		
	
	
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
