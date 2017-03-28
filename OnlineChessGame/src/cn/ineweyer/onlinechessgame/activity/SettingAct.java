package cn.ineweyer.onlinechessgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;

/**
 * 游戏设置控制及其界面类
 * @author LQ
 *
 */
public class SettingAct extends Activity implements OnClickListener{
	
	private ImageView playerChessImg, computerChessImg;
	private LinearLayout document;
	private int playerImgId, computerImgId, bgImgId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
		setContentView(R.layout.setting_layout);
		
		
		playerChessImg =  (ImageView) findViewById(R.id.playerChessImg);
		computerChessImg = (ImageView) findViewById(R.id.computerChessImg);
		document = (LinearLayout) findViewById(R.id.document);
		playerImgId = Config.getPlayerChessColor(SettingAct.this);
		
		playerChessImg.setImageResource(playerImgId);
		computerImgId = Config.getCompeterChessColor(SettingAct.this);
		computerChessImg.setImageResource(computerImgId);
		bgImgId = Config.getChessBg(SettingAct.this);
		document.setBackgroundResource(bgImgId);
		
		//设置监听
		findViewById(R.id.configUpdate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(Config.PLAYER_CHESS_IMG, playerImgId);
				i.putExtra(Config.COMPETER_CHESS_IMG, computerImgId);
				i.putExtra(Config.CHESS_BG, bgImgId);
				
				Config.setPlayerChessColor(SettingAct.this, playerImgId);
				Config.setCompeterChessColor(SettingAct.this, computerImgId);
				Config.setChessBg(SettingAct.this, bgImgId);
				SettingAct.this.setResult(3, i);
				SettingAct.this.finish();
			}
		});
		
		findViewById(R.id.chessImgOne).setOnClickListener(this);
		findViewById(R.id.chessImgTwo).setOnClickListener(this);
		findViewById(R.id.chessImgThree).setOnClickListener(this);
		findViewById(R.id.chessImgFour).setOnClickListener(this);
		findViewById(R.id.chessImgFive).setOnClickListener(this);
		findViewById(R.id.chessImgSix).setOnClickListener(this);
		
		findViewById(R.id.chessImgOneC).setOnClickListener(this);
		findViewById(R.id.chessImgTwoC).setOnClickListener(this);
		findViewById(R.id.chessImgThreeC).setOnClickListener(this);
		findViewById(R.id.chessImgFourC).setOnClickListener(this);
		findViewById(R.id.chessImgFiveC).setOnClickListener(this);
		findViewById(R.id.chessImgSixC).setOnClickListener(this);
		
		findViewById(R.id.chessBgOne).setOnClickListener(this);
		findViewById(R.id.chessBgTwo).setOnClickListener(this);
		findViewById(R.id.chessBgThree).setOnClickListener(this);
		findViewById(R.id.chessBgFour).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean canChoose = true;
		switch(v.getId()) {
		case R.id.chessImgOne :
			if(computerImgId != R.drawable.chess_img1) {
				playerImgId = R.drawable.chess_img1;
				playerChessImg.setImageResource(R.drawable.chess_img1);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img1);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgTwo :
			if(computerImgId != R.drawable.chess_img2) {
				playerImgId = R.drawable.chess_img2;
				playerChessImg.setImageResource(R.drawable.chess_img2);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img2);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgThree :
			if(computerImgId != R.drawable.chess_img3) { 
				playerImgId = R.drawable.chess_img3;
				playerChessImg.setImageResource(R.drawable.chess_img3);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img3);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgFour :
			if(computerImgId != R.drawable.chess_img4) {
				playerImgId = R.drawable.chess_img4;
				playerChessImg.setImageResource(R.drawable.chess_img4);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img4);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgFive :
			if(computerImgId != R.drawable.chess_img5) {
				playerImgId = R.drawable.chess_img5;
				playerChessImg.setImageResource(R.drawable.chess_img5);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img5);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgSix : 
			if(computerImgId != R.drawable.chess_img6) {
				playerImgId = R.drawable.chess_img6;
				playerChessImg.setImageResource(R.drawable.chess_img6);
				Config.setPlayerChessColor(SettingAct.this, R.drawable.chess_img6);
			}
			else {
				canChoose = false;
			}
			break;
			
		case R.id.chessImgOneC :
			if(playerImgId != R.drawable.chess_img1) {
				computerImgId = R.drawable.chess_img1;
				computerChessImg.setImageResource(R.drawable.chess_img1);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img1);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgTwoC :
			if(playerImgId!= R.drawable.chess_img2) {
				computerImgId = R.drawable.chess_img2;
				computerChessImg.setImageResource(R.drawable.chess_img2);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img2);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgThreeC :
			if(playerImgId != R.drawable.chess_img3) { 
				computerImgId = R.drawable.chess_img3;
				computerChessImg.setImageResource(R.drawable.chess_img3);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img3);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgFourC :
			if(playerImgId != R.drawable.chess_img4) {
				computerImgId = R.drawable.chess_img4;
				computerChessImg.setImageResource(R.drawable.chess_img4);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img4);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgFiveC :
			if(playerImgId != R.drawable.chess_img5) {
				computerImgId = R.drawable.chess_img5;
				computerChessImg.setImageResource(R.drawable.chess_img5);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img5);
			}
			else {
				canChoose = false;
			}
			break;
		case R.id.chessImgSixC : 
			if(playerImgId != R.drawable.chess_img6) {
				computerImgId = R.drawable.chess_img6;
				computerChessImg.setImageResource(R.drawable.chess_img6);
				Config.setCompeterChessColor(SettingAct.this, R.drawable.chess_img6);
			}
			else {
				canChoose = false;
			}
			break;
			
		case R.id.chessBgOne :
			bgImgId = R.drawable.chess_bg1;
			document.setBackgroundResource(R.drawable.chess_bg1);
			Config.setChessBg(SettingAct.this, R.drawable.chess_bg1);
			break;
		case R.id.chessBgTwo :
			bgImgId = R.drawable.chess_bg2;
			document.setBackgroundResource(R.drawable.chess_bg2);
			Config.setChessBg(SettingAct.this, R.drawable.chess_bg2);
			break;
		case R.id.chessBgThree :
			bgImgId = R.drawable.chess_bg3;
			document.setBackgroundResource(R.drawable.chess_bg3);
			Config.setChessBg(SettingAct.this, R.drawable.chess_bg3);
			break;
		case R.id.chessBgFour :
			bgImgId = R.drawable.chess_bg4;
			document.setBackgroundResource(R.drawable.chess_bg4);
			Config.setChessBg(SettingAct.this, R.drawable.chess_bg4);
			break;
		}
		if(!canChoose) {
			Toast.makeText(SettingAct.this, R.string.cantChoseComputerImg,Toast.LENGTH_SHORT).show();
		}
	}
}
