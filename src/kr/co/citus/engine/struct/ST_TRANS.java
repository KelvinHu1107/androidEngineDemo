package kr.co.citus.engine.struct;

public class ST_TRANS {
	public int 		dist; // �Ÿ�
	public int		time; // �ð�
	public int		pathCnt;
	public String[]	fname;
	public String[]	routeNm;
	public String[]	tname;
	
	public ST_TRANS()	{ init(); }
	void init()
	{
		dist = 0;
		time = 0;
		pathCnt = 0;
		fname = null;
		routeNm = null;
		tname = null;
	}
}
