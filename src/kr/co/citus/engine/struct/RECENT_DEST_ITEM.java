package kr.co.citus.engine.struct;

public class RECENT_DEST_ITEM {
	
	public String	admin;
	public int		kind;
	public int		tmp;
	public boolean	bShowMidInfo;
	public int		lrpx;
	public int		lrpy;
	
	public String	szStart;		// 출발지 명칭
	public IPOINT	pos_start;		// 출발지 위치
	public String	szDestination;	// 도착지 명칭
	public IPOINT	pos_end;		// 도착지 위치
	public int		destRpOpt;		// 목적지 까지 경로 탐색 옵셕 (추천/고속/일반/최단)
	public int		nViaPos;		// 경유지 개수
	public String[]	szViaName;		// 경유지 명칭
	public IPOINT[]	pos_via;		// 경유지 위치 // 가상 경유지 위치를 저장하기 위해 여유 있게 잡는다. 
	public IPOINT[]	pos_via2;		// 경유지 위치 // 가상 경유지 위치를 저장하기 위해 여유 있게 잡는다. 
	public int[]	viaRpOpt;		// 경유지별 탐색 옵션 (추천/고속/일반/최단)
	
	public int		pos_start_x;		// 출발지 위치
	public int		pos_start_y;		// 출발지 위치
	public int		pos_end_x;		// 도착지 위치
	public int		pos_end_y;		// 도착지 위치

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


