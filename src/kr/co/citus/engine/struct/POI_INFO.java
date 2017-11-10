package kr.co.citus.engine.struct;

public class POI_INFO {
	
	public String	name;	///< 검색명
	public String	addr;	///< 검색위치 주소
	public String	tel;	///< 검색위치 전화번호
	public String	kindName;
	public int		x,y;		// srpark
	public byte		rp_flag;
	public int		kind;		///< 업종코드
	public int		roadid_and_se;
	
	
	public POI_INFO()	{ init(); }
	void init()
	{
		name = null;
		addr = null;
		tel = null;
		kindName = null;
		x = 0;
		y = 0;
		kind = 0;
		rp_flag = 0;
		roadid_and_se = 0;
	}
	public POI_INFO(String _name, String _addr, String _tel, String _kindName, int _x, int _y, int _kind, byte _rp_flag, int _roadid_and_se)
	{
		name = new String(_name);
		addr = new String(_addr);
		tel = new String(_tel);
		kindName = new String(_kindName);
		x = _x;
		y = _x;
		kind = _kind;
		rp_flag = _rp_flag;
		roadid_and_se = _roadid_and_se;
	}
}
