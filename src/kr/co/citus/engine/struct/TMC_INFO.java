package kr.co.citus.engine.struct;

import java.util.ArrayList;

public class TMC_INFO {
	public ArrayList<View_RoadSpeed> viewRoadSpeed = null;
	public ArrayList<GetCCTVByRoadID> getCCTVByRoadID = null;
	public ArrayList<DT_CityCMS> cityCMS = null;
	public ArrayList<Note> note = null;
	public ArrayList<Note_Road> noteRoad = null;
	public ArrayList<DT_CityEvent> cityEvent = null;
	public ArrayList<DT_AccidentProne> accidentProne = null;
	
	public void clear()
	{
		if (viewRoadSpeed != null)
		{
			viewRoadSpeed.clear();
			viewRoadSpeed = null;
		}
		
		if (getCCTVByRoadID != null)
		{
			getCCTVByRoadID.clear();
			getCCTVByRoadID = null;
		}

		if (cityCMS != null)
		{
			cityCMS.clear();
			cityCMS = null;
		}

		if (note != null)
		{
			note.clear();
			note = null;
		}

		if (noteRoad != null)
		{
			noteRoad.clear();
			noteRoad = null;
		}

		if (cityEvent != null)
		{
			cityEvent.clear();
			cityEvent = null;
		}

		if (viewRoadSpeed != null)
		{
			accidentProne.clear();
			accidentProne = null;
		}

	}
	public static class View_RoadSpeed
	{
		public int RoadID = 0;
		public int Speed = 0;
	}
	
	public static class GetCCTVByRoadID
	{
		public int RoadID = 0;
		public int CameraID = 0;
		public int lat = 0;
		public int lon = 0;
		public String Camera_Name = null;
	}
	
	public static class DT_CityCMS
	{
		public String DC_CMSID = null;
		public String DC_Location = null;
		public String DC_Content = null;
		public String DC_UpdateTime = null;
		public int DC_CityID = 0;
		public int DC_px = 0;
		public int DC_py = 0;
		public int DC_RoadID = 0;
	}
	
	public static class Note
	{
		public int NoteID = 0;
		public String Note = null;
		public String UpdateTime = null;
	}
	
	public static class Note_Road
	{
		public int RoadID = 0;
		public String STIME = null;
		public String ETIME = null;
		public int NoteID = 0;
	}
	
	public static class DT_CityEvent
	{
		public String DC_Content = null;
		public int DC_CityID = 0;
		public int DC_px = 0;
		public int DC_py = 0;
		public String DC_eType = null;
		public int DC_RoadID = 0;
		public String DC_HappenTime = null;
		public String DC_UpdateTime = null;
		public String DC_RoadName = null;
	}
	
	public static class DT_AccidentProne 
	{
		public String DC_Content = null;
		public int DC_CityID = 0;
		public int DC_px = 0;
		public int DC_py = 0;
		public String DC_eType = null;
		public int DC_RoadID = 0;
		public String DC_HappenTime = null;
		public String DC_UpdateTime = null;
		public String DC_RoadName = null;
		
	}
}
