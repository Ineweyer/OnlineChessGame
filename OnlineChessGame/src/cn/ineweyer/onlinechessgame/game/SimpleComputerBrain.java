package cn.ineweyer.onlinechessgame.game;

import java.util.ArrayList;
import java.util.Random;

import android.widget.GridView;
import android.widget.ImageView;
import cn.ineweyer.onlinechessgame.Config;

/**
 * 继承自棋盘管理，实现简单的人机对战
 * @author LQ
 *
 */
public class SimpleComputerBrain extends ChessManager{
	
	private ArrayList<Integer> playerOrderList = new ArrayList<Integer>();
	private ArrayList<Integer> computerOrderList = new ArrayList<Integer>();
	
	/**
	 * 刷新用户棋子背景图片
	 * @param gridView  棋盘网格布局
	 * @param chessImg  用户棋子图片资源ID
	 */
	public void freshPlayChess(GridView gridView, int chessImg) {
		for(int i = 0; i< playerOrderList.size() ; i++) {
			((ImageView) gridView.getChildAt(playerOrderList.get(i))).setImageResource(chessImg);
		}
	}
	
	/**
	 * 刷新对手棋子背景图片
	 * @param gridView 棋盘网格布局
	 * @param chessImg 对手棋子图片资源ID
	 */
	public void freshComputerChess(GridView gridView, int chessImg) {
		for(int i = 0; i< computerOrderList.size() ; i++) {
			((ImageView) gridView.getChildAt(computerOrderList.get(i))).setImageResource(chessImg);
		}
	}
	
	/**
	 * 自动下棋
	 * @return 棋子位置
	 */
	public int autoPlay() {
		int position = -1;
		boolean notReady = true;		
		//玩家再下一子就赢了
    	if(super.playerMax == (Config.GAME_WIN_NUM - 1)) {
    		position = aroundOut(super.playerMax, true);
    		if(position >= 0) {
    			notReady = false;
    		}
    	}
    	
    	//玩家再下一子也赢了
    	if(notReady && (super.playerMax == (Config.GAME_WIN_NUM - 2))) {
    		position = aroundOut(super.playerMax, false);
    		if(position >= 0) {
    			notReady = false;
    		}
    	}
    	
    	//玩家占优势，防守，同时对上面操作中的非法位置进行过滤
    	if((chessMap.get(position) != null) || notReady) {
    		do {
    			position = aroundOut(super.playerMax, true);
    			if(position < 0) {
    				//找不到可下棋位置，说明最多棋子方向上已经被堵死，最多棋子数减1
    				super.playerMax--;
    			}
    		}
    		while(position < 0 || chessMap.get(position) != null);
    	}
    	// 添加棋子
		super.addChessAtPosition(position, Config.COMPETER);
		//将当前的棋子假如到机器棋子队列中
		computerOrderList.add(position);
		return position;
	}
	
	/**
	 * 用户下棋
	 * @param position  下棋位置
	 * @return  当前位置能否下棋
	 */
	public boolean playerAddChess(int position) {
		if(addChessAtPosition(position, Config.PLAYER)) {
			playerOrderList.add(position);
			return true;
		}
		return false;
	}
	
	/**
	 * 用户悔棋
	 * @return  悔掉的棋子数组
	 */
	public int[] playerRegret() {
		if(playerOrderList.size() <= 0) {
			return new int[0];
		}
		int[] position = new int[2];
		position[0] = reBackChessStaus(Config.COMPETER);
		position[1] = reBackChessStaus(Config.PLAYER);
		playerOrderList.remove(playerOrderList.size() - 1);
		return position;
	}

	/**
	 * 找到玩家的棋子,在玩家棋子的旁边下棋
	 * @param value  查找条件，查找连线棋子数大于此值的位置
	 * @param mod    单方向还是两个方向
	 * @return 棋子位置
	 */
	private int aroundOut(int value, boolean mod) {
		Random random = new Random();
		int dir = random.nextInt(2);
		int computerOrderSize = playerOrderList.size();
		int firstStart = random.nextInt(computerOrderSize);
		int position = -1;
		Chess chess = null;

		if (dir == 0) {
			for (int i = firstStart; i >= 0; i--) {
				position = aroundMiddle(chess, playerOrderList.get(i), random,
						value, mod);
				if (position >= 0) {
					return position;
				}
			}
			for (int i = firstStart + 1; i < computerOrderSize; i++) {
				position = aroundMiddle(chess, playerOrderList.get(i), random,
						value, mod);
				if (position >= 0) {
					return position;
				}
			}
		} else {
			for (int i = firstStart + 1; i < computerOrderSize; i++) {
				position = aroundMiddle(chess, playerOrderList.get(i), random,
						value, mod);
				if (position >= 0) {
					return position;
				}
			}
			for (int i = firstStart; i >= 0; i--) {
				position = aroundMiddle(chess, playerOrderList.get(i), random,
						value, mod);
				if (position >= 0) {
					return position;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @param chess   棋子
	 * @param index   棋子位置
	 * @param random  随机对象
	 * @param value   查找条件，查找连线棋子数大于此值的位置
	 * @param mod     单方向还是两个方向
	 * @return 可下棋位置
	 */
   private int aroundMiddle(Chess chess,int index, Random random, int value, boolean mod) {
	   int dirMiddle = random.nextInt(2);
	   Chess chessAmpt;
	   int position, positionAmpt;
	   Location loc;
	   Location reverseLoc;
	   boolean modFlow = false;
	   
	   if(((chess = chessMap.get(index)) != null) && (chess.getMax() >= value)) {
		   loc = chess.getLocation();
		   reverseLoc = getReveseLocation(loc);
		   
			if(dirMiddle == 0) {
				chessAmpt = chessMap.get(getLineEnd(loc, chess.getLocationSize(loc), chess.getPosition()));
				if (chessAmpt != null) {
					position = aroundIn(chessAmpt, random, getAroundList(chessAmpt.getPosition()), loc);
					//当前位置可下棋
					if ((position >= 0)) {
						//无预判模式
						if(mod) {
							return position;
						}
						//带预判模式标识第一次检测通过
						else {
							modFlow = true;
						}
					}
				}
		
				//同侧方向无位置
				chessAmpt = chessMap.get(getLineEnd(reverseLoc, chess.getLocationSize(reverseLoc), chess.getPosition()));
				if(chessAmpt != null) {
					positionAmpt = aroundIn(chessAmpt, random, getAroundList(chessAmpt.getPosition()), reverseLoc);
					if(positionAmpt >= 0) {
						//无预判模式
						if(mod) {
							return positionAmpt;
						}
						//带预判模式且第一次检测通过
						else if(modFlow) {
							return positionAmpt;
						}
					}
				}
			}
			else {
				//同侧方向无位置
				chessAmpt = chessMap.get(getLineEnd(reverseLoc, chess.getLocationSize(reverseLoc), chess.getPosition()));
				if(chessAmpt != null) {
					positionAmpt = aroundIn(chessAmpt, random, getAroundList(chessAmpt.getPosition()), reverseLoc);
					if(positionAmpt >= 0) {
						//无预判模式
						if(mod) {
							return positionAmpt;
						}
						//带预判模式标识第一次检测通过
						else {
							modFlow = true;
						}
					}
				}
				
				chessAmpt = chessMap.get(getLineEnd(loc, chess.getLocationSize(loc), chess.getPosition()));
				if (chessAmpt != null) {
					position = aroundIn(chessAmpt, random, getAroundList(chessAmpt.getPosition()), loc);
					//当前位置可下棋
					if ((position >= 0)) {
						//无预判模式
						if(mod) {
							return position;
						}
						//带预判模式且第一次检测通过
						else if(modFlow) {
							return position;
						}
					}
				}
			}
   		}
	   return -1;
   }
	
	/**
	 * 遍历当前用户的棋子周围的空位置
	 * @param chess             棋子
	 * @param random            随机变量对象
	 * @param aroundChessList   周边可下棋子位置集
	 * @param location          方向
	 * @return  棋子位置
	 */
	private int aroundIn(Chess chess, Random random,ArrayList<AtLocationChess> aroundChessList, Location location) { 
		int position  = -1;
		int dir = random.nextInt(2);
		int aroundChessSize = aroundChessList.size();
		int secondStart = random.nextInt(aroundChessSize);
		
		if(dir == 0) {
			for(int i = secondStart; i >= 0; i--) {
				position = aroundChessList.get(i).getPosition();
				//获取空位置
				if(((chess = chessMap.get(position)) == null) && sameLocation(location, aroundChessList.get(i).getLocation())) {
					return position;
				}
			}
			for(int i = secondStart; i < aroundChessSize ;i++) {
				position = aroundChessList.get(i).getPosition();
				//获取空位置
				if(((chess = chessMap.get(position)) == null) && sameLocation(location, aroundChessList.get(i).getLocation())) {
					return position;
				}
			}
		}
		else {
			for(int i = secondStart; i < aroundChessSize ;i++) {
				position = aroundChessList.get(i).getPosition();
				//获取空位置
				if(((chess = chessMap.get(position)) == null) && sameLocation(location, aroundChessList.get(i).getLocation())) {
					return position;
				}
			}
			for(int i=secondStart; i >= 0; i--) {
				position = aroundChessList.get(i).getPosition();
				//获取空位置
				if(((chess = chessMap.get(position)) == null) && sameLocation(location, aroundChessList.get(i).getLocation())) {
					return position;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 判断方向是否相同
	 * @param loc1  方向1
	 * @param loc2 方向2
	 * @return  俩方向是否相同
	 */
	public boolean sameLocation(Location loc1, Location loc2) {
		boolean equal = false;
		switch(loc1) {
		case NORTH :
		case SOUTH :
			if(loc2 == Location.NORTH || loc2 == Location.SOUTH) {
				equal = true;
			}
			break;
		case EAST_NORTH :
		case WEST_SOUTH :
			if(loc2 == Location.EAST_NORTH || loc2 == Location.WEST_SOUTH) {
				equal = true;
			}
			break;
		case EAST :
		case WEST :
			if(loc2 == Location.EAST || loc2 == Location.WEST) {
				equal = true;
			}
			break;
		case EAST_SOUTH :
		case WEST_NORTH :
			if(loc2 == Location.WEST_NORTH || loc2 == Location.EAST_SOUTH) {
				equal = true;
			}
			break;
		}
		return equal;
	}
}