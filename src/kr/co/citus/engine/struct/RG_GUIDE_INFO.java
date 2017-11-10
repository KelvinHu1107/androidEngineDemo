package kr.co.citus.engine.struct;

public class RG_GUIDE_INFO {
	public double		targDist;
	public int			targIdx;
	public int			snd_dist;
	public int			snd_dir_code;
	public int			snd_info_code;
	public int			iRoadType;
	public String		cpDirName;// [64];
	public String		rname;//[64];
	public String		cname;//[64];
	
	public int			img_idx;
	public String		disItem;
	public int          targX;
	public int          targY;
	public int			nodeX;
	public int			nodeY;

	public int 			kwt_RoadId;
	public int			kwt_rclass;
	public int			kwt_kind;
	public int 			kwt_RoadSpeed;
	public int 			kwt_RoadNo;
	
	public int			map_idx;
	public int			link_idx;
	public int			se;
	public int 			length ;
	public int			kwt_state;
	public int			kwt_zlevel;
	
	public RG_GUIDE_INFO()	{ init(); }
	
	void init()
	{
		targDist = 0;
		targIdx = 0;
		snd_dist = 0;
		snd_dir_code = 0;
		snd_info_code = 0;
		iRoadType = 0;
		cpDirName = null;
		rname = null;
		cname = null;
		
		img_idx = 0;
		disItem = null;
		targX = 0;
		targY = 0;
		
		kwt_RoadId = 0;
		kwt_rclass = 0;
		kwt_kind   = 0;
		kwt_RoadSpeed = 0;
		kwt_RoadNo = 0;
		
		map_idx = 0;
		link_idx = 0;
		se = 0;
		length = 0 ;
		kwt_state = 0;
		kwt_zlevel = 0;
	}
}
