package kr.co.citus.engine.struct;

public class NDB_POI_BODY_INFO {
	
	public String	name1;
	public String	name2;
	public String	admin_name;
	public String	kind1_name;
	public String	kind2_name;
	public String	kind3_name;
	public String	address;
	public String	tel;
	public String	ubcode;
	public int		brd_idx;
	public int		roadid_and_se;
	public int		kind_idx;
	public int		admin_idx;
	public int		group_idx;
	public int		flag;
	public int		x,y;

	public NDB_POI_BODY_INFO()
	{
		init();
	}
	
	void init()
	{
		name1 = null;
		name2 = null;
		admin_name = null;
		kind1_name = null;
		kind2_name = null;
		kind3_name = null;
		address = null;
		tel = null;
		ubcode = null;
		roadid_and_se = 0;
		brd_idx = 0;
		kind_idx = 0;
		admin_idx = 0;
		group_idx = 0;
		flag = 0;
		x = 0;
		y = 0;
	}
}
