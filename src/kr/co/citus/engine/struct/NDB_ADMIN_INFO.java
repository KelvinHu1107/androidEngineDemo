package kr.co.citus.engine.struct;

public class NDB_ADMIN_INFO {
	
	public String	admin1_name;
	public String	admin2_name;
	public String	admin3_name;
	public int		admin_idx;
	public int		admin1_idx;
	public int		admin2_idx;
	public int		admin3_idx;
	public int		depth;
	public int		child_num;
	public int		x,y;

	public NDB_ADMIN_INFO()
	{
		init();
	}
	
	void init()
	{
		admin1_name = null;
		admin2_name = null;
		admin3_name = null;
		admin_idx = 0;
		admin1_idx = 0;
		admin2_idx = 0;
		admin3_idx = 0;
		depth = 0;
		child_num = 0;
		x = 0;
		y = 0;
	}
}
