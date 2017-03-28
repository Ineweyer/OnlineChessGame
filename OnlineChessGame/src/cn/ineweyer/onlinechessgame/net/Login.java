package cn.ineweyer.onlinechessgame.net;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ineweyer.onlinechessgame.Config;

/**
 * 用户登录网络操作类
 * @author LQ
 *
 */
public class Login {
	/**
	 *登陆网络请求
	 * @param username  用户名
	 * @param password  密码
	 * @param success   请求成功接口
	 * @param fail      请求失败接口
	 * @return
	 */
	public static String login(String username, String password, final SeccuessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.LOGIN_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								success.onSeccuess(Config.KEY_TOKEN);
							}
							break;
							default: 
								if(fail != null) {
									fail.onFail(json.getString(Config.KEY_TOKEN));
								}
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if(fail != null) {
							fail.onFail("网络传输出错");
						}
					}
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail(String result) {
				if(fail != null) {
					fail.onFail("请检查网络");
				}
			}
		}, Config.KEY_ACTION,Config.ACTION_LOGIN,
		Config.PARA_USER_NAME, username,
		Config.PARA_PASSWORD, password);
		
		return null;
	}
	
	public static interface SeccuessCallBack {
		public void onSeccuess(String token);
	}
	public static interface FailCallBack {
		public void onFail(String result);
	}
	
}
