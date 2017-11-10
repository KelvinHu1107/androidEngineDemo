package kr.co.citus.engine.struct;

public class DB_ADDR_INFO {
	String 		name;	///< 업종이름
	int			num;		///< 하위업종 개수
	int			code;		///< 업종코드
	public DB_ADDR_INFO()	{ init(); }
	void init()
	{
		name = null;
		num = 0;
		code = 0;
	}
}
