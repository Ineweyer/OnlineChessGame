package cn.ineweyer.onlinechessgame.game.update;

/**
 * 棋子评分表
 * @author LQ
 *
 */
public class Score { 
 
	//再走一棋保证赢
    public final static int FIVE = 100;                       //成五
    public final static int DOUBLE_FOUR = 99;                 //双四
    public final static int DOUBLE_FULL_THREE = 95;           //双全三
    public final static int FULL_FOUR = 93;                   //全四
    
    //占很大优势，可以进攻
    public final static int FULL_THREE = 88;                  //全三
    public final static int HALF_FOUR = 85;                   //半四
    public final static int DOUBLE_HALF_THREE = 82;           //双半三
    public final static int HALF_THREE = 80;                  //半三
    
    //优势特别小
    public final static int DOUBLE_FULL_TWO = 65;             //双全活二
    public final static int DOUBLE_HALF_TWO = 60;             //双半活二
    public final static int FULL_TWO = 55;                    //全活二
    public final static int HALF_TWO = 50;                    //半活二
    public final static int ONE = 0;                          //活一
    
    public final static int FORBIDDEN = -1;                   //禁手
}
