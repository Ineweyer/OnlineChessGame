package cn.ineweyer.onlinechessgame.game;

/**
 * 棋子类
 * @author LQ
 *
 */
public class Chess {
	//八个方向的多棋子数
	private int east, west, south, north, eastSouth, eastNorth, westSouth,
			westNorth;
	//棋子所在的位置
	private int position;
	//最大棋子
	private int max;
	//最大棋子的方向
	private Location location;

	//棋子所属者标识
	private boolean mineChess; 

	/**
	 * 棋子构造函数
	 * @param east         东方棋子数
	 * @param west         西方棋子数
	 * @param south        南方棋子数
	 * @param north        北方棋子数
	 * @param eastSouth    东南棋子数
	 * @param eastNorth    东北棋子数
	 * @param westSouth    西方棋子数
	 * @param westNorth    西北棋子数
	 * @param used         使用情况
	 * @param position     位置
	 */
	public Chess(int east, int west, int south, int north, int eastSouth,
			int eastNorth, int westSouth, int westNorth, boolean used, int position) {
		super();
		this.east = east;
		this.west = west;
		this.south = south;
		this.north = north;
		this.eastSouth = eastSouth;
		this.eastNorth = eastNorth;
		this.westSouth = westSouth;
		this.westNorth = westNorth;
		this.position = position;
		
	}

	/**
	 * 棋子构造函数
	 * @param isMine    棋子所属者
	 * @param position  棋子位置
	 */
	public Chess(boolean isMine,int position) {
		this.east = 0;
		this.west = 0;
		this.south = 0;
		this.north = 0;
		this.eastSouth = 0;
		this.eastNorth = 0;
		this.westSouth = 0;
		this.westNorth = 0;
		this.position = position;
		this.max = 0;
		this.mineChess = isMine;
	}
	
	/**
	 * 刷新最大值
	 * @return  该棋子与周围的棋子能够围成的最大棋子数
	 */
	public int freshMax() {
		if(this.max <= (this.east + this.west + 1)) {
			this.max = (this.east + this.west) + 1;
			this.location = Location.EAST;
		}
		if(this.max <= (this.south + this.north + 1)) {
			this.max = (this.south + this.north) + 1;
			this.location = Location.NORTH;
		}
		if(this.max <= (this.eastNorth + this.westSouth + 1)) {
			this.max = (this.eastNorth + this.westSouth) + 1;
			this.location = Location.EAST_NORTH;
		}
		if(this.max <= (this.eastSouth + this.westNorth + 1)) {
			this.max = (this.eastSouth + this.westNorth) + 1;
			this.location = Location.EAST_SOUTH;
		}
		return this.max;
	}
	
	/**
	 * 当悔棋时修改一条直线上的最大棋子数
	 * @param atLocation   刷新单个方向的最大棋子数
	 */
	public void fixMax(Location atLocation) {
		switch(atLocation) {
		case NORTH :
		case SOUTH : 
			this.max = (this.south + this.north) + 1;
			this.location = Location.NORTH;
			break;
		case WEST :
		case EAST :
			this.max = (this.east + this.west) + 1;
			this.location = Location.EAST;
			break;
		case WEST_NORTH :
		case EAST_SOUTH :
			this.max = (this.eastSouth + this.westNorth) + 1;
			this.location = Location.EAST_SOUTH;
			break;
		case WEST_SOUTH :
		case EAST_NORTH :
			this.max = (this.eastNorth + this.westSouth) + 1;
			this.location = Location.EAST_NORTH;
			break;
		}
	}
	
	/**
	 * 用户刷新当前下的棋子,为某个方向增加棋子数
	 * @param location   位置
	 * @param size       延展棋子数
	 */
	public void freshLocation(Location location, int size) {
		switch(location) {
		case NORTH :
			setNorth(size + this.north);
			break;
		case EAST_NORTH :
			setEastNorth(size + this.south);
			break;
		case EAST :
			setEast(size + this.east);
			break;
		case EAST_SOUTH :
			setEastSouth(size + this.eastSouth);
			break;
		case SOUTH:
			setSouth(size + this.south);
			break;
		case WEST_SOUTH:
			setWestSouth(size + westSouth);
			break;
		case WEST :
			setWest(size + west);
			break;
		case WEST_NORTH :
			setWestNorth(size + westNorth);
			break;
		}
		
	}
	
	/**
	 * 根据方向获取对应方向的在一条线上的棋子数
	 * @param location  棋子位置
	 * @return  对应方向上的棋子数
	 */
	public int getLocationSize(Location location) {
		int size = 0;
		switch (location) {
		case NORTH:
			size = getNorth();
			break;
		case EAST_NORTH:
			size = getEastNorth();
			break;
		case EAST:
			size = getEast();
			break;
		case EAST_SOUTH:
			size = getEastSouth();
			break;
		case SOUTH:
			size = getSouth();
			break;
		case WEST_SOUTH:
			size = getWestSouth();
			break;
		case WEST:
			size = getWest();
			break;
		case WEST_NORTH:
			size = getWestNorth();
			break;
		}
		return size;
	}
	
	/**
	 * 当前棋子的所有情况
	 * @return 当前棋子的所有情况
	 */
	public boolean isMineChess() {
		return mineChess;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		String str;
		str = " position: " + position;
		str += " north: " + north;
		str += " eastNorth: " + eastNorth;
		str += " east: "  + east;
		str += " eastSouth: " + eastSouth;
		str += " south: " + south;
		str += " westSouth: " + westSouth;
		str += " west: " + west;
		str += " westNorth: " + westNorth;
		str += " max:" + max;
		str += " location: " + location;
		str += " mineChess: " + mineChess;
		return str;
	}
	
	/**
	 * @return the east
	 */
	public int getEast() {
		return east;
	}

	/**
	 * @param east
	 *            the east to set
	 */
	public void setEast(int east) {
		this.east = east;
	}

	/**
	 * @return the west
	 */
	public int getWest() {
		return west;
	}

	/**
	 * @param west
	 *            the west to set
	 */
	public void setWest(int west) {
		this.west = west;
	}

	/**
	 * @return the south
	 */
	public int getSouth() {
		return south;
	}

	/**
	 * @param south
	 *            the south to set
	 */
	public void setSouth(int south) {
		this.south = south;
	}

	/**
	 * @return the north
	 */
	public int getNorth() {
		return north;
	}

	/**
	 * @param north
	 *            the north to set
	 */
	public void setNorth(int north) {
		this.north = north;
	}

	/**
	 * @return the eastSouth
	 */
	public int getEastSouth() {
		return eastSouth;
	}

	/**
	 * @param eastSouth
	 *            the eastSouth to set
	 */
	public void setEastSouth(int eastSouth) {
		this.eastSouth = eastSouth;
	}

	/**
	 * @return the eastNorth
	 */
	public int getEastNorth() {
		return eastNorth;
	}

	/**
	 * @param eastNorth
	 *            the eastNorth to set
	 */
	public void setEastNorth(int eastNorth) {
		this.eastNorth = eastNorth;
	}

	/**
	 * @return the westSouth
	 */
	public int getWestSouth() {
		return westSouth;
	}

	/**
	 * @param westSouth
	 *            the westSouth to set
	 */
	public void setWestSouth(int westSouth) {
		this.westSouth = westSouth;
	}

	/**
	 * @return the westNorth
	 */
	public int getWestNorth() {
		return westNorth;
	}

	/**
	 * @param westNorth
	 *            the westNorth to set
	 */
	public void setWestNorth(int westNorth) {
		this.westNorth = westNorth;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

}