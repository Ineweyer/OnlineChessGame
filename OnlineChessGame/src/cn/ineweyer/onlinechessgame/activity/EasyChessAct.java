package cn.ineweyer.onlinechessgame.activity;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;
import cn.ineweyer.onlinechessgame.game.SimpleComputerBrain;

/**
 * 简单的新手陪练
 * @author LQ
 *
 */
public class EasyChessAct extends Activity implements OnClickListener{
	private GridView chessGrid;                    //棋盘
	private RelativeLayout linear;                 //棋盘背景布局
	private ImageAdapter imageAdapter;             //棋盘GRIDView棋子适配器
	private boolean notsetAdapter = true;          //设置适配器标识
	private SimpleComputerBrain chessManager;      //简单智能下棋
	private Chronometer timer;                     //计时器
	boolean noteTimer = true;                      //开始下棋时设置下棋
	private int computerImg;                       //电脑棋子图片
	private int playerImg;                         //玩家棋子图片
	private int chessBg;                           //棋盘背景图片
	private TextView currentPlayer;                //当前下棋者
	private boolean canDo = true;                  //是否允许下棋
	private Handler imageHandler;                  //用户棋子的闪动效果
	private ImageView currentChess = null;         //当前对手所下的棋子
	protected int chessAniTime = 0;                //闪动持续的时间
	 
	//延迟电脑下棋速度
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0123) {
				int pos = chessManager.autoPlay();
				if(pos >= 0){
					currentChess = ((ImageView)chessGrid.getChildAt(pos));
					//发送棋子动画请求
					new Timer().schedule(new TimerTask(){    
						public void run() {  
							Message message = new Message();      
							message.what = 0555;      
							imageHandler.sendMessage(message);     
						}            
					}, 50);
				}
				currentPlayer.setText(R.string.player);
				canDo = true;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);				// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);		// 设置全屏
		setContentView(R.layout.game_with_computer);

		
		computerImg = Config.getCompeterChessColor(EasyChessAct.this);
		playerImg = Config.getPlayerChessColor(EasyChessAct.this);
		chessBg = Config.getChessBg(EasyChessAct.this);
		
		
		//初始化棋盘
		findViewById(R.id.document).setBackgroundResource(chessBg);
		currentPlayer = (TextView) findViewById(R.id.currentPlayer);
		chessGrid = (GridView) findViewById(R.id.chessWithComputerBgGrid);
		setAdapter();
		
		
		//初始化棋盘管理者
		chessManager = new SimpleComputerBrain();
		
		//绑定点击事件
		findViewById(R.id.regretBtn).setOnClickListener(this);
		findViewById(R.id.giveUpBtn).setOnClickListener(this);
		findViewById(R.id.gameSetting).setOnClickListener(this);
		findViewById(R.id.exitGame).setOnClickListener(this);
		
		((ImageView)(findViewById(R.id.computerChessImg))).setImageResource(computerImg);
		((ImageView)(findViewById(R.id.playerChessImg))).setImageResource(playerImg);
		findViewById(R.id.document).setBackgroundResource(chessBg);
		
		//初始化计时部分
		timer = (Chronometer)findViewById(R.id.time);
		
		//棋子闪动效果
		imageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int what = msg.what;
				if (what == 0555 || what == 0666) {
					final int info;
					if(what == 0555) {
						currentChess.setImageResource(R.drawable.chess_ani1);
						info = 0666;
					}
					else {
						currentChess.setImageResource(R.drawable.chess_ani2);
						info = 0555;
					}
					//进行下一次动画
					if(chessAniTime++ < Config.GRID_NUM) {
						//发送棋子动画请求
						new Timer().schedule(new TimerTask(){    
							public void run() {  
								Message message = new Message();      
								message.what = info;      
								System.out.println(info);
								imageHandler.sendMessage(message);     
							}            
						}, 50);
					}
					else {
						currentChess.setImageResource(computerImg);
						chessAniTime = 0;
						canDo = true;
					}
				}
			}
			};
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == 3) {
			playerImg = Config.getPlayerChessColor(EasyChessAct.this);
			computerImg = Config.getCompeterChessColor(EasyChessAct.this);
			chessBg = Config.getChessBg(EasyChessAct.this);
			
			findViewById(R.id.document).setBackgroundResource(chessBg);
			chessManager.freshComputerChess(chessGrid, computerImg);
			chessManager.freshPlayChess(chessGrid, playerImg);
			timer.start();
		}
	}
		
	@Override
	protected void onStart() {
		super.onStart();
	
		playerImg = Config.getPlayerChessColor(EasyChessAct.this);
		computerImg = Config.getCompeterChessColor(EasyChessAct.this);
		chessBg = Config.getChessBg(EasyChessAct.this);
		
		//刷新界面
		findViewById(R.id.document).setBackgroundResource(chessBg);
		((ImageView)(findViewById(R.id.computerChessImg))).setImageResource(computerImg);
		((ImageView)(findViewById(R.id.playerChessImg))).setImageResource(playerImg);
		chessManager.freshComputerChess(chessGrid, computerImg);
		chessManager.freshPlayChess(chessGrid, playerImg);
		timer.start();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regretBtn:
			int[] position = chessManager.playerRegret();
			for (int i = 0; i < position.length; i++) {
				((ImageView) chessGrid.getChildAt(position[i]))
						.setImageResource(0);
			}
			break;
		case R.id.giveUpBtn:
			newGame();
			break;
		case R.id.gameSetting: 
			timer.stop();
			startActivityForResult(new Intent(EasyChessAct.this, SettingAct.class), 1);
			break;
		case R.id.exitGame:
			exitGame();
			break;
		}
	}
	
	/**
	 *  设置adapter
	 */
	private void setAdapter() {
		// 创建一个List对象，List对象的元素是Map
		ArrayList<Integer> imageList = new ArrayList<Integer>();
		for (int i = 0; i < Config.CHESS_SIZE; i++) {
			imageList.add(0);
		}

		chessGrid = (GridView) findViewById(R.id.chessWithComputerBgGrid);
		imageAdapter = new ImageAdapter(this, imageList);
		linear = (RelativeLayout) findViewById(R.id.chessWithComputerBg);
		//第一次加載頁面設置adapter
		linear.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						linear.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (notsetAdapter) {
									imageAdapter.setWidth((int) (linear.getWidth() / Config.GRID_NUM));
									imageAdapter.setHeight((int) (linear.getHeight() / (Config.GRID_NUM+1)));
									chessGrid.setAdapter(imageAdapter);
									notsetAdapter = false;
								}
							}
						}, 300);
					}
				});

		// 添加列表项被选中的监听器
		chessGrid.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 添加列表项被单击的监听器
		chessGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(canDo) {
					fighting(position, (ImageView)view);
				}
			}
		});
	}

	/**
	 *  重新开始游戏
	 */
	public void newGame() {
		timer.stop();
		
		AlertDialog.Builder builder = new Builder(EasyChessAct.this);
		builder.setMessage(R.string.sureToNewGameText);
		builder.setTitle(R.string.sureToNewGameTitle);
		builder.setPositiveButton(R.string.sureToNewGameCertain,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						chessManager = new SimpleComputerBrain();
						// 重绘棋盘
						for (int i = 0; i < Config.CHESS_SIZE; i++) {
							((ImageView) chessGrid.getChildAt(i)).setImageResource(0);
						}
						timer.setBase(SystemClock.elapsedRealtime());
						timer.start();
					}
				});
		builder.setNegativeButton(R.string.sureToNewGameConceil,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						timer.start();
					}
				});
		builder.create().show();
	}
	
	/**
	 * 人机对战
	 * @param position  位置
	 * @param image     棋子显示容器
	 */
	public void fighting(int position, ImageView image) {
		if(chessManager.playerAddChess(position)) {
			image.setImageResource(playerImg);
			checkResult();
			if(noteTimer) {
				timer.setBase(SystemClock.elapsedRealtime());
				timer.start();
				noteTimer = false;
			}
			
			canDo = false;
			currentPlayer.setText(R.string.computer);
			TimerTask task = new TimerTask(){    
		        public void run() {  
		        	Message message = new Message();      
		            message.what = 0123;      
		            handler.sendMessage(message);     
		        }            
		    };
		    new Timer().schedule(task, new Random().nextInt(400)+400);
			  
		}
	}
	
	/**
	 * 检查是否胜利
	 */
	private void checkResult() {
		if(chessManager.getPlayerMax() == Config.GAME_WIN_NUM || chessManager.getCompeterMax() == Config.GAME_WIN_NUM) {
			Intent data = new Intent(EasyChessAct.this, ResultAct.class);
			if(chessManager.getPlayerMax() == Config.GAME_WIN_NUM) {
				data.putExtra("result", R.string.youAreWin);
			}
			else {
				data.putExtra("result", R.string.youAreLose);
			}
			startActivity(data);
			finish();
		}
	}

	/**
	 * 退出游戏
	 */
	private void exitGame() {
		timer.stop();
		AlertDialog.Builder builder = new Builder(EasyChessAct.this);
		builder.setMessage(R.string.exitGameDialogText);
		builder.setTitle(R.string.exitGame);
		builder.setPositiveButton(R.string.exitGameDialogCertain,
				new DialogInterface.OnClickListener() {
					//结束游戏
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(EasyChessAct.this, IndexUIAct.class));
						EasyChessAct.this.finish();
					}
				});
		builder.setNegativeButton(R.string.exitGameDialogConceil,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timer.start();
					}
				});
		builder.create().show();
	}
}
