package cn.ineweyer.onlinechessgame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import cn.ineweyer.onlinechessgame.Config;

/**
 * 用户管理当前的棋盘信息
 * 
 * @author LQ
 * 
 */
public class ChessManager {
	protected Map<Integer, Chess> chessMap; // 棋子状态
	protected List<Integer> freeChessList; // 空闲棋子处
	private int win; // 胜利标志
	protected int playerMax; // 用户最多棋子数
	protected int competerMax; // 对手最多棋子数
	private int backIndex; // 悔棋索引

	/**
	 *  带参数的构造函数，用户游戏恢复
	 * @param chessMap         棋盘
	 * @param freeChessList    空闲棋子队列
	 * @param playerMax        玩家当前最大棋子数
	 * @param competerMax      对手当前最大棋子数
	 */
	public ChessManager(Map<Integer, Chess> chessMap,
			List<Integer> freeChessList, int playerMax, int competerMax) {
		super();
		this.chessMap = chessMap;
		this.freeChessList = freeChessList;
		this.win = Config.GAME_RESULT_GAMING;
		this.playerMax = playerMax;
		this.competerMax = competerMax;
		this.backIndex = 0;
	}

	/**
	 *  不到参数的构造函数，用户开始新的游戏
	 */
	@SuppressLint("UseSparseArrays")
	public ChessManager() {
		this.chessMap = new HashMap<Integer, Chess>();
		this.freeChessList = new ArrayList<Integer>();
		for (int i = 1; i <= Config.CHESS_SIZE; i++) {
			freeChessList.add(i);
		}
		this.win = Config.GAME_RESULT_GAMING;
		this.playerMax = 0;
		this.competerMax = 0;
		this.backIndex = 0;
	}

	/**
	 *  添 加一个棋子 
	 * @param position   棋子位置
	 * @param isMine     棋子所有者
	 * @return           棋子是否添加成功
	 */
	public boolean addChessAtPosition(int position, boolean isMine) {
		// 当前位置没有棋子
		if (freeChessList.get(position) >= 0) {
			// 标识当前位置被占据，其中backIndex绝对值越大表示当前棋子下的时间越新，用户悔棋
			freeChessList.set(position, --backIndex);
			// 插入棋子
			Chess chess = new Chess(isMine, position);
			// 保存棋子
			chessMap.put(position, chess);
			// 刷新周围棋子的状态,当真时，游戏结束
			refreshChessStaus(chess, position, isMine);
			return true;
		}
		return false;
	}

	/**
	 *  刷新周围棋子状态
	 * @param chess       当前棋子
	 * @param position    棋子位置
	 * @param isMine      棋子所有标识
	 */
	private void refreshChessStaus(Chess chess, int position, boolean isMine) {
		ArrayList<AtLocationChess> aroundChessList = getAroundList(position); // 棋子周围可下棋位置集合
		AtLocationChess atLocationChess; // 中间变量，用于存放棋子周围可下棋位置
		Location atLocation, atReverseLocation; // 同方向和反反方向
		Chess chessAmpt, chessAmptEnd; // 中间变量，用户临时存放周围棋子
		int chessSizeAmpt, reverseChessSizeAmpt; // 中间变量，当前下棋节点与临时周围节点的连线棋子数
		
		// 逐个处理周围位置
		for (int i = 0; i < aroundChessList.size(); i++) {
			atLocationChess = aroundChessList.get(i);
			chessAmpt = chessMap.get(atLocationChess.getPosition());
			// 当前位置有棋子
			if (chessAmpt != null) {
				if (chessAmpt.isMineChess() == isMine) {
					// 刚下的棋子为参照者
					atLocation = atLocationChess.getLocation();
					// 周围的棋子为参考者
					atReverseLocation = getReveseLocation(atLocation);

					// 能和周围棋子组成一条直线方向的刚下棋子的同方向棋子数
					chessSizeAmpt = chessAmpt.getLocationSize(atLocation);
					reverseChessSizeAmpt = chess.getLocationSize(atReverseLocation);

					// 更新周围棋子的在刚下棋子影响的棋子数
					chessAmpt.freshLocation(atReverseLocation, reverseChessSizeAmpt + 1);
					// 刷新一条线上最大棋子数
					freshMax(chessAmpt.freshMax(), isMine);
					// 更新刚下棋子的被周围棋子影响的棋子数
					chess.freshLocation(atLocation, chessSizeAmpt + 1);

					// 更新直线两端的棋子值
					if (chessSizeAmpt != 0) {
						// 同方向端点
						chessAmptEnd = chessMap.get(getLineEnd(atLocation, chessAmpt.getLocationSize(atLocation), chessAmpt.getPosition()));
						if ((chessAmptEnd != null) && (chessAmptEnd.isMineChess() == isMine)) {
							chessAmptEnd.freshLocation(atReverseLocation, chessAmpt.getLocationSize(atReverseLocation));
							// 刷新一条直线上最大棋子数
							freshMax(chessAmptEnd.freshMax(), isMine);
						}
					}
					if (reverseChessSizeAmpt != 0) {
						// 反方向端点
						chessAmptEnd = chessMap.get(getLineEnd( atReverseLocation, chess.getLocationSize(atReverseLocation), chess.getPosition()));
						if ((chessAmptEnd != null) && (chessAmptEnd.isMineChess() == isMine)) {
							chessAmptEnd.freshLocation(atLocation, chess.getLocationSize(atLocation));
							// 刷新一条直线上最大棋子数
							freshMax(chessAmptEnd.freshMax(), isMine);
						}
					}
				}
			}
		}
		// 刷新一条线上的最大棋子数
		freshMax(chess.freshMax(), isMine);
	}

	/**
	 *  悔棋
	 * @param isMine   棋子所有者标识
	 * @return         悔掉的棋子
	 */
	public int reBackChessStaus(boolean isMine) {
		ArrayList<AtLocationChess> aroundChessList; // 棋子周围可下棋位置集合
		AtLocationChess atLocationChess; // 中间变量，用于存放棋子周围可下棋位置
		Location atLocation, atReverseLocation; // 同方向和反反方向
		Chess chessAmpt, chessAmptEnd; // 中间变量，用户临时存放周围棋子
        int max = 0;                       //保存当前的用户的最大棋子数
		int recentChess = getRecentChess(); // 获取刚下的棋子的位置
		aroundChessList = getAroundList(recentChess); // 获取刚下棋子周边棋子

		//位置合法
		if (recentChess >= 0) {
			// 逐个处理周围位置
			for (int i = 0; i < aroundChessList.size(); i++) {
				atLocationChess = aroundChessList.get(i);
				chessAmpt = chessMap.get(atLocationChess.getPosition());
				// 当前位置有棋子
				if (chessAmpt != null) {
					if (chessAmpt.isMineChess() == isMine) {
						// 刚下的棋子为参照者
						atLocation = atLocationChess.getLocation();
						// 周围的棋子为参考者
						atReverseLocation = getReveseLocation(atLocation);

						// 还与其他的棋子组成一条直线
						if (chessAmpt.getLocationSize(atLocation) != 0) {
							// 同方向端点
							chessAmptEnd = chessMap.get(getLineEnd(atLocation,
									chessAmpt.getLocationSize(atLocation),
									chessAmpt.getPosition()));
							if (chessAmptEnd != null
									&& (chessAmptEnd.isMineChess() == isMine)) {
								chessAmptEnd.freshLocation(atReverseLocation, -chessAmpt.getLocationSize(atReverseLocation));
								chessAmptEnd.fixMax(atReverseLocation);
							}
						}
						// 更新周围棋子的在刚下棋子影响的棋子数
						chessAmpt.freshLocation(atReverseLocation, -(chessAmpt.getLocationSize(atReverseLocation)));
						chessAmpt.fixMax(atReverseLocation);
					}
				}
			}
			//保存当前的最大一条直线上的棋子
			max = chessMap.get(recentChess).getMax();
			// 从已经下的棋子中删除该棋子
			chessMap.remove(recentChess);
			//如过删去的棋子在最大的
			if(max == playerMax || max == competerMax) {
				fixMax(isMine);
			}
		}
		return recentChess;
	}
	
	/**
	 * 悔棋时更新最大棋子数
	 * @param isMine   棋子所有者标识
	 */
	protected void fixMax(boolean isMine) {
		int max = 0;
		Chess chess;
		for(int i = 0; i<freeChessList.size(); i++) {
			if(freeChessList.get(i) < 0) {
				if(((chess = chessMap.get(i))!= null) && (chess.isMineChess() == isMine)) {
					if(chess.getMax() > max)  {
						max = chess.getMax();
					}
				}
			}
		}
		if(isMine == Config.COMPETER) {
			competerMax = max;
		}
		else {
			playerMax = max;
		}
	}

	/**
	 *  找到上一步下的棋子
	 * @return  上一步棋子位置
	 */
	private int getRecentChess() {
		if (backIndex >= 0) {
			return -1;
		}
		for (int i = 0; i < freeChessList.size(); i++) {
			if (freeChessList.get(i) == backIndex) {
				freeChessList.set(i, i);
				backIndex++;
				return i;
			}
		}
		return -1;
	}

	/**
	 *  刷新一条线上最多棋子状态
	 * @param size     距方向上端点的偏移
	 * @param isMine   棋子所有者标识
	 * @return  刷新是否成功
	 */
	public boolean freshMax(int size, boolean isMine) {
		// 更新当前用户的最多棋子数
		if (isMine) {
			playerMax = size > playerMax ? size : playerMax;
			if (playerMax == Config.GAME_WIN_NUM) {
				this.win = Config.GAME_RESULT_WIN;
				return true;
			}
		}
		// 更新对手的最大棋子数
		else {
			competerMax = size > competerMax ? size : competerMax;
			if (competerMax == Config.GAME_WIN_NUM) {
				this.win = Config.GAME_RESULT_LOSE;
				return true;
			}
		}
		return false;
	}

	/**
	 *  返回直线location方向的端点
	 * @param location    方向
	 * @param size        距方向上端点的偏移
	 * @param position    棋子位置
	 * @return  棋子位置
	 */
	public int getLineEnd(Location location, int size, int position) {
		int i;
		switch (location) {
		case NORTH:
			position = position - (size * Config.GRID_NUM);
			break;
		case EAST_NORTH:
			for (i = 0; i < size; i++) {
				position = position - Config.GRID_NUM + 1;
			}
			break;
		case EAST:
			position = position + size;
			break;
		case EAST_SOUTH:
			for (i = 0; i < size; i++) {
				position = position + Config.GRID_NUM + 1;
			}
			break;
		case SOUTH:
			position = position + (size * Config.GRID_NUM);
			break;
		case WEST_SOUTH:
			for (i = 0; i < size; i++) {
				position = position + Config.GRID_NUM - 1;
			}
			break;
		case WEST:
			position = position - size;
			break;
		case WEST_NORTH:
			for (i = 0; i < size; i++) {
				position = position - Config.GRID_NUM - 1;
			}
			break;
		}
		return position;
	}

	/**
	 *  方向取反
	 * @param location  方向
	 * @return  反方向
	 */
	public Location getReveseLocation(Location location) {
		Location loc = null;
		switch (location) {
		case NORTH:
			loc = Location.SOUTH;
			break;
		case EAST_NORTH:
			loc = Location.WEST_SOUTH;
			break;
		case EAST:
			loc = Location.WEST;
			break;
		case EAST_SOUTH:
			loc = Location.WEST_NORTH;
			break;
		case SOUTH:
			loc = Location.NORTH;
			break;
		case WEST_SOUTH:
			loc = Location.EAST_NORTH;
			break;
		case WEST:
			loc = Location.EAST;
			break;
		case WEST_NORTH:
			loc = Location.EAST_SOUTH;
			break;
		}
		return loc;
	}

	/**
	 *  获取当前位置的可放棋子位置
	 * @param position  当前棋子位置
	 * @return  当前棋子旁边可放棋子位置
	 */
	protected ArrayList<AtLocationChess> getAroundList(int position) {
		ArrayList<AtLocationChess> list = new ArrayList<AtLocationChess>();
		if (position <= (Config.GRID_NUM)) {
			// 第二行第一个元素，只有north, eastNorth, east, eastSouth, south
			if (position == Config.GRID_NUM) {
				list.add(new AtLocationChess(Location.NORTH, position
						- Config.GRID_NUM)); // north
				list.add(new AtLocationChess(Location.EAST_NORTH, position
						- Config.GRID_NUM + 1)); // eastNorth
				list.add(new AtLocationChess(Location.EAST, position + 1)); // east
				list.add(new AtLocationChess(Location.EAST_SOUTH, position
						+ Config.GRID_NUM + 1)); // eastSouth
				list.add(new AtLocationChess(Location.SOUTH, position
						+ Config.GRID_NUM)); // south
			}
			// 第一行第一个元素，只有east，eastSouth，south
			else if (position == 0) {
				list.add(new AtLocationChess(Location.EAST, position + 1)); // east
				list.add(new AtLocationChess(Location.EAST_SOUTH, position
						+ Config.GRID_NUM + 1)); // eastSouth
				list.add(new AtLocationChess(Location.SOUTH, position
						+ Config.GRID_NUM)); // south
			}
			// 第一行最后一个元素,只有south，westSouth，west
			else if (position == (Config.GRID_NUM - 1)) {
				list.add(new AtLocationChess(Location.SOUTH, position
						+ Config.GRID_NUM)); // south
				list.add(new AtLocationChess(Location.WEST_SOUTH, position
						+ Config.GRID_NUM - 1)); // westSouth
				list.add(new AtLocationChess(Location.WEST, position - 1)); // west
			}
			// 第一行中间元素,只有east，eastSouth，south，westSouth，west
			else {
				list.add(new AtLocationChess(Location.EAST, position + 1)); // east
				list.add(new AtLocationChess(Location.EAST_SOUTH, position
						+ Config.GRID_NUM + 1)); // eastSouth
				list.add(new AtLocationChess(Location.SOUTH, position
						+ Config.GRID_NUM)); // south
				list.add(new AtLocationChess(Location.WEST_SOUTH, position
						+ Config.GRID_NUM - 1)); // westSouth
				list.add(new AtLocationChess(Location.WEST, position - 1)); // west
			}
		} else if (position >= (Config.CHESS_SIZE - Config.GRID_NUM - 1)) {
			// 倒数第二行最后一个元素
			if (position == (Config.CHESS_SIZE - Config.GRID_NUM - 1)) {
				list.add(new AtLocationChess(Location.SOUTH, position
						+ Config.GRID_NUM)); // south
				list.add(new AtLocationChess(Location.WEST_SOUTH, position
						+ Config.GRID_NUM - 1)); // westSouth
				list.add(new AtLocationChess(Location.WEST, position - 1)); // west
				list.add(new AtLocationChess(Location.WEST_NORTH, position
						- Config.GRID_NUM - 1)); // westNorth
				list.add(new AtLocationChess(Location.NORTH, position
						- Config.GRID_NUM)); // north
			}
			// 倒数第一行第一个元素
			else if (position == (Config.CHESS_SIZE - Config.GRID_NUM)) {
				list.add(new AtLocationChess(Location.NORTH, position
						- Config.GRID_NUM)); // north
				list.add(new AtLocationChess(Location.EAST_NORTH, position
						- Config.GRID_NUM + 1)); // eastNorth
				list.add(new AtLocationChess(Location.EAST, position + 1)); // east
			}
			// 最后一个元素
			else if (position == (Config.CHESS_SIZE - 1)) {
				list.add(new AtLocationChess(Location.WEST, position - 1)); // west
				list.add(new AtLocationChess(Location.WEST_NORTH, position
						- Config.GRID_NUM - 1)); // westNorth
				list.add(new AtLocationChess(Location.NORTH, position
						- Config.GRID_NUM)); // north
			}
			// 倒数第一行中间元素
			else {
				list.add(new AtLocationChess(Location.NORTH, position
						- Config.GRID_NUM)); // north
				list.add(new AtLocationChess(Location.EAST_NORTH, position
						- Config.GRID_NUM + 1)); // eastNorth
				list.add(new AtLocationChess(Location.EAST, position + 1)); // east
				list.add(new AtLocationChess(Location.WEST, position - 1)); // west
				list.add(new AtLocationChess(Location.WEST_NORTH, position
						- Config.GRID_NUM - 1)); // westNorth
			}
		}
		// 左侧第一列
		else if ((position % Config.GRID_NUM) == 0) {
			list.add(new AtLocationChess(Location.NORTH, position
					- Config.GRID_NUM)); // north
			list.add(new AtLocationChess(Location.EAST_NORTH, position
					- Config.GRID_NUM + 1)); // eastNorth
			list.add(new AtLocationChess(Location.EAST, position + 1)); // east
			list.add(new AtLocationChess(Location.EAST_SOUTH, position
					+ Config.GRID_NUM + 1)); // eastSouth
			list.add(new AtLocationChess(Location.SOUTH, position
					+ Config.GRID_NUM)); // south
		}
		// 右侧第一列
		else if ((((position + 1) % Config.GRID_NUM) == 0)) {
			list.add(new AtLocationChess(Location.SOUTH, position
					+ Config.GRID_NUM)); // south
			list.add(new AtLocationChess(Location.WEST_SOUTH, position
					+ Config.GRID_NUM - 1)); // westSouth
			list.add(new AtLocationChess(Location.WEST, position - 1)); // west
			list.add(new AtLocationChess(Location.WEST_NORTH, position
					- Config.GRID_NUM - 1)); // westNorth
			list.add(new AtLocationChess(Location.NORTH, position
					- Config.GRID_NUM)); // north
		}

		// 中间区域，每个位置有八个相连的点，分别是north，eastNorth，east，eastSouth，south，westSouth，west，westNorth
		else {
			list.add(new AtLocationChess(Location.NORTH, position
					- Config.GRID_NUM)); // north
			list.add(new AtLocationChess(Location.EAST_NORTH, position
					- Config.GRID_NUM + 1)); // eastNorth
			list.add(new AtLocationChess(Location.EAST, position + 1)); // east
			list.add(new AtLocationChess(Location.EAST_SOUTH, position
					+ Config.GRID_NUM + 1)); // eastSouth
			list.add(new AtLocationChess(Location.SOUTH, position
					+ Config.GRID_NUM)); // south
			list.add(new AtLocationChess(Location.WEST_SOUTH, position
					+ Config.GRID_NUM - 1)); // westSouth
			list.add(new AtLocationChess(Location.WEST, position - 1)); // west
			list.add(new AtLocationChess(Location.WEST_NORTH, position
					- Config.GRID_NUM - 1)); // westNorth
		}

		return list;
	}

	/**
	 * @return the win
	 */
	public int getWin() {
		return win;
	}

	/**
	 * @return the playerMax
	 */
	public int getPlayerMax() {
		return playerMax;
	}

	/**
	 * @return the competerMax
	 */
	public int getCompeterMax() {
		return competerMax;
	}

	/**
	 * @return the backIndex
	 */
	public int getBackIndex() {
		return backIndex;
	}

}
