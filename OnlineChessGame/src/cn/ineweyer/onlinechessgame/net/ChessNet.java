package cn.ineweyer.onlinechessgame.net;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ineweyer.onlinechessgame.Config;

/**
 * 棋子网络操作类操作
 * @author LQ
 *
 */
public class ChessNet {
	
	/**
	 *获取指定位置棋子网络请求 
	 * @param roomId      房间id
	 * @param step        棋子步数
	 * @param success     请求成功接口
	 * @param fail        请求失败接口
	 * @return
	 */
	public static String getChess(final int roomId, final int step, final SeccuessCallBack success, final FailCallBack fail) {
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								json = (JSONObject) json.getJSONArray(Config.KEY_CHESS).get(0);
								success.onSeccuess(json);
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
		}, Config.KEY_ACTION,Config.ACTION_GET_CHESS,
		Config.PARA_CHESS_ROOM_ID, roomId+"",
		Config.PARA_STEP, step+"");
		
		return null;
	}
	
	/**
	 * 添加一枚棋子的网络请求
	 * @param roomId     房间id
	 * @param step       棋子步数
	 * @param location   棋子位置
	 * @param owner      棋子所有者
	 * @param success    请求成功接口
	 * @param fail       请求失败接口
	 * @return
	 */
	public static String addChess(final int roomId, final int step, final int location, String owner,  
		 final SeccuessCallBack success, final FailCallBack fail) {
		 
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								success.onSeccuess(null);
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
		}, Config.KEY_ACTION,Config.ACTION_ADD_CHESS,
		Config.PARA_CHESS_ROOM_ID, roomId+"",
		Config.PARA_STEP, step+"",
		Config.PARA_LOCATION, location +"" ,
		Config.PARA_OWNER, owner);
		
		return null;
	}
	
	/**
	 * 发送悔棋网络请求
	 * @param room_id_one   房间id
	 * @param step_one      悔棋的第一步
	 * @param step_two      悔棋的第二步
	 * @param success       悔棋成功时的接口
	 * @param fail          悔棋失败的接口
	 * @return
	 */
	public static String regret(final int room_id, final int step, 
		 final SeccuessCallBack success, final FailCallBack fail) {
		 
		new NetConnection(Config.SEND_MESSAGE_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					try {
						System.out.println(result);
						JSONObject json = new JSONObject(result);
						switch(json.getInt(Config.PARA_STATUS)) {
						case Config.NET_RESULT_SECUESS :
							if(success != null) {
								success.onSeccuess(null);
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
		}, Config.KEY_ACTION,Config.ACTION_REGRET,
		Config.PARA_CHESS_ROOM_ID, room_id + "",
		Config.PARA_STEP, step + "");
		
		return null;
	}
	
	
	public static interface SeccuessCallBack {
		public void onSeccuess(JSONObject json);
	}
	public static interface FailCallBack {
		public void onFail(String result);
	}
}
