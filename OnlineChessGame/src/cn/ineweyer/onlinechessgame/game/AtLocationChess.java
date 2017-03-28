package cn.ineweyer.onlinechessgame.game;

/**
 *用于棋子附近位置的棋子
 * @author LQ
 *
 */
public class AtLocationChess {
	private Location location;
	private int position;
	
	
	public AtLocationChess(Location location, int position) {
		super();
		this.location = location;
		this.position = position;
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
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
	
}
