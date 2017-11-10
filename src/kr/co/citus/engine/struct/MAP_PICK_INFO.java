package kr.co.citus.engine.struct;

public class MAP_PICK_INFO {
	public int map_x, map_y;
	public double lat, lon;
	public int info_type;	// 0:POI, 1:Road, 2:Coord
	public int poi_idx;	// info_type = 0
	public int map_idx, link_idx;
	
	public String name;
	
	public MAP_PICK_INFO()
	{
		init();
	}
	
	public void init()
	{
		map_x = map_y = map_idx = link_idx = 0;
		lat = lon = 0.;
		info_type = -1;
		poi_idx = -1;
	}
}
