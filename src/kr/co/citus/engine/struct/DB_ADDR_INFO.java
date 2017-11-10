package kr.co.citus.engine.struct;

public class DB_ADDR_INFO {
	String 		name;	///< �����̸�
	int			num;		///< �������� ����
	int			code;		///< �����ڵ�
	public DB_ADDR_INFO()	{ init(); }
	void init()
	{
		name = null;
		num = 0;
		code = 0;
	}
}
