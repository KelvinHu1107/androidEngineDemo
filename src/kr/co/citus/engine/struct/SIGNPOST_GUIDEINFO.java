package kr.co.citus.engine.struct;

public class SIGNPOST_GUIDEINFO {
	public boolean is_active;
	public double remain_dist;
	public int turn_type;
	public String info;
	public String exit_num;
	
	public SIGNPOST_GUIDEINFO()
	{
		init();
	}
	
	public void init()
	{
		is_active = false;
		remain_dist = 0;
		turn_type = 0;
		info = null;
		exit_num = null;
	}
}
