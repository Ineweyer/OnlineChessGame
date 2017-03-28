package cn.ineweyer.onlinechessgame.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.ineweyer.onlinechessgame.Config;
import cn.ineweyer.onlinechessgame.R;
import cn.ineweyer.onlinechessgame.game.update.ChessManager;
import cn.ineweyer.onlinechessgame.game.update.ChessType;
import cn.ineweyer.onlinechessgame.net.ChessNet;
import cn.ineweyer.onlinechessgame.net.RoomNet;

/**
 * 在线下棋控制类
 * @author LQ
 * 
 */
public class OnlineGameAct extends Activity implements OnClickListener{
	private GridView chessGrid;              //棋盘
	private RelativeLayout linear;           //背景布局，用户更换背景
	private ImageAdapter imageAdapter;       //图片适配器
	private boolean notsetAdapter = true;    //是否需要设置图片适配器，（第一次创建界面时需要设置）
	private ChessManager chessManager;       //下棋逻辑控制对象
	private Chronometer timer;               //计时器
	boolean noteTimer = true;                //通知是否重新计时
	private int competerImg;                 //对手棋子图片资源id
	private int playerImg;                   //玩家棋子图片资源id
	private int chessBg;                     //棋盘背景图片资源id
	private TextView currentPlayer;          //当前下棋者
	private TextView gameStatus;             //当前游戏状态
	
	//关于棋子闪烁
	private ImageView currentChess = null;
	private int chessAniTime = 0;
	
	//关于在线的信息
	private boolean canDo = false, gaming=false;        //刚开始不能下棋
	private String competerName, username, roomOwner;   //用户信息
	private int room_id;                                //当前下棋房间Id
	private TextView competer;                          //对手名字显示
	
	private Handler handler;                            //获取对手已下棋子处理器
	private Handler enterRoomHandler;                   //获取下棋房间处理器
	private Handler imageHandler;                       //用户棋子的闪动效果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);				// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);		// 设置全屏
		setContentView(R.layout.online_game);	
		
		//初始化棋盘
		findViewById(R.id.document).setBackgroundResource(chessBg);
		currentPlayer = (TextView) findViewById(R.id.currentPlayer);
		competer = (TextView) findViewById(R.id.competer);
		gameStatus = (TextView) findViewById(R.id.errorLog);
		chessGrid = (GridView) findViewById(R.id.chessWithComputerBgGrid);
		setAdapter();
		
		
		//初始化棋盘管理者
		chessManager = new ChessManager();
		
		//绑定点击事件
		findViewById(R.id.regretBtn).setOnClickListener(this);
		findViewById(R.id.gameSetting).setOnClickListener(this);
		findViewById(R.id.exitGame).setOnClickListener(this);
		
		//设置显示样式
		competerImg = Config.getCompeterChessColor(OnlineGameAct.this);
		playerImg = Config.getPlayerChessColor(OnlineGameAct.this);
		chessBg = Config.getChessBg(OnlineGameAct.this);
		//设置游戏背景图片
		((ImageView)(findViewById(R.id.computerChessImg))).setImageResource(competerImg);
		((ImageView)(findViewById(R.id.playerChessImg))).setImageResource(playerImg);
		findViewById(R.id.document).setBackgroundResource(chessBg);
		//设置玩家名称
		((TextView)findViewById(R.id.player)).setText(Config.getCachedUserName(OnlineGameAct.this));
		((TextView)findViewById(R.id.competer)).setText(R.string.unConnect);
		gameStatus.setText(R.string.waitCompeter);
		username = Config.getCachedUserName(OnlineGameAct.this);
		
		//初始化计时部分
		timer = (Chronometer)findViewById(R.id.time);
		timer.stop();
		
		//连接队友
		getRoom();
		
		//初始化处理器
		initHandler();
		
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
								imageHandler.sendMessage(message);     
							}            
						}, 50);
					}
					else {
						currentChess.setImageResource(competerImg);
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
			playerImg = Config.getPlayerChessColor(OnlineGameAct.this);
			competerImg = Config.getCompeterChessColor(OnlineGameAct.this);
			chessBg = Config.getChessBg(OnlineGameAct.this);
			
			findViewById(R.id.document).setBackgroundResource(chessBg);
			chessManager.freshChessImg(chessGrid, competerImg, playerImg);
			timer.start();
		}
	}
		
	//重写方法当页面重新启动时刷新界面
	@Override
	protected void onStart() {
		super.onStart();
		
		//设置显示样式
		playerImg = Config.getPlayerChessColor(OnlineGameAct.this);
		competerImg = Config.getCompeterChessColor(OnlineGameAct.this);
		chessBg = Config.getChessBg(OnlineGameAct.this);
		
		//刷新界面
		findViewById(R.id.document).setBackgroundResource(chessBg);
		((ImageView)(findViewById(R.id.computerChessImg))).setImageResource(competerImg);
		((ImageView)(findViewById(R.id.playerChessImg))).setImageResource(playerImg);
		chessManager.freshChessImg(chessGrid, competerImg, playerImg);
		timer.start();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gameSetting:
			//更改游戏
			timer.stop();
			startActivityForResult(new Intent(OnlineGameAct.this, SettingAct.class), 1);
			break;
		case R.id.exitGame:
			//退出游戏
			exitGame();
			break;

		case R.id.regretBtn:
			//悔棋操作
			if(!canDo) {
				Toast.makeText(OnlineGameAct.this, R.string.cannotRegret, Toast.LENGTH_SHORT).show();
				break;
			}
			int stepBack = chessManager.getStep();
			int posi = chessManager.onlineRegret();
			if(posi == -1) {
				Toast.makeText(OnlineGameAct.this, R.string.cannotRegret, Toast.LENGTH_SHORT).show();
				break;
			}
			else {
				//撤销棋子显示
				((ImageView) chessGrid.getChildAt(posi)).setImageResource(0);
				//更新服务端信息
				ChessNet.regret(room_id, stepBack, new ChessNet.SeccuessCallBack() {
					
					@Override
					public void onSeccuess(JSONObject json) {
						canDo = false;
						
						Toast.makeText(OnlineGameAct.this, R.string.regretSeccuess, Toast.LENGTH_SHORT).show();
						//发送获取棋子请求
					    new Timer().schedule(new TimerTask(){    
					        public void run() {  
					        	Message message = new Message();      
					            message.what = 0123;      
					            handler.sendMessage(message);     
					        }            
					    }, Config.NET_DELAY_TIME);
					}
				}, new ChessNet.FailCallBack() {
					
					@Override
					public void onFail(String result) {
						Toast.makeText(OnlineGameAct.this, R.string.regretFail, Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		}
	}
	/**
	 * 设置棋盘显示适配器
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
		
		//当页面元素已经开始绘制后再设置适配器，因为用棋子去平分棋盘
		linear.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						linear.postDelayed(new Runnable() {

							@Override
							public void run() {
								//第一次加載頁面設置adapter
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
		
		// 添加列表项被单击的监听器
		chessGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(canDo) {
					fighting(position, (ImageView)view);
				}
				else if(!gaming){
					Toast.makeText(OnlineGameAct.this, R.string.waitCompeter, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	
	/**
	 * 下棋控制逻辑
	 * @param position  棋子位置
	 * @param image     棋子ImageView
	 */
	public void fighting(int position, ImageView image) {
		if(chessManager.addChess(ChessType.PLAYER, position / Config.GRID_NUM, position % Config.GRID_NUM)) {
			//显示棋子
			image.setImageResource(playerImg);

			//第一次重新计时
			if(noteTimer) {
				timer.setBase(SystemClock.elapsedRealtime());
				timer.start();
				noteTimer = false;
			}
			
			//阻塞当前用户，不能再下棋，等待对手回应
			canDo = false;
			currentPlayer.setText(competerName);
			if(chessManager.hasWin(position / Config.GRID_NUM, position % Config.GRID_NUM)) {
				
				Intent data = new Intent(OnlineGameAct.this, ResultAct.class);
				data.putExtra("result", R.string.youAreWin);
				startActivity(data);
				finish();
			}
			
			//上传数据
			ChessNet.addChess(room_id, chessManager.getStep(), position, username, new ChessNet.SeccuessCallBack() {
				
				@Override
				public void onSeccuess(JSONObject json) {
					
				}
			}, new ChessNet.FailCallBack() {
				
				@Override
				public void onFail(String result) {
					Toast.makeText(OnlineGameAct.this, result, Toast.LENGTH_SHORT).show();
				}
			});
			
			//发送获取棋子请求
		    new Timer().schedule(new TimerTask(){    
		        public void run() {  
		        	Message message = new Message();      
		            message.what = 0123;      
		            handler.sendMessage(message);     
		        }            
		    }, Config.NET_DELAY_TIME);
		}
	}

	/**
	 * 链接对手
	 */
	private void getRoom() {
		
		//网络连接请求
		RoomNet.getRoom(username, new RoomNet.SeccuessCallBack() {
			
			@Override
			public void onSeccuess(JSONObject json) {
				try {
					roomOwner = json.getString(Config.PARA_OWNER);
					//当前房间所有者不是当前请求用户
					if(!username.equals(roomOwner)) {
						competerName = roomOwner;
						room_id = json.getInt(Config.PARA_ROOM_ID);
						gaming = true;
						competer.setText(competerName);
						gameStatus.setText(R.string.onlineGaming);
						
						//发送获取棋子请求
					    new Timer().schedule(new TimerTask(){    
					        public void run() {  
					        	Message message = new Message();      
					            message.what = 0123;      
					            handler.sendMessage(message);     
					        }            
					    }, Config.NET_DELAY_TIME);
					    
					    currentPlayer.setText(competerName);
					}
					//当前用户是房主
					else {
						//进入房间等待
						new Timer().schedule(new TimerTask(){    
							public void run() {  
								Message message = new Message();      
								message.what = 0456;      
								enterRoomHandler.sendMessage(message);     
							}            
						}, Config.NET_DELAY_TIME);
					}
				} catch (JSONException e) {
					Toast.makeText(OnlineGameAct.this, R.string.waitCompeter, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}, new RoomNet.FailCallBack() {
			
			@Override
			public void onFail(String result) {
				Toast.makeText(OnlineGameAct.this, result, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 *  初始化Handler
	 */
	private void initHandler() {
		
		//用于获取对手所辖棋子
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0123) {
					
					//获取棋子
					ChessNet.getChess(room_id, chessManager.getStep() + 1,
							new ChessNet.SeccuessCallBack() {
								@Override
								public void onSeccuess(JSONObject json) {
									try {
										int pos = json.getInt(Config.PARA_LOCATION);
										int roomID = json.getInt(Config.PARA_CHESS_ROOM_ID);
										//等待对手下棋
										if(roomID == -1) {
											// 发送获取棋子请求
											new Timer().schedule(new TimerTask() {
												public void run() {
													Message message = new Message();
													message.what = 0123;
													handler.sendMessage(message);
												}
											}, Config.NET_DELAY_TIME);
											return ;
										}
										//对手已经悔棋
										else if(roomID == -2) {
											int position = chessManager.onlineRegret();
											if(position <= -1) {
												Toast.makeText(OnlineGameAct.this, R.string.regretFail, Toast.LENGTH_SHORT).show();
											}
											else {
												//撤销棋子显示
												((ImageView)chessGrid.getChildAt(position)).setImageResource(0);
												canDo = true;
											}
											return ;
										}
										//对手已经退出
										else if(roomID == -3) {
											//离开房间
											RoomNet.leaveRoom(roomOwner, new RoomNet.SeccuessCallBack() {
												
												@Override
												public void onSeccuess(JSONObject json) {
													
												}
											}, new RoomNet.FailCallBack() {
												
												@Override
												public void onFail(String result) {
													
												}
											});
											Intent data = new Intent(OnlineGameAct.this, ResultAct.class);
											data.putExtra("result", R.string.competerLoginOut);
											startActivity(data);
											finish();
											return ;
										}
										//正常获取到棋子
										else if(pos >= 0){
											if(chessManager.addChess(ChessType.COMPETER, pos / Config.GRID_NUM, pos % Config.GRID_NUM)) {
												
												//发送棋子动画请求
												currentChess = ((ImageView)chessGrid.getChildAt(pos));
												new Timer().schedule(new TimerTask(){    
													public void run() {  
														Message message = new Message();      
														message.what = 0555;      
														imageHandler.sendMessage(message);     
													}            
												}, 50);
												
												//查看是否获胜
												if(chessManager.hasWin(pos / Config.GRID_NUM, pos  % Config.GRID_NUM)) {
													
													//离开房间
													RoomNet.leaveRoom(roomOwner, new RoomNet.SeccuessCallBack() {
														
														@Override
														public void onSeccuess(JSONObject json) {
															
														}
													}, new RoomNet.FailCallBack() {
														
														@Override
														public void onFail(String result) {
															
														}
													});
													
													// 显示下棋结果
													new Timer().schedule(new TimerTask() {
														public void run() {
															Intent data = new Intent(OnlineGameAct.this, ResultAct.class);
															data.putExtra("result", R.string.youAreLose);
															startActivity(data);
															finish();
														}
													}, 1500);
												}
											}
										}
										currentPlayer.setText(username);
									} catch (JSONException e) {
										
										// 发送获取棋子请求
										new Timer().schedule(new TimerTask() {
											public void run() {
												Message message = new Message();
												message.what = 0123;
												handler.sendMessage(message);
											}
										}, Config.NET_DELAY_TIME);
									}
								}
							}, new ChessNet.FailCallBack() {
								@Override
								public void onFail(String result) {
									// 发送获取棋子请求
									new Timer().schedule(new TimerTask() {
										public void run() {
											Message message = new Message();
											message.what = 0123;
											handler.sendMessage(message);
										}
									}, Config.NET_DELAY_TIME);
								}
							});
				}
			};
		};

		// 用户等待对手上线handler
		enterRoomHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0456) {
					RoomNet.checkRoom(username, new RoomNet.SeccuessCallBack() {

						@Override
						public void onSeccuess(JSONObject json) {
							try {
								// 已经有玩家进入
								if (1 == json.getInt(Config.PARA_ROOM_STATUS)) {
									Toast.makeText(OnlineGameAct.this,R.string.competerComeIn, Toast.LENGTH_LONG).show();
									competerName = json.getString(Config.PARA_COMPETER);
									room_id = json.getInt(Config.PARA_ROOM_ID);
									gaming = true;
									canDo = true;
									competer.setText(competerName);
									gameStatus.setText(R.string.onlineGaming);
									currentPlayer.setText(competerName);
								}
								// 没有玩家进入
								else {
									// 发送检查房间
									new Timer().schedule(new TimerTask() {
										public void run() {
											Message message = new Message();
											message.what = 0456;
											enterRoomHandler
													.sendMessage(message);
										}
									}, Config.NET_DELAY_TIME);
								}
							} catch (JSONException e) {
								Toast.makeText(OnlineGameAct.this,R.string.waitCompeter, Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						}
					}, new RoomNet.FailCallBack() {

						@Override
						public void onFail(String result) {

						}
					});
				}
			};
		};
	}
	
	/**
	 * 退出游戏
	 */
	private void exitGame() {
		timer.stop();
		AlertDialog.Builder builder = new Builder(OnlineGameAct.this);
		builder.setMessage(R.string.exitGameDialogText);
		builder.setTitle(R.string.exitGame);
		builder.setPositiveButton(R.string.exitGameDialogCertain,
				new DialogInterface.OnClickListener() {
					//结束游戏
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//离开房间
						RoomNet.leaveRoom(roomOwner, new RoomNet.SeccuessCallBack() {
							
							@Override
							public void onSeccuess(JSONObject json) {
								
							}
						}, new RoomNet.FailCallBack() {
							
							@Override
							public void onFail(String result) {
								
							}
						});
						startActivity(new Intent(OnlineGameAct.this, IndexUIAct.class));
						OnlineGameAct.this.finish();
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