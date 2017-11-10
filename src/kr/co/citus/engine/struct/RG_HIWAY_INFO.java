package kr.co.citus.engine.struct;

public class RG_HIWAY_INFO {

	public String szText;
	public String szForwardRName;
	public int partIdx;
	public int kind;
	public int group_count;
	public int x,y;
	public int distFromStart;
	public char sign = 0;
	public char state = 0;
	
	public RG_HIWAY_INFO()
	{
		init();
	}
	
	public void init()
	{
		szText = null;
		szForwardRName = null;
		partIdx = 0;
		kind = 0;
		x = y = 0;
		distFromStart = 0;
		sign = '0' ;
		state = '0' ;
	}
}
