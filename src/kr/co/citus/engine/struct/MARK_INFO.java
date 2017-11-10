package kr.co.citus.engine.struct;

public class MARK_INFO {
	
	public String			name;			///< �̸�    
	public String			desc;			///< ����
	public String			tel;			///< ��ȭ
	public int				addrIdx;
	public int				x;				///< ��ġ��ǥ X
	public int				y;				///< ��ġ��ǥ Y
	public int				kind;			///< �����ڵ�, ������ϰ�쿡�� HOT���η� ���
	public int				symbol;			///< ǥ�� �ɺ�
	public int				groupIdx;

	public MARK_INFO()	{ init(); }
	void init()
	{
		name = null;    
		desc = null;
		tel = null;
		addrIdx = 0;
		x = 0;
		y = 0;
		kind = 0;
		symbol = 0;
		groupIdx = 0;
	}
}


