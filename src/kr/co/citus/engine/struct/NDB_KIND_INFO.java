package kr.co.citus.engine.struct;

public class NDB_KIND_INFO {
	
	public String	kind1_name;
	public String	kind2_name;
	public String	kind3_name;
	public int		kind_idx;
	public int		kind1_idx;
	public int		kind2_idx;
	public int		kind3_idx;
	public int		kind_code;
	public int		depth;
	public int		child_num;

	public NDB_KIND_INFO()
	{
		init();
	}
	
	void init()
	{
		kind1_name = null;
		kind2_name = null;
		kind3_name = null;
		kind_idx = 0;
		kind1_idx = 0;
		kind2_idx = 0;
		kind3_idx = 0;
		kind_code = 0;
		depth = 0;
		child_num = 0;
	}
}
