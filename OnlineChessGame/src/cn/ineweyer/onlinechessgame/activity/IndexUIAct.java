package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;

/**
 * 进入游戏后的主界面类，用于显示界面及其处理逻辑
 * @author LQ
 *
 */
public class IndexUIAct extends Activity implements OnClickListener{
	private Button easyGame, startGame, watchGame, watchBlog, exit, loginOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//获取对应的按钮
		easyGame = (Button) findViewById(R.id.startEasyGame);
		startGame = (Button) findViewById(R.id.startGame);
		watchGame = (Button) findViewById(R.id.watchGame);
		watchBlog = (Button) findViewById(R.id.watchBlog);
		exit = (Button) findViewById(R.id.exit);
		loginOut = (Button) findViewById(R.id.loginOut);
		
		//设置点击事件监听器
		easyGame.setOnClickListener(this);
		startGame.setOnClickListener(this);
		watchGame.setOnClickListener(this);
		watchBlog.setOnClickListener(this);
		exit.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.startEasyGame :
			startActivity(new Intent(IndexUIAct.this, EasyChessAct.class));
			finish();
			break;
		case R.id.startGame :
			startActivity(new Intent(IndexUIAct.this, IntelligentChessAct.class));
			finish();
			break;
		case R.id.watchGame :
			startActivity(new Intent(IndexUIAct.this, OnlineGameAct.class));
			finish();
			break;
		case R.id.watchBlog :
			startActivity(new Intent(IndexUIAct.this, WatchBlogAct.class));
			finish();
			break;
		case R.id.exit :
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
			System.exit(0);
			break;
		case R.id.loginOut :
			//app文件中的登陆信息
			Config.setCachedPassword(IndexUIAct.this, null);
			Config.setCachedUserName(IndexUIAct.this, null);
			//启动登陆界面
			startActivity(new Intent(IndexUIAct.this, LoginAct.class));
			finish();
			break;
		default: 
			break;
		}
	}
}
