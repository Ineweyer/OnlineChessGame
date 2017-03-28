package cn.ineweyer.onlinechessgame.game.update;

/**
 * 棋子唯一标识类，
 * @author LQ
 *
 */
public class TypeAndLocation {
	int location;
	ChessType chessType;
	
	/**
	 * 棋子唯一标识类构造函数
	 * @param location   位置
	 * @param chessType  棋子类型
	 */
	public TypeAndLocation(int location, ChessType chessType) {
		this.location = location;
		this.chessType = chessType;
	}

	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(int location) {
		this.location = location;
	}

	/**
	 * @return the chessType
	 */
	public ChessType getChessType() {
		return chessType;
	}

	/**
	 * @param chessType the chessType to set
	 */
	public void setChessType(ChessType chessType) {
		this.chessType = chessType;
	}
	
	
}
