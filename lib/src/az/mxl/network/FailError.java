package az.mxl.network;


import com.android.volley.VolleyError;

/**
 * ∑√Œ ¥ÌŒÛ(code != ok)
 * @author mxl
 */
@SuppressWarnings("serial")
public class FailError extends VolleyError {

	private String msg;

	public FailError(String msg) {
		this.msg = msg;
	}

	public ErrorInfo getMsg() {
		return new ErrorInfo(ErrorInfo.CODE_ERR, msg);
	}

}
