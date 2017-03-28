package cn.ineweyer.onlinechessgame;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {

	public static final String APP_ID = "cn.ineweyer.onlienchessgame";
	public static final String CHARSET = "utf-8";
	//棋子配置信息
	public static final int GRID_NUM = 15;
	public static final int CHESS_SIZE = 225;
	public static final int GAME_WIN_NUM = 5;
	public static final int GAME_RESULT_GAMING = 1;
	public static final int GAME_RESULT_LOSE = -1;
	public static final int GAME_RESULT_WIN = 2;
	public static final int GAME_RESULT_EQUAL = 0;
	
	
	//游戏样式配置
	public static final String PLAYER_CHESS_IMG = "player_chess_color";
	public static final String COMPETER_CHESS_IMG = "competer_chess_color";
	public static final String CHESS_BG = "chess_bg";
	public static final int TIME_TO_THINK = 60000;
	public static final int NET_DELAY_TIME = 2000;
	
	//保存信息
	public static final String CACHED_TOKEN = "";
	public static final String CACHED_USERNAME ="";
	
	//应用的服务器地址
	public static final String LOGIN_URL = "http://192.168.1.104:8080/ineweyer/android/login";
	public static final String REGISTER_URL = "http://192.168.1.104:8080/ineweyer/android/register";
	public static final String BLOG_URL = "http://192.168.1.104:8080/ineweyer/person.jsp";
	public static final String SEND_MESSAGE_URL = "http://192.168.1.104:8080/ineweyer/android/sendmessage";
	
	//所有ACTION的KEY值
	public static final String KEY_ACTION = "action";
	public static final String KEY_TOKEN = "info";
	public static final String KEY_ROOM = "room";
	public static final String KEY_CHESS = "chess";
	
	//所有ACTION的值
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_REGISTER = "register";
	public static final String ACTION_GET_ROOM = "get_room";
	public static final String ACTION_CHECK_ROOM = "check_room";
	public static final String ACTION_LEAVE_ROOM = "leave_room";
	public static final String ACTION_GET_CHESS = "get_chess";
	public static final String ACTION_ADD_CHESS = "add_chess";
	public static final String ACTION_REGRET = "regret";
	
	//网络参数信息
	public static final String PARA_USER_NAME = "username";
	public static final String PARA_PASSWORD = "password";
	public static final String PARA_EMAIL = "mail";
	public static final String PARA_ROOM_ID = "id";
	public static final String PARA_ROOM_STATUS = "gaming";
	public static final String PARA_STEP = "step";
	public static final String PARA_LOCATION = "location";
	public static final String PARA_OWNER = "owner";
	public static final String PARA_COMPETER = "competer";
	public static final String PARA_CHESS_ROOM_ID = "room_id";
	public static final String PARA_STEP_ONE = "step_one";
	public static final String PARA_STEP_TWO = "step_two";
	public static final String PARA_STATUS = "status";
	//网络请求结果
	public static final int NET_RESULT_SECUESS = 1;
	public static final int NET_RESULT_FAIL = -1;
	
	//身份标识
	public static final boolean COMPETER = false;
	public static final boolean PLAYER = true;
	
	/**
	 * 保存密码
	 * @param context   上下文
	 * @param password  密码
	 */
	public static void setCachedPassword(Context context, String password) {
		Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		editor.putString(PARA_PASSWORD, password);
		editor.commit();
	}
	
	/**
	 * 保存用户名
	 * @param context    上下文
	 * @param username   用户名
	 */
	public static void setCachedUserName(Context context, String username) {
		Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		editor.putString(PARA_USER_NAME, username);
		editor.commit();
	}
	
	/**
	 * 保存用户设置自己的棋子样式
	 * @param context   上下文
	 * @param color      自己棋子颜色
	 */
	public static void setPlayerChessColor(Context context, int color) {
		Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		editor.putInt(PLAYER_CHESS_IMG, color);
		editor.commit();
	}
	
	/**
	 * 获取已经设置的自己棋子颜色
	 * @param context   上下文
	 * @return  自己棋子颜色
	 */
	public static int getPlayerChessColor(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(PLAYER_CHESS_IMG, 0);
	}
	
	/**
	 * 设置对手棋子颜色
	 * @param context  上下文
	 * @param color    对手棋子颜色
	 */
	public static void setCompeterChessColor(Context context, int color) {
		Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		editor.putInt(COMPETER_CHESS_IMG, color);
		editor.commit();
	}
	
	/**
	 * 获取对手棋子颜色
	 * @param context   上下文
	 * @return  对手棋子颜色
	 */
	public static int getCompeterChessColor(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(COMPETER_CHESS_IMG, 0);
	}
	
	/**
	 * 设置棋盘背景
	 * @param context   上下文
	 * @param chessbg   背景图片资源Id
	 */
	public static void setChessBg(Context context, int chessbg) {
		Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		editor.putInt(CHESS_BG , chessbg);
		editor.commit();
	}
	
	/**
	 * 获取棋盘背景
	 * @param context  上下文
	 * @return 背景图片
	 */
	public static int getChessBg(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(CHESS_BG, 0);
	}
	
	/**
	 * 获取之前已经登陆的用户密码
	 * @param context   上下文
	 * @return  用户密码
	 */
	public static String getCachedPassword(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(PARA_PASSWORD, null);
	}
	
	/**
	 * 获取之前已经登陆的用户名
	 * @param context   上下文
	 * @return  用户名
	 */
	public static String getCachedUserName(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(PARA_USER_NAME, null);
	}
}
