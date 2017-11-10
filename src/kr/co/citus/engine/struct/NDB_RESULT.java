package kr.co.citus.engine.struct;

public class NDB_RESULT {
	
	public String 	name1;
	public String 	name2;
	public int		body_idx;
	public int		x, y;
	public int		distance;
	public short	kind_idx;
	public short	admin_idx;
	public short	angle;
	public int		group_idx;
	public int		group_count;
	public int		name1_len;
	public int		name2_len;
	public int		ch_seq;
	public int		roadid_and_se;
	public int		brd_code;
	public int		fuzzy_mode;
	
	public byte		addr_search_info0; // 0:
	public byte		addr_search_info1; // 0:
	public byte		addr_search_info2; // 0:
	public byte		addr_search_info3; // 0:
	public byte		addr_search_info4; // 0:
	
	// for Android
	public int		db_type;
	public String	admin1;
	public String 	admin2;
	public String	admin3;
	public int		admin1_idx;
	public int		admin2_idx;
	public int		admin3_idx;
	
	public NDB_RESULT()
	{
		init();
	}
	
	public NDB_RESULT(String _name, int _db_type)
	{
		init();
		name1 = new String(_name);
		db_type = _db_type;
	}
	
	public NDB_RESULT(NDB_RESULT _item)
	{
		init();
		if(_item.name1 != null) name1 = new String(_item.name1);
		if(_item.name2 != null) name2 = new String(_item.name2);
		//if(_item.admin_str != null) admin_str = new String(_item.admin_str);
		body_idx = _item.body_idx;
		x = _item.x;
		y = _item.y;
		distance = _item.distance;
		kind_idx = _item.kind_idx;
		admin_idx = _item.admin_idx;
		angle = _item.angle;
		group_idx = _item.group_idx;
		group_count = _item.group_count;
		name1_len = _item.name1_len;
		name2_len = _item.name2_len;
		ch_seq = _item.ch_seq;
		roadid_and_se = _item.roadid_and_se;
		db_type = _item.db_type;
		
		brd_code = _item.brd_code;
		addr_search_info0 = _item.addr_search_info0;
		addr_search_info1 = _item.addr_search_info1;
		addr_search_info2 = _item.addr_search_info2;
		addr_search_info3 = _item.addr_search_info3;
		addr_search_info4 = _item.addr_search_info4;
		
//		admin1_idx = admin2_idx = admin3_idx = -1;
	}
	
	
	void init()
	{
		name1 = null;
		name2 = null;
		
		body_idx = 0;
		x = 0;
		y = 0;
		distance = 0;
		kind_idx = 0;
		admin_idx = 0;
		angle = 0;
		group_idx = 0;
		group_count = 0;
		name1_len = 0;
		name2_len = 0;
		ch_seq = 0;
		roadid_and_se = 0;
		
		db_type = -1;
		brd_code = 0;
		fuzzy_mode = 0;
		addr_search_info0 = 0;
		addr_search_info1 = 0;
		addr_search_info2 = 0;
		addr_search_info3 = 0;
		addr_search_info4 = 0;
		
		admin1_idx = admin2_idx = admin3_idx = -1;
		//admin_str = null;
		
	}
	
	public boolean addr_has_houseno()
	{
		return addr_search_info3 > 0;
	}
	public boolean addr_has_admin()
	{
		return addr_search_info0 > 0;
	}
	public boolean addr_has_admin3()
	{
		return addr_search_info2>0;
	}
	public int get_total_input_length_without_last()
	{
		return addr_search_info0 + addr_search_info1 + addr_search_info2 + addr_search_info3;
	}
}