package cn.ineweyer.onlinechessgame.game.update;

import java.util.ArrayList;

import android.widget.GridView;
import android.widget.ImageView;

import cn.ineweyer.onlinechessgame.Config;

/**
 * 棋盘管理类
 * @author LQ
 *
 */
public class ChessManager {
	
	//棋盘Map
	protected ChessType[][] chessMap;
	//电脑棋子Map
	protected int[][] competerMap = new int[Config.GRID_NUM][Config.GRID_NUM];
	//用户棋子Map
	protected int[][] playerMap = new int[Config.GRID_NUM][Config.GRID_NUM];
	//下棋顺序数组
	protected ArrayList<TypeAndLocation> orderList = new ArrayList<TypeAndLocation>();

	// 对手标识
	protected ChessType competerType = ChessType.COMPETER;
	// 玩家标识
	protected ChessType playerType = ChessType.PLAYER;
	//四个方向上最大连子的状态
	protected ChessStatus[] chessStatus = new ChessStatus[4];
	
	/**
	 * 棋盘管理类构造函数
	 */
	public ChessManager() {
		this.chessMap = new ChessType[Config.GRID_NUM][Config.GRID_NUM];
		for(int i = 0 ; i < Config.GRID_NUM ; i++) {
			for(int j = 0 ; j < Config.GRID_NUM ; j++) {
				chessMap[i][j] = ChessType.NONE;
			}
		}
	}

	/**
	 * 添加一颗棋子
	 * @param chessType   棋子类型，换言之棋子所有者
	 * @param r           棋盘上行值
	 * @param c           棋盘上列值
	 * @return            添加棋子是否成功
	 */
	public boolean addChess(ChessType chessType, int r, int c) {
		if(chessMap[r][c] == ChessType.NONE) {
			chessMap[r][c] = chessType;
			orderList.add(new TypeAndLocation(r * Config.GRID_NUM + c, chessType));
			return true;
		}
		return false;
	}
	
	/**
	 * 	根据游戏设置里面的修改刷新所有的棋子的显示样式
	 * @param chessGrid     棋盘网格视图
	 * @param competerImg   对手棋子图片资源ID
	 * @param playerImg     用户棋子图片资源ID
	 */
	public void freshChessImg(GridView chessGrid, int competerImg, int playerImg) {
		for(int i=0 ; i<orderList.size() ; i++) {
			if(orderList.get(i).getChessType() == ChessType.PLAYER) {
				((ImageView)chessGrid.getChildAt(orderList.get(i).getLocation())).setImageResource(playerImg);
			}
			else {
				((ImageView)chessGrid.getChildAt(orderList.get(i).getLocation())).setImageResource(competerImg);
			}
		}
	}
	
	/**
	 * 用户悔棋
	 * @return 悔掉的棋子位置数组，为两项，让自己重下故悔掉一个对手棋子，一个自己棋子
	 */
	public int[] regret() {
		if(orderList.size() < 2) {
			return null;
		}
		
		int[] location = new int[2];
		for(int i=0; i < 2 ; i++) {
			location[i] = orderList.get(orderList.size() - 1).getLocation();
			orderList.remove(orderList.size() - 1);
			chessMap[location[i] / Config.GRID_NUM][location[i] % Config.GRID_NUM] = ChessType.NONE;
		}
		return location;
	}
	
	/**
	 * 在线悔棋
	 * @return 悔掉的棋子位置，仅为一项，即让对方重下
	 */
	public int onlineRegret() {
		if(orderList.size() <= 1) {
			return -1;
		}
		int location = orderList.get(orderList.size() - 1).getLocation();
		orderList.remove(orderList.size() - 1);
		chessMap[location / Config.GRID_NUM][location % Config.GRID_NUM] = ChessType.NONE;
		return location;
	}
	
	/**
	 * 返回当前的刚下棋子位置
	 * @return 刚下棋子位置
	 */
	public int getStep() {
		return orderList.size() - 1;
	}
 	
	/**
	 * 判断是否胜利
	 * @param r  行号
	 * @param c  列号
	 * @return 是否胜利
	 */
	public boolean hasWin(int r, int c) {
		ChessType chessType = chessMap[r][c];

		int count = 1;
		// 纵向搜索
		for (int i = r + 1; i < r + 5; i++) {
			if (i >= Config.GRID_NUM)
				break;
			if (chessMap[i][c] == chessType) {
				count++;
			} else
				break;
		}
		for (int i = r - 1; i > r - 5; i--) {
			if (i < 0)
				break;
			if (chessMap[i][c] == chessType)
				count++;
			else
				break;
		}
		
		if (count >= 5)
			return true;
		
		// 横向搜索
		count = 1;
		for (int i = c + 1; i < c + 5; i++) {
			if (i >= Config.GRID_NUM)
				break;
			if (chessMap[r][i] == chessType)
				count++;
			else
				break;
		}
		for (int i = c - 1; i > c - 5; i--) {
			if (i < 0)
				break;
			if (chessMap[r][i] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"2");
		if (count >= 5)
			return true;
		
		// 斜向"\"
		count = 1;
		for (int i = r + 1, j = c + 1; i < r + 5; i++, j++) {
			if (i >= Config.GRID_NUM || j >= Config.GRID_NUM) {
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
			}
			else {
				break;
			}
		}
		for (int i = r - 1, j = c - 1; i > r - 5; i--, j--) {
			if (i < 0 || j < 0) {
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
			}
			else {
				break;
			}
		}
		if (count >= 5)
			return true;
		
		// 斜向"/"
		count = 1;
		for (int i = r + 1, j = c - 1; i < r + 5; i++, j--) {
			if (i >= Config.GRID_NUM || j < 0) {
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
			}
			else
				break;
		}
		for (int i = r - 1, j = c + 1; i > r - 5; i--, j++) {
			if (i < 0 || j >= Config.GRID_NUM) {
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
			}
			else {
				break;
			}
		}
		//胜利
		if (count >= 5) {
			return true;
		}
		
		return false;
	}

}
