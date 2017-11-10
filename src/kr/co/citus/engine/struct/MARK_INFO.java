package kr.co.citus.engine.struct;

public class MARK_INFO {
	
	public String			name;			///< 이름    
	public String			desc;			///< 설명
	public String			tel;			///< 전화
	public int				addrIdx;
	public int				x;				///< 위치좌표 X
	public int				y;				///< 위치좌표 Y
	public int				kind;			///< 종별코드, 등록지일경우에는 HOT여부로 사용
	public int				symbol;			///< 표시 심볼
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


