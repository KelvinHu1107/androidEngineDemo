package kr.co.citus.engine.struct;

public class RECENT_DEST_ITEM {
	
	public String	admin;
	public int		kind;
	public int		tmp;
	public boolean	bShowMidInfo;
	public int		lrpx;
	public int		lrpy;
	
	public String	szStart;		// ����� ��Ī
	public IPOINT	pos_start;		// ����� ��ġ
	public String	szDestination;	// ������ ��Ī
	public IPOINT	pos_end;		// ������ ��ġ
	public int		destRpOpt;		// ������ ���� ��� Ž�� �ɼ� (��õ/���/�Ϲ�/�ִ�)
	public int		nViaPos;		// ������ ����
	public String[]	szViaName;		// ������ ��Ī
	public IPOINT[]	pos_via;		// ������ ��ġ // ���� ������ ��ġ�� �����ϱ� ���� ���� �ְ� ��´�. 
	public IPOINT[]	pos_via2;		// ������ ��ġ // ���� ������ ��ġ�� �����ϱ� ���� ���� �ְ� ��´�. 
	public int[]	viaRpOpt;		// �������� Ž�� �ɼ� (��õ/���/�Ϲ�/�ִ�)
	
	public int		pos_start_x;		// ����� ��ġ
	public int		pos_start_y;		// ����� ��ġ
	public int		pos_end_x;		// ������ ��ġ
	public int		pos_end_y;		// ������ ��ġ

	public RECENT_DEST_ITEM()	{ init(); }
	void init()
	{
		admin = null;
		kind = 0;
		tmp = 0;
		bShowMidInfo = false;
		lrpx = 0;
		lrpy = 0;
		
		szStart = null;
		pos_start = null;
		szDestination = null;
		pos_end = null;
		destRpOpt = 0;
		nViaPos = 0;
		szViaName = null;
		pos_via = null; 
		pos_via2 = null; 
		viaRpOpt = null;
		
		pos_start_x = 0;
		pos_start_y = 0;
		pos_end_x = 0;
		pos_end_y = 0;
	}
}


