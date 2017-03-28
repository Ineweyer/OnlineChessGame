package cn.ineweyer.onlinechessgame.game.update;

import java.util.Random;

import android.graphics.Point;
import cn.ineweyer.onlinechessgame.Config;

/**
 * 智能下棋
 * @author LQ
 *
 */
public class AiChess extends ChessManager{

	/**
	 * 智能下棋类构造函数
	 */
	public AiChess() {
		super();
	}

	/**
	 * 电脑自动下棋
	 * @return 电脑下棋的位置
	 */
	public int computerAddChess() {
		Point p = getAiPoint();
		if(addChess(ChessType.COMPETER, p.x, p.y)) {
			return p.x * Config.GRID_NUM + p.y;
		}
		else {
			return -1;
		}
	}
	
	/**
	 * 玩家下棋
	 * @param location  棋子位置
	 * @return          棋子是否下成功
	 */
	public boolean playerAddChess(int location) {
		return addChess(ChessType.PLAYER, location / Config.GRID_NUM , location % Config.GRID_NUM);
	}
	
	/**
	 * 初始化棋盘
	 */
	private void initValue() {
		for (int r = 0; r < Config.GRID_NUM; r++) {
			for (int c = 0; c < Config.GRID_NUM; c++) {
				competerMap[r][c] = 0;
				playerMap[r][c] = 0;
			}
		}
		for (int i = 0; i < chessStatus.length; i++) {
			chessStatus[i] = ChessStatus.DIED;
		}
	}
	
	/**
	 * 获取电脑下一步的位置
	 * @return 下一步电脑应该下的坐标
	 */
	private Point getAiPoint() {
		//定义中间变量
		int computerMax = 0, playerMax = 0;
		Random rd = new Random();
		Point computerPoint = new Point(-1, -1);
		Point playerPoint = new Point();
		
		initValue();
		
		//获取到评价分数
		for (int r = 0; r < Config.GRID_NUM; r++) {
			for (int c = 0; c < Config.GRID_NUM; c++) {
				//非空位置，获取估分
				if (chessMap[r][c] == ChessType.NONE) {
					this.competerMap[r][c] = getValue(r, c, this.competerType);
					this.playerMap[r][c] = getValue(r, c, this.playerType);
				}
			}
		}
				
		// 分别选出电脑估分和玩家估分的最大值
		for (int r = 0; r < Config.GRID_NUM; r++) {
			
			for (int c = 0; c < Config.GRID_NUM; c++) {
				// 选出电脑估分的最大值
				if (computerMax == competerMap[r][c]) {
					//估分相同，为了更智能，以一定的概率替换具有相同估分的位置
					if (rd.nextInt(15) % 2 == 0) {
						computerMax = competerMap[r][c];
						computerPoint.x = r;
						computerPoint.y = c;
					}
				} else if (computerMax < competerMap[r][c]) {
					computerMax = competerMap[r][c];
					computerPoint.x = r;
					computerPoint.y = c;
				}
				
				// 选出玩家估分的最大值
				if (playerMax == playerMap[r][c]) {
					//估分相同，为了更智能，以一定的概率替换具有相同估分的位置
					if (rd.nextInt(15) % 2 == 0) {
						playerMax = playerMap[r][c];
						playerPoint.x = r;
						playerPoint.y = c;
					}
				} else if (playerMax < playerMap[r][c]) {
					playerMax = playerMap[r][c];
					playerPoint.x = r;
					playerPoint.y = c;
				}
			}
		}
		
		// 两者都在90~100分之间，电脑主动出击胜率更大
		if (computerMax >= 90 && computerMax < 100 && playerMax >= 90 && playerMax < 100) {
			return computerPoint;
		}
		//若玩家更占优势，攻占玩家位置进行防守，否则主动攻击
		else {
			return playerMax > computerMax ? playerPoint : computerPoint;
		}
	}
	/**
	 * 禁手检查
	 * @param r           行值
	 * @param c           列值
	 * @param chessType   棋子类型
	 * @return   是否为禁手
	 */
	private boolean forbidde(int[] dir, int r, int c, ChessType chessType) {
		int temp1 = 0;
		int temp2 = 0;
		
		// 三三禁手1
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE) {
				//全三
				temp1++;
			}
			if(dir[i] == 3 && chessStatus[i] == ChessStatus.DOUBLE_HALF_ALIVE) {
				//拓展后的两端三
				temp2++;
			}
			if(temp1 == 1 && temp2 == 1) {
				return false;
			}
		}
		
		temp1 = temp2 = 0;
		//三三禁手2
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE) {
				//全三
				temp1++;
			}
			if(temp1 == 2) {
				return false;
			}
		}
		
		temp1 = 0;
		//四四禁手1,2
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.ALIVE) {
				//全三
				temp1++;
			}
			if(temp1 == 2) {
				return false;
			}
		}
		
		temp1 = 0;
		//四四禁手3
		for(int i = 0; i < dir.length; i++) {
			if(dir[i] == 5 && chessStatus[i] == ChessStatus.DIED) {
				return false;
			}
		}
		
		temp1 = 0;
		//四四禁手4
		for(int i = 0; i < dir.length; i++) {
			if(dir[i] == 4 && chessStatus[i] == ChessStatus.DOUBLE_HALF_ALIVE) {
				return false;
			}
		}
		
		temp1 = 0;
		//四三三禁手
		for(int i = 0; i < dir.length; i++) {
			if(dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE) {
				temp1++;
			}
			else if(dir[i] == 4 && chessStatus[i] == ChessStatus.ALIVE) {
				temp2++;
			}
			if(temp1 == 2 && temp2 == 1) {
				return false;
			}
		}
		
		//长连禁手
		for(int i = 0; i < dir.length; i++) {
			if(dir[i] == 5 && chessStatus[i] == ChessStatus.ALIVE) {
				switch (i) {
				case 0:
					//DownRight
					temp1 = 1;
					temp1 = 1;
					break;
				case 1:
					//DownLeft
					temp1 = 1;
					temp2 = -1;
					break;
				case 2:
					//vertical	
					temp1 = 1;
					temp2 = 0;
					break;
				case 3:
					//horizontal
					temp1 = 0;
					temp2 = 1;
					break;
				default:
					temp1 = temp2 = 0;
					break;
				}
				int rTemp = r;
				int cTemp = c;
				boolean empty = true;
				
				//正向
				for(int m = 0 ; m<5 ; m++) {
					rTemp += temp1;
					cTemp += temp2;
					if(rTemp >= Config.GRID_NUM || rTemp < 0) {
						empty = false;
						break;
					}
					else if(cTemp >= Config.GRID_NUM || cTemp < 0) {
						empty = false;
						break;
					}
					else if(chessMap[rTemp][cTemp] == ChessType.NONE) {
						empty = false;
						break;
					}
				}
				//至少一端非空
				if(!empty) {
					return true;
				}
				
				//反向
				rTemp = r;
				cTemp = c;
				for(int m = 0 ; m<5 ; m++) {
					rTemp -= temp1;
					cTemp -= temp2;
					if(rTemp >= Config.GRID_NUM || rTemp < 0) {
						empty = false;
						break;
					}
					else if(cTemp >= Config.GRID_NUM || cTemp < 0) {
						empty = false;
						break;
					}
					else if(chessMap[rTemp][cTemp] == ChessType.NONE) {
						empty = false;
						break;
					}
				}
				//至少一端非空
				if(!empty) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取由行号和列号指定的棋子的评分
	 * @param r           行值
	 * @param c           列值
	 * @param chessType   棋子类型，棋子所有者
	 * @return            评分
	 */
	private int getValue(int r, int c, ChessType chessType) {
		
		//定义四个方向的最大连子数数组
		int[] dir = new int[4];
		//获取四个方向的最大连子数数组
		dir[0] = this.getDownRightCount(r, c, chessType);
		dir[1] = this.getDownLeftCount(r, c, chessType);
		dir[2] = this.getVerCount(r, c, chessType);
		dir[3] = this.getHorCount(r, c, chessType);

		if(!forbidde(dir, r, c, chessType)) {
			return Score.FORBIDDEN;
		}
		// 成五
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] >= 5 && chessStatus[i] == ChessStatus.ALIVE) {
				return Score.FIVE;
			}
		}
		
		int temp = 0;
		//双四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED) {
				temp++;
			}
			if (temp == 2) {
				return Score.DOUBLE_FOUR;
			}
				
		}
		
		temp = 0;
		// 双活三
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && (chessStatus[i] == ChessStatus.ALIVE || chessStatus[i] == ChessStatus.DOUBLE_HALF_ALIVE)) {
				temp++;
			}
			if (temp == 2) {
				return Score.DOUBLE_FULL_THREE;
			}
				
		}
		
		// 全活四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && (chessStatus[i] == ChessStatus.ALIVE || chessStatus[i] == ChessStatus.DOUBLE_HALF_ALIVE)) {
				return Score.FULL_FOUR;
			}
		}
		
		
		//全三
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && (chessStatus[i] == ChessStatus.ALIVE || chessStatus[i] == ChessStatus.DOUBLE_HALF_ALIVE)) {
				return Score.FULL_THREE;
			}
		}
		
		//半四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.HALF_ALIVE) {
				return Score.HALF_FOUR;
			}
		}
		
		temp = 0;
		//双半三
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.HALF_ALIVE) {
				temp++;
			}
			if (temp == 2) {
				return Score.DOUBLE_HALF_THREE;
			}
				
		}
		
		//半三
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED) {
				return Score.HALF_THREE;
			}
		}
		
		//双全活二
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 2 && chessStatus[i] == ChessStatus.ALIVE) {
				temp++;
			}
			if (temp == 2) {
				return Score.DOUBLE_FULL_TWO;
			}
				
		}
		//双半活二
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 2 && chessStatus[i] != ChessStatus.DIED) {
				temp++;
			}
			if (temp == 2) {
				return Score.DOUBLE_HALF_TWO;
			}
				
		}
		//全活二
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 2 && chessStatus[i] == ChessStatus.ALIVE) {
				return Score.FULL_TWO;
			}
		}
	    //半活二
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 2 && chessStatus[i] != ChessStatus.DIED) {
				return Score.HALF_TWO;
			}
		}
	    //活一
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 1 && chessStatus[i] != ChessStatus.DIED) {
				return Score.HALF_TWO;
			}
		}
		
		return 0;	
	}

	/**
	 * 右下方向上的棋子数
	 * @param r           行值
	 * @param c           列值
	 * @param chessType   棋子类型
	 * @return            连成线棋子数
	 */
	private int getDownRightCount(int r, int c, ChessType chessType) {
		int count = 1;
		int tr1 = r + 1;
		int tc1 = c + 1;
		int tr2 = r - 1;
		int tc2 = c - 1;
		boolean rDownAlive = true;
		boolean lUpAlive = true;
		
		//检测当前点的右下端
		for (int i = r + 1, j = c + 1; i < r + 5 ; i++, j++) {
			//右下侧越界
			if (i >= Config.GRID_NUM || j >= Config.GRID_NUM) {
				rDownAlive = false;
				break;
			}
			//同子相连
			if (chessMap[i][j] == chessType) {
				count++;
				//同子成五
				if (count >= 5) {
					chessStatus[0] = ChessStatus.ALIVE;
					return count;
				}
			}
			//右下侧同子结束
			else {
				//遇空结束，右下侧未死
				if(chessMap[i][j] == ChessType.NONE) {
					rDownAlive = true;
				} 
				//遇对方棋子，右下侧已死
				else {
					rDownAlive = false;
				}
				tr1 = i; //记录右下结束位置
				tc1 = j;
				break;
			}
		}
		//检测当前位置的左上侧
		for (int i = r - 1, j = c - 1; i > r - 5; i--, j--) {
			if (i < 0 || j < 0) {
				//右下已死上侧越界
				if (!rDownAlive && count < 5) {
					chessStatus[0] = ChessStatus.DIED;
					return count;
				}
				//标识左上已死
				lUpAlive = false;
				break;
			}
			//同子相连
			if ((chessMap[i][j] == chessType)) {
				count++;
				//成五
				if (count >= 5) {
					chessStatus[0] = ChessStatus.ALIVE;
					return count;
				}
			} 
			else {
				//遇空结束，左上侧未死
				if(chessMap[i][j] == ChessType.NONE) {
					lUpAlive = true;
				} 
				//遇对方棋子，左上侧已死
				else {
					lUpAlive = false;
				}
				tr2 = i; //记录左上结束位置
				tc2 = j;
				break;
			}
		}
		
		//左上右下已死
		if (!lUpAlive && !rDownAlive) {
			chessStatus[0] = ChessStatus.DIED;
			return count;
		} 
		else {
			
			// 当两端有活的时候，看是否可以延伸与两边的棋子组成同线
			int tempCount1 = count, tempCount2 = count;
			boolean isAlive1 = false, isAlive2 = false;
			//右下延伸
			if(rDownAlive) {
				for (int m = tr1 + 1, k = tc1 + 1; m > tr1 + 5; m++, k++) {
					if (m >= Config.GRID_NUM || k >= Config.GRID_NUM)
						break;
					if (chessMap[m][k] == chessType) {
						tempCount1++;
					} else {
						isAlive1 = (chessMap[m][k] == ChessType.NONE) ? true
								: false;
						break;
					}
				}
			}
			//左上延伸
			if(lUpAlive) {
				for (int m = tr2 - 1, k = tc2 - 1; m < tr2 - 5; m--, k--) {
					if (m < 0 || k < 0)
						break;
					if (chessMap[m][k] == chessType) {
						tempCount2++;
					}
					else {
						isAlive2 = (chessMap[m][k] == ChessType.NONE) ? true
								: false;
						break;
					}
				}
			}
			
			// 如果两头都是空，直接跳出
			if (tempCount1 == count && tempCount2 == count) {
				//两端都可以拓展
				if(lUpAlive && rDownAlive) {
					chessStatus[0] = ChessStatus.ALIVE;	
				}
				//只有一侧拓展
				else {
					chessStatus[0] = ChessStatus.HALF_ALIVE;	
				}
				return count;
			}
			
			//左上右下两侧延伸相等
			if (tempCount1 == tempCount2) {
				count = tempCount1;
				
				//越界处理
				if(count >= 5)  {
					count = 5;
				}
				chessStatus[0] = (isAlive1 && isAlive2) ? ChessStatus.DOUBLE_HALF_ALIVE : ChessStatus.HALF_ALIVE;
			} 
			else {
				count = (tempCount1 > tempCount2) ? tempCount1 : tempCount2;
				
				if (count >= 5) {
					count = 5;
				}
				if (tempCount1 > tempCount2) { 
					chessStatus[0] = (isAlive1) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
				
				else {
					chessStatus[0] = (isAlive2) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
			}
		}
		return count;
	}

	/**
	 * 左下方向上的棋子数
	 * @param r           行值
	 * @param c           列值
	 * @param chessType   棋子类型
	 * @return            连成线棋子数
	 */
	private int getDownLeftCount(int r, int c, ChessType chessType) {
		int count = 1;
		int tr1 = r + 1;
		int tc1 = c - 1;
		int tr2 = r - 1;
		int tc2 = c + 1;
		boolean lDownAlive = true;
		boolean rUpAlive = true;
		
		//检测当前点的左下端
		for (int i = r + 1, j = c - 1; i < r + 5 ; i++, j--) {
			//左下侧越界
			if (i >= Config.GRID_NUM || j < 0) {
				lDownAlive = false;
				break;
			}
			//同子相连
			if (chessMap[i][j] == chessType) {
				count++;
				//同子成五
				if (count >= 5) {
					chessStatus[1] = ChessStatus.ALIVE;
					return count;
				}
			}
			//左下侧同子结束
			else {
				//遇空结束，左下侧未死
				if(chessMap[i][j] == ChessType.NONE) {
					lDownAlive = true;
				} 
				//遇对方棋子，左下侧已死
				else {
					lDownAlive = false;
				}
				tr1 = i; //记录左下结束位置
				tc1 = j;
				break;
			}
		}
		//检测当前位置的右上侧
		for (int i = r - 1, j = c + 1; i > r - 5; i--, j++) {
			if (i < 0 || j >= Config.GRID_NUM) {
				//左下已死上侧越界
				if (!lDownAlive && count < 5) {
					chessStatus[1] = ChessStatus.DIED;
					return count;
				}
				//标识右上已死
				rUpAlive = false;
				break;
			}
			//同子相连
			if ((chessMap[i][j] == chessType)) {
				count++;
				//成五
				if (count >= 5) {
					chessStatus[1] = ChessStatus.ALIVE;
					return count;
				}
			} else {
				//遇空结束，右上侧未死
				if(chessMap[i][j] == ChessType.NONE) {
					lDownAlive = true;
				} 
				//遇对方棋子，右上侧已死
				else {
					lDownAlive = false;
				}
				tr2 = i; //记录右上结束位置
				tc2 = j;
				break;
			}
			
		}
		
		//左下右上已死
		if (!rUpAlive && !lDownAlive) {
			chessStatus[1] = ChessStatus.DIED;
			return count;
		} else {
			// 当两端活的时候，看是否可以延伸与两边的棋子组成同线
			int tempCount1 = count, tempCount2 = count;
			boolean isAlive1 = false, isAlive2 = false;
			//左下延伸
			if(lDownAlive) {
				for (int m = tr1 + 1, k = tc1 - 1; m > tr1 + 5; m++, k--) {
					if (m >= Config.GRID_NUM || k < 0)
						break;
					if (chessMap[m][k] == chessType) {
						tempCount1++;
					} else {
						isAlive1 = (chessMap[m][k] == ChessType.NONE) ? true
								: false;
						break;
					}
				}
			}
			//右上延伸
			if(rUpAlive) {
				for (int m = tr2 - 1, k = tc2 + 1; m < tr2 - 5; m--, k++) {
					if (m < 0 || k >= Config.GRID_NUM)
						break;
					if (chessMap[m][k] == chessType) {
						tempCount2++;
					}
					else {
						isAlive2 = (chessMap[m][k] == ChessType.NONE) ? true
								: false;
						break;
					}
				}
			}
			// 如果两头都是不可拓展，直接跳出
			if (tempCount1 == count && tempCount2 == count) {
				//两端都空
				if (rUpAlive && lDownAlive) {
					chessStatus[1] = ChessStatus.ALIVE;
				}
				//仅有一段
				else {
					chessStatus[1] = ChessStatus.HALF_ALIVE;
				}
				return count;
			}
			//左下右上两侧延伸相等
			if (tempCount1 == tempCount2) {
				count = tempCount1;
				//越界处理
				if(count >= 5)  {
					count = 5;
				}
				chessStatus[1] = (isAlive1 && isAlive2) ? ChessStatus.DOUBLE_HALF_ALIVE : ChessStatus.HALF_ALIVE;
			} else {
				count = (tempCount1 > tempCount2) ? tempCount1 : tempCount2;
				if (count >= 5) {
					count = 5;
				}
					
				if (tempCount1 > tempCount2) {
					chessStatus[1] = (isAlive1) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
				else {
					chessStatus[1] = (isAlive2) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
			}
		}
		return count;
	}
	
	/**
	 * 获取上下方向的同子数
	 * @param r 行号
	 * @param c 列号
	 * @param chessType 棋子类型
	 * @return  同子数
	 */
		private int getVerCount(int r, int c, ChessType chessType) {
			int count = 1;
			int t1 = r + 1;    //最下端可下位置
			int t2 = r - 1;    //最上端可下位置
			boolean downAlive = true;
			boolean upAlive = true;
			
			//检测当前点的下端
			for (int j = r + 1; j < r + 5; j++) {
				//下侧越界
				if (j >= Config.GRID_NUM) {
					downAlive = false;
					break;
				}
				//同子相连
				if (chessMap[j][c] == chessType) {
					count++;
					//同子成五
					if (count >= 5) {
						chessStatus[2] = ChessStatus.ALIVE;
						return count;
					}
				}
				//下侧同子结束
				else {
					//遇空结束，下端未死
					if(chessMap[j][c] == ChessType.NONE) {
						downAlive = true;
					} 
					//遇对方棋子，下端已死
					else {
						downAlive = false;
					}
					t1 = j; //记录下端结束位置
					break;
				}
			}
			//检测当前位置的上侧
			for (int j = r - 1; j > r - 5; j--) {
				if (j < 0) {
					//下侧已死上侧越界
					if (!downAlive && count < 5) {
						chessStatus[2] = ChessStatus.DIED;
						return count;
					}
					//标识上侧已死
					upAlive = false;
					break;
				}
				//同子相连
				if (upAlive && (chessMap[j][c] == chessType)) {
					count++;
					//成五
					if (count >= 5) {
						chessStatus[2] = ChessStatus.ALIVE;
						return count;
					}
				} else {
					//遇空结束，上侧未死
					if(chessMap[j][c] == ChessType.NONE) {
						upAlive = true;
					} 
					//遇对方棋子，上侧已死
					else {
						upAlive = false;
					}
					t2 = j; //记录上侧结束位置
					break;
				}
			}
			//下侧上侧已死
			if (!upAlive && !downAlive) {
				chessStatus[2] = ChessStatus.DIED;
				return count;
			} else {
				
				// 当两端有活的时候，看是否可以延伸与两边的棋子组成同线
				int tempCount1 = count, tempCount2 = count;
				boolean isAlive1 = false, isAlive2 = false;
				//下侧延伸
				if(downAlive) {
					for (int i = t1 + 1; i < t1 + 5; i++) {
						if (i >= Config.GRID_NUM)
							break;
						if (chessMap[i][c] == chessType) {
							tempCount1++;
						} else {
							isAlive1 = (chessMap[i][c] == ChessType.NONE) ? true
									: false;
							break;
						}
					}
				}
				//上侧延伸
				if(upAlive) {
					for (int i = t2 - 1; i > t2 - 5; i--) {
						if (i < 0)
							break;
						if (chessMap[i][c] == chessType) {
							tempCount2++;
						}
						else {
							isAlive2 = (chessMap[i][c] == ChessType.NONE) ? true
									: false;
							break;
						}
					}
				}
				// 如果两头都是不可拓展，直接跳出
				if (tempCount1 == count && tempCount2 == count) {
					
					//两端点都可以是拓展
					if (upAlive && downAlive) {
						chessStatus[2] = ChessStatus.ALIVE;
					}
					//仅一端可拓展
					else  {
						chessStatus[2] = ChessStatus.HALF_ALIVE;
					}
					return count;
				}
				//上下两侧延伸相等
				if (tempCount1 == tempCount2) {
					count = tempCount1;
					//越界处理
					if(count >= 5)  {
						count = 5;
					}

					chessStatus[2] = (isAlive1 && isAlive2) ? ChessStatus.DOUBLE_HALF_ALIVE : ChessStatus.HALF_ALIVE;
				} else {
					count = (tempCount1 > tempCount2) ? tempCount1 : tempCount2;
					if (count >= 5) {
						count = 5;
					}
					
					if (tempCount1 > tempCount2) {
						chessStatus[2] = (isAlive1) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
					}
					else {
						chessStatus[2] = (isAlive2) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
					}
				}
			}
			return count;
		}

/**
 * 获取左右方向的同子数
 * @param r 行号
 * @param c 列号
 * @param chessType 棋子类型
 * @return  同子数
 */
	private int getHorCount(int r, int c, ChessType chessType) {
		int count = 1;
		int t1 = c + 1;    //最右端可下位置
		int t2 = c - 1;    //最左端可下位置
		boolean rightAlive = true;
		boolean leftAlive = true;
		
		//检测当前点的右端
		for (int j = c + 1; j < c + 5; j++) {
			//右侧越界
			if (j >= Config.GRID_NUM) {
				rightAlive = false;
				break;
			}
			//同子相连
			if (chessMap[r][j] == chessType) {
				count++;
				//同子成五
				if (count >= 5)  {
					chessStatus[3] = ChessStatus.ALIVE;
					return count;
				}
			}
			//右侧同子结束
			else {
				//遇空结束，右端未死
				if(chessMap[r][j] == ChessType.NONE) {
					rightAlive = true;
				} 
				//遇对方棋子，右端已死
				else {
					rightAlive = false;
				}
				t1 = j; //记录右端结束位置
				break;
			}
		}
		//检测当前位置的左侧
		for (int j = c - 1; j > c - 5; j--) {
			if (j < 0) {
				//右侧已死左侧越界
				if (!rightAlive && count < 5) {
					chessStatus[3] = ChessStatus.DIED;
					return 0;
				}
				//标识左侧已死
				leftAlive = false;
				break;
			}
			//同子相连
			if (leftAlive && (chessMap[r][j] == chessType)) {
				count++;
				//成五
				if (count >= 5)
					return count;
			}
			else {
				//遇空结束，左侧未死
				if(chessMap[r][j] == ChessType.NONE) {
					leftAlive = true;
				} 
				//遇对方棋子，左侧已死
				else {
					leftAlive = false;
				}
				t2 = j; //记录右端结束位置
				break;
			}
		}
		
		//右侧左侧已死
		if (!leftAlive && !rightAlive) {
			chessStatus[3] = ChessStatus.DIED;
			return count;
		} 
		else {
			
			// 当两端有活的时候，看是否可以延伸与两边的棋子组成同线
			int tempCount1 = count, tempCount2 = count;
			boolean isAlive1 = false, isAlive2 = false;
			//右侧延伸
			if(rightAlive) {
				for (int i = t1 + 1; i < t1 + 5; i++) {
					if (i >= Config.GRID_NUM)
						break;
					if (chessMap[r][i] == chessType) {
						tempCount1++;
					} else {
						isAlive1 = (chessMap[r][i] == ChessType.NONE) ? true : false;
						break;
					}
				}
			}
			//左侧延伸
			if(leftAlive) {
				for (int i = t2 - 1; i > t2 - 5; i--) {
					if (i < 0)
						break;
					if (chessMap[r][i] == chessType) {
						tempCount2++;
					}
					else {
						isAlive2 = (chessMap[r][i] == ChessType.NONE) ? true : false;
						break;
					}
				}
			}
			// 如果两头有可拓展，直接跳出
			if (tempCount1 == count && tempCount2 == count) {
				
				//两端可拓展
				if (leftAlive && rightAlive) {
					chessStatus[3] = ChessStatus.ALIVE;
				}
				//仅一端可拓展
				else {
					chessStatus[3] = ChessStatus.DIED;
				}
				return count;
			}
			
			if (tempCount1 == tempCount2) {
				count = tempCount1;
				
				//越界处理
				if(count >= 5)  {
					count = 5;
				}
				chessStatus[3] = (isAlive1 && isAlive2) ? ChessStatus.DOUBLE_HALF_ALIVE : ChessStatus.HALF_ALIVE;
			} else {
				count = (tempCount1 > tempCount2) ? tempCount1 : tempCount2;
				if (count >= 5) {
					count = 5;
				}
					
				if (tempCount1 > tempCount2) {
					chessStatus[3] = (isAlive1) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
							
				else {
					chessStatus[3] = (isAlive2) ? ChessStatus.HALF_ALIVE : ChessStatus.DIED;
				}
			}
		}
		return count;
	}
}
