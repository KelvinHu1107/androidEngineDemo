package kr.co.citus.engine.struct;

public class MAP_MATCH_RESULT_INFO 
{
	public double	x;				///< map matching position X
	public double	y;				///< map matching position Y
	public double	gps_x;			///< GPS position X
	public double	gps_y;			///< GPS position Y
	public int		angle;			///< car angle
	public int		speed;			///< car speed

	public int		map_idx;		///< matched mesh index
	public int		link_idx;		///< matched link index
	public int		link_dir;		///< driving direction on link, 1:forward, -1:backword, 0:unknown
	public int		vtx_idx;
	public double	vtx_dist;
	public long	dist;			///< passed distance by direction (link_dir)

	public int		is_match;		///< 0: unmatched. other is matched

	public int     isHighway;		///< 0: not highway 
	public int     roadType2;		// road type
	public int		roadClass2;		// road class
	public int		kwt_roadId;		// Kingwaytek ROAD_ID
	public int		kwt_kind;		// Kingwaytek road kind 
	public int		kwt_rclass;		// kingwaytek rclass
	public int		kwt_road_speed;	// kingwaytek road speed
	public String	roadName=null;	///< road name
	
	
	public MAP_MATCH_RESULT_INFO()	{ init(); }
	
	void init()
	{
		x = 0;
		y = 0;
		gps_x = 0;
		gps_y = 0;
		angle = 0;
		speed = 0;

		map_idx = 0;
		link_idx = 0;
		link_dir = 0;
		vtx_idx = 0;
		vtx_dist = 0;
		dist = 0;

		is_match = 0;

		isHighway = 0;
		roadType2 = 0;
		roadClass2 = 0;
		kwt_roadId = 0;
		kwt_kind = 0;
		kwt_rclass = 0;
		kwt_road_speed = 0;
		roadName=null;
	}
}
