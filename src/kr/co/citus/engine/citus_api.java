package kr.co.citus.engine;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.citus.engine.struct.BOUND_BOX;
import kr.co.citus.engine.struct.DB_ADDR_INFO;
import kr.co.citus.engine.struct.DB_ITEM;
import kr.co.citus.engine.struct.DB_KIND_INFO;
import kr.co.citus.engine.struct.ICON_INFO;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.MAP_MATCH_RESULT_INFO;
import kr.co.citus.engine.struct.MAP_PICK_INFO;
import kr.co.citus.engine.struct.MARK_INFO;
import kr.co.citus.engine.struct.MM_IPC_CAND_INFO;
import kr.co.citus.engine.struct.NDB_ADMIN_INFO;
import kr.co.citus.engine.struct.NDB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_POI_BODY_INFO;
import kr.co.citus.engine.struct.NDB_RESULT;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.struct.RECENT_DEST_ITEM;
import kr.co.citus.engine.struct.RG_GUIDE_INFO;
import kr.co.citus.engine.struct.RG_HIWAY_INFO;
import kr.co.citus.engine.struct.RG_REMAIN_INFO;
import kr.co.citus.engine.struct.SIGNPOST_GUIDEINFO;
import kr.co.citus.engine.struct.TMC_ROUTE_INFO;
import kr.co.citus.engine.wrap.WrapBoolean;
import kr.co.citus.engine.wrap.WrapDouble;
import kr.co.citus.engine.wrap.WrapInt;
import kr.co.citus.engine.wrap.WrapString;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.Surface;
import android.widget.TextView;

public class citus_api
{
	static {
		System.loadLibrary("curl");
		System.loadLibrary("hci_sys");
		System.loadLibrary("hci_sys_jni");
		System.loadLibrary("hci_tts");
		System.loadLibrary("hci_tts_jni");
		System.loadLibrary("hci_tts_local_v6_synth");
		System.loadLibrary("hci_tts_local_v6-v5_synth");
		System.loadLibrary("jtopus");
		System.loadLibrary("jtspeex");
		System.loadLibrary("jtz");
		System.loadLibrary("Rousen");
	}
	
	public static int TRUE = 1;
	public static int FALSE = 0;
	
	public static final int DB_TYPE_POI = 0;
	
	// route plan method
	public static final int RP_METHOD_OPTIMAL = 0;
	public static final int RP_METHOD_SHORT   = 1;
	public static final int RP_METHOD_NO_HIGHWAY = 2;
	public static final int RP_METHOD_HIGHWAY = 3;
	public static final int RP_METHOD_1ST_HIGHWAY = 4;
	public static final int RP_METHOD_3RD_HIGHWAY = 5;

	// route plan nav mode (car type)
	public static final int RP_VEHICLE_CAR = 0;
	public static final int RP_VEHICLE_BICYCLE   = 1;
	public static final int RP_VEHICLE_MOTOR = 2;
	public static final int RP_VEHICLE_HAVEY_MOTOR = 3;
	public static final int RP_VEHICLE_TRUCK = 4;
	public static final int RP_VEHICLE_HAVEY_TRUCK = 5;


	// UI_RoutePlan Error Code
	public static final int RP_ERROR_NO_START_POS		=1000;
	public static final int RP_ERROR_NO_END_POS			=1000+1;
	public static final int RP_ERROR_NO_VIA_POS			=1000+2;
	public static final int RP_ERROR_TOO_CLOSE_START_END=1000+3;
	public static final int RP_ERROR_TOO_CLOSE_START_VIA=1000+4;
	public static final int RP_ERROR_TOO_CLOSE_VIA_END	=1000+5;
	public static final int RP_ERROR_TOO_CLOSE_VIA_VIA	=1000+6;
	public static final int RP_ERROR_WRONG_START_POS	=1000+7;
	public static final int RP_ERROR_WRONG_END_POS		=1000+8;
	public static final int RP_ERROR_ISOLATED_START_END	=1000+9;
	public static final int RP_ERROR_ISOLATED_VIA_END	=1000+10;
	public static final int RP_ERROR_SHOULD_USE_FERRY	=1000+11;
	public static final int RP_ERROR_HAS_UNREACHABLE	=1000+12;

	public static final int RP_ERROR_LOOP_OVER_FLOW				= 14;	// maximum loop count overflow (max loop = 65535)
	public static final int RP_ERROR_NETWORK_ROAD_CONNECTION	= 21;
	public static final int RP_ERROR_CANNOT_MAKE_ROUTE			= 22;
	public static final int RP_ERROR_ROAD_BLOCKED_START			= 23;
	public static final int RP_ERROR_ROAD_BLOCKED_END			= 24;
	public static final int RP_ERROR_BECAUSE_AVOID				= 25;

	
	// sound info codes
		public static final int  SND_DIR_0			= 0;
		public static final int  SND_DIR_STRAIGHT	= 0;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ãƒ«ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		public static final int	 SND_DIR_RIGHT_1	= 1;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?1ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int	 SND_DIR_RIGHT		= 2;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int	 SND_DIR_RIGHT_4	= 3;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?4ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int	 SND_DIR_LEFT_11	= 4;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 11ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int	 SND_DIR_LEFT		= 5;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int	 SND_DIR_LEFT_8		= 6;   //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 8ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int	 SND_DIR_UTURN		= 7;   //Uï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		public static final int  SND_DIR_PTURN		= 8;	// P-Turnï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		public static final int  SND_DIR_RTURN		= 9;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½.
		public static final int  SND_DIR_LTURN		= 10;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½.
		public static final int  SND_DIR_GO_12		= 11;	// 12ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int  SND_DIR_RIGHT_2	= 12;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?2ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int  SND_DIR_RIGHT_3	= 13;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?3ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int  SND_DIR_RIGHT_5	= 14;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?5ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int  SND_DIR_LEFT_10	= 15;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 10ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		public static final int  SND_DIR_LEFT_9		= 16;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 9ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		public static final int  SND_DIR_LEFT_7		= 17;	// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 7ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?

	public static final int  SND_DIR_ROUNDABOUT_0	= 18;	
	public static final int  SND_DIR_ROUNDABOUT_1	= 19;
	public static final int  SND_DIR_ROUNDABOUT_3	= 20;
	public static final int  SND_DIR_ROUNDABOUT_5	= 21;
	public static final int  SND_DIR_ROUNDABOUT_6	= 22;
	public static final int  SND_DIR_ROUNDABOUT_8	= 23;
	public static final int  SND_DIR_ROUNDABOUT_9	= 24;
	public static final int  SND_DIR_ROUNDABOUT_11	= 25;

	// sound dir codes
		public static final int  SND_DIR_L_SIDE			= 26; //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
		public static final int  SND_DIR_R_SIDE			= 27; //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		public static final int  SND_DIR_M_SIDE			= 28; //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½??ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		
		public static final int	 SND_DIR_MAX			= 29;

		public static final int	 SND_DIR_ACT_AFTER_RTURN	= 31; //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿? 
		public static final int	 SND_DIR_ACT_AFTER_LTURN	= 32;
		public static final int	 SND_DIR_RACT_AFTER_RTURN	= 33;
		public static final int	 SND_DIR_LACT_AFTER_RTURN	= 34;
		public static final int	 SND_DIR_RACT_AFTER_LTURN	= 35;
		public static final int	 SND_DIR_LACT_AFTER_LTURN	= 36;

	public static final int	 SND_DIR_ACT_AFTER_RCHANGE	= 37;
	public static final int	 SND_DIR_ACT_AFTER_LCHANGE	= 38;
	public static final int	 SND_DIR_RCHANGE			= 39;
	public static final int	 SND_DIR_LCHANGE			= 40;

	public static final int  SND_INFO_TO_OVERPASS		 = 1;
//	public static final int  SND_INFO_NOT_TO_OVERPASS	 = 2;
	public static final int  SND_INFO_TO_UNDERPASS		 = 3;
//	public static final int  SND_INFO_NOT_TO_UNDERPASS	 = 4;
	public static final int  SND_INFO_ENTER_HIWAY		 = 5;
	public static final int  SND_INFO_EXIT_HIWAY		 = 6;

	public static final int  SND_INFO_MID_OK			 = 7;
	public static final int  SND_INFO_DEST_OK			 = 8;

	public static final int  SND_INFO_TO_TUNNEL			 = 9;
//	public static final int  SND_INFO_NOT_TO_TUNNEL		= 10;
	public static final int  SND_INFO_GO_STRAIGHT		= 11;
	public static final int  SND_INFO_HIGHWAY_DIR_IC	= 12;  // SND_DIR = HIWAY SOUND NO
	public static final int  SND_INFO_HIGHWAY_DIR_JC	= 13;   
	public static final int	 SND_INFO_ENTER_ROTARI		= 14;
	public static final int  SND_INFO_TOLL				= 15;

	public static final int  SND_INFO_ROUNDABOUT_U		= 16;
	public static final int  SND_INFO_ROUNDABOUT_1		= 17;
	public static final int  SND_INFO_ROUNDABOUT_2		= 18;
	public static final int  SND_INFO_ROUNDABOUT_3		= 19;
	public static final int  SND_INFO_ROUNDABOUT_4		= 20;
	public static final int  SND_INFO_ROUNDABOUT_5		= 21;
	//
	public static final int  SND_INFO_VMID_OK			= 22;
	public static final int	 SND_INFO_FERRY				= 23;
	
	// new
	public static final int	 SND_INFO_LANE_FAST			= 24;	// to fast lane
	public static final int	 SND_INFO_LANE_SLOW			= 25;	// to slow lane
	public static final int	 SND_INFO_ENTER_EXPRESS		= 26;	// enter expressway
	public static final int	 SND_INFO_EXIT_EXPRESS		= 27;	// exit expressway
	public static final int	 SND_INFO_ENTER_BRIDGE		= 28;	// enter bridge

	public static final int	 SND_INFO_ENTER_IC			= 29;	// enter IC
	public static final int	 SND_INFO_ENTER_RAMP		= 30;	// enter Ramp

	// NDB
	public static final int	 NDB_RES_NONE				= -1;
	public static final int	 NDB_RES_POI				= 0;	// POI RESULT auto detect 
	public static final int	 NDB_RES_KIND_NAME			= 1;	// Category result
	public static final int	 NDB_RES_ADDR_ADMIN			= 2;	// city & district of address
	public static final int	 NDB_RES_ADDR				= 3;		// road name of address
	public static final int	 NDB_RES_INTERS				= 4;		// intersection
	public static final int	 NDB_RES_TEL				= 5;		// telephone
	public static final int	 NDB_RES_UBCODE				= 6;		// ubcode
	public static final int	 NDB_RES_POI_BODY			= 7;	// POI body	
	public static final int	 NDB_RES_POI_NEAR_BY		= 8;	// POI near by	
	public static final int	 NDB_RES_POI_KIND			= 9;	// POI category
	
	public static final int	 NDB_SEARCH_METHOD_BY_KEYWORD	= 0;		
	public static final int	 NDB_SEARCH_METHOD_BY_NAME		= 1;
	public static final int	 NDB_SEARCH_METHOD_BY_TEL		= 2;
	public static final int	 NDB_SEARCH_METHOD_BY_UBCODE	= 3;
	public static final int	 NDB_SEARCH_METHOD_BY_INTERS	= 4;
	public static final int	 NDB_SEARCH_METHOD_BY_KIND_NAME	= 5;
	public static final int	 NDB_SEARCH_METHOD_BY_KIND		= 6;
	public static final int	 NDB_SEARCH_METHOD_BY_ADDR		= 7;
	public static final int	 NDB_SEARCH_METHOD_BY_NEAR_BY	= 8;
	public static final int	 NDB_SEARCH_METHOD_BY_HIGHWAY_FACILITY	= 9;
	
	public static final int	 POI_MS_INFO_RELATION_ENTRY_EXIT = 1;
	public static final int	 POI_MS_INFO_RELATION_PARKING_LOT = 2;
	public static final int	 POI_MS_INFO_RELATION_FACILITY = 3;
	public static final int	 POI_MS_INFO_RELATION_STORE = 4;
	// ~NDB
	
	
	public static final int RPP_END = 0;	// set destination
	public static final int RPP_START = 1;	// set start
	public static final int RPP_MID_ALL = 2;// use SYS_ResetRPPos only.
	public static final int RPP_MID = 3;	// set via
	public static final int RPP_MID2 = RPP_MID+1;	// via point has RPP_MID+??
	
	// Setting
	// for setting vehicle image
	public static final int DISPLAY_VEHICLE_CAR         = 0; // Car
	public static final int DISPLAY_VEHICLE_SCOOTER     = 1; // Scooter
	public static final int DISPLAY_VEHICLE_BIGBIKE     = 2; // Big Bike
	public static final int DISPLAY_VEHICLE_TRUCK       = 3; // Truck
	public static final int DISPLAY_VEHICLE_BIGTRUCK    = 4; // Big Truck
	public static final int DISPLAY_VEHICLE_TYPE_COUNT  = 5; // Vehicle type count

	// for display ext infomation
	public static final int SETTING_EXT_JUNCTIONVIEW    = 0; // Junction View
	public static final int SETTING_EXT_JUNCTIONDRAW    = 1; // Junction Drawing
	public static final int SETTING_EXT_TMC             = 2; // TMC
	public static final int SETTING_EXT_CAMERA          = 3; // Camera
	public static final int SETTING_EXT_SPEED_LIMIT     = 4; // Speed Limit
	public static final int SETTING_EXT_CCTV            = 5; // CCTV

	// for select display or ring
	public static final int SETTING_TYPE_DISPLAY        = 0; // for display
	public static final int SETTING_TYPE_RING           = 1; // for Ring(Sound Alert)

	public static final int SETTING_RING_TYPE_RING      = 1; // for Ring Setting
	public static final int SETTING_RING_TYPE_VOICE     = 2; // for Voice Setting
	public static final int SETTING_RING_TYPE_OFF       = 0; // Turn off ring

	// language for voice prompt
	public static final int VOICE_LANG_MANDARIN         = 0; // Mandarin
	public static final int VOICE_LANG_TAIYU            = 1; // Taiyu
	public static final int VOICE_LANG_HAKKA            = 2; // Hakka
	public static final int VOICE_LANG_ENGLISH          = 3; // English
	//~Setting
	
	// for CPF
	public static final int CPF_TYPE_FIXED				= 'C';	// Fixed Camaera
	public static final int CPF_TYPE_MOVING				= 'M';	// Moving Camaera
	public static final int CPF_TYPE_SIGN				= 30;
	public static final int KWT_CPF_TYPE_DRV_DIST		= 'G';
	
	public static NDB_RESULT g_res_item = null;// TODO ³o­Óªº¥Î³~?
	private static Timer timer_2; // TODO ³o­Óªº¥Î³~?
	public static Mutex m_ndk_lock; // TODO ³o­Óªº¥Î³~?
	
	public static final int AD_INDEX_AIRWAVES = 101 ;
	
	public static int SetTimer4Native(int delay) // TODO ³o­Óªº¥Î³~?
    {
    	
    	if (timer_2 != null)
    	{
    		timer_2.cancel();
    		timer_2.purge();
    		timer_2 = null;
    	}
    	
    	timer_2 = new Timer("4SetTimer");
		try {
			timer_2.schedule(new TimerTask() {

				@Override
				public void run() {
					m_ndk_lock.lock();
					CallTimerHandler();
					m_ndk_lock.unlock();

				}
			}, delay);
		} catch (Exception e) {
			return 0;
		}
    	return 1;
    }
    
    public static void KillTimer4Native()
    {
    	if (timer_2 == null)
    		return;
    	timer_2.cancel();
    	timer_2.purge();
    	
    	timer_2 = null;
    }
    
    // TODO aidl ²¾°Ê¨ì EngineService
    public static ArrayList<Integer> 	mAvoid_Road_Id 	= new ArrayList<Integer>();
    public static ArrayList<IPOINT> 	mAvoid_Position	= new ArrayList<IPOINT>();
    public static ArrayList<BOUND_BOX> 	mAvoid_Area 	= new ArrayList<BOUND_BOX>();
    
    public static void avoid_reset()
    {
    	mAvoid_Road_Id.clear();
    	mAvoid_Position.clear();
    	mAvoid_Area.clear();
    }
    
    public static void avoid_add_road_id(int road_id)
    {
    	mAvoid_Road_Id.add(new Integer(road_id));
    }
    public static void avoid_add_position(int x, int y)
    {
    	mAvoid_Position.add(new IPOINT(x,y));
    }
    public static void avoid_add_area(int xmin, int ymin, int xmax, int ymax)
    {
    	mAvoid_Area.add(new BOUND_BOX(xmin,ymin,xmax,ymax));
    }
    
    // callback variable
	public static final int ENGINE_CALLBACK_ROUTE_DELETE	= 1;
	// ROUTE PLAN
	public static final int ENGINE_CALLBACK_ROUTE_START		= 2;
	public static final int ENGINE_CALLBACK_ROUTE_END		= 3;
	// RE-ROUTE PLAN
	public static final int ENGINE_CALLBACK_RE_ROUTE_START	= 4;
	public static final int ENGINE_CALLBACK_RE_ROUTE_DONE	= 5;
	// SIMULATION
	public static final int ENGINE_CALLBACK_SIMULATION_START = 6;
	public static final int ENGINE_CALLBACK_SIMULATION_END	 = 7;
	// TOUCH DOEN
	public static final int ENGINE_CALLBACK_TOUCH_DOWN_END	= 8;
	public static final int ENGINE_CALLBACK_TOUCH_DOWN_VIA	= 9;

    public static void CallbackFunction(int v)
    {
//    	try{
//    		EngineService.callbackFunction(v);
//    	}catch(RemoteException e){
//    		e.printStackTrace();
//    	}
    }
   
    // TODO À³¸ÓÄÝ©óap code
    public static void setTextViewColorPartial(TextView view, String fullText, String subText, int color)
    {
    	view.setText(fullText, TextView.BufferType.SPANNABLE);
    	Spannable str = (Spannable) view.getText();
    	int i = fullText.indexOf(subText);
    	if(i>= 0)
    		str.setSpan(new ForegroundColorSpan(color), i, i+subText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
	
	
	// Base System
    private static native void CallTimerHandler();
    public static native void setEnginePath(String enginePath,String engineSavePath); // TODO
    
	public static native int UI_InitializeAll(Context context,citus_listener listener,int x, int y);
	
	public static native void SetCallbackClass(String class_name); // set class name like a "kr/co/citus/engine/citus_api"
																	// then jin called UIArriveTarget(int val);
	
	public static native boolean RG_IsEndTouchDown(); // just 1 time can check, check after auto reset
	
	public static native boolean GPS_IsLogging();
	public static native void GPS_LogStart();
	public static native void GPS_LogEnd();
	
	public static native void GPS_Android_SetInfo(int _isFromGPS, int _isActive, double _speed, double _direction, double _longitude, double _latitude, double _HDOP, double _altitude);
    public static native void GPS_Android_datetime(int _y, int _m, int _d, int _h, int _min, int _s);
    public static native void GPS_Android_setSatellite(int isReset, int _isView, int _id, int _elev, int _azimuth, int _snr);
    public static native void GPS_Android_SetActive(int isActive);
    
    public static native int SYS_GetMemInfo();
	public static native int UI_getActiveView();
	public static native int SYS_IsQuit();
	public static native void UI_GotoMap(byte[] name, double x, double y);
	public static native void UI_ZoomOut();
	public static native void UI_ZoomIn();
	public static native void UI_SetZoomInfo(double x, double y);
	public static native void SYS_SetStoragePath(String _path);
	public static native boolean UI_OnTouch(int _msg, int _x, int _y, int _arg);
	public static native void UI_OnRotate(int _msg, double deltaRad);
	public static native void UI_Invalidate();
	public static native void UI_OnTimer();
	public static native void UI_OnSize(int nType, int cx, int cy);
	public static native void UI_OnIdle();
	
	
	// 3D
	public static native void MV3D_InitRendering(int width, int height);
	public static native void MV3D_ExitRendering();
	public static native void MV3D_OnSurfaceCreated(Surface surface);
	public static native void MV3D_OnSurfaceChanged(Surface surface, int width, int height);
	public static native void MV3D_OnSurfaceDestroyed(Surface surface);
	public static native void MV3D_SetPanning(boolean panning);
	
	public static native void SYS_SetSearchPos(int _xx, int _yy, int _roadid_and_se, String _name, String _addr);
	public static native boolean SYS_GetSearchPos(POI_INFO _info);
	public static native void UI_SetMapMoveMode(byte _mode, boolean isDraw, boolean hideMark);
	public static native byte UI_GetMapMoveMode();
	public static native void UI_GotoPOIPoint(int _x, int _y, int _zf, String _name);
	
	// New3
	public static native int UI_CheckStartPos();
	public static native int UI_CheckMidPos();
	public static native int UI_CheckEndPos();
	public static native void UI_SetStartPos(boolean moveTo, boolean isUpdate);
	public static native void UI_SetMidPos(boolean moveTo, boolean bNear, boolean isUpdate);
	public static native void UI_SetEndPos(boolean moveTo, boolean isUpdate);
	public static native boolean UI_GoCurrent();
	
	// New4
	public static native void UI_SetRpIdx(int idx);
	public static native boolean UI_RoutePlan(boolean _isAutoStartPos, boolean _isAutoReRoute, boolean _isSaveTemp, int gRouteSum, WrapInt nErrorCode, boolean checkDist);
	public static native void UI_MapBackup();
	public static native boolean UI_showRouteInfo(int showTime);
	public static native boolean UI_RouteCancel(boolean flushNet);
	public static native void UI_Invalidate(int rc);
	
	// New5
	public static native void RG_GuideRemoveAll();
	//public static native void UI_Redraw();
	public static native void SYS_SetCarPos(double _x, double _y);
	public static native void SYS_SetCarPos_ForSearch(double _x, double _y);
	public static native IPOINT SYS_GetCarPos();
	public static native IPOINT SYS_GetCarPos_ForSearch();
	public static native int ADMIN_GetName(int _centerX, int _centerY, WrapString _adminName);
	public static native boolean SYS_SetRPPos(int RPP_type, int _xx, int _yy, int _roadid_and_se, String _name, boolean autoSave);
	
	// New6
	public static native boolean SYS_ResetRPPos(int RPP_type, boolean isSave);
	public static native boolean SYS_GetEndPos(WrapInt _x, WrapInt _y, WrapString _name);
	public static native boolean SYS_GetStartPos(WrapInt _x, WrapInt _y, WrapString _name);
	public static native boolean SYS_GetMidPos(int _idx, WrapInt _x, WrapInt _y, WrapString _name);
	public static native int SYS_GetMidPosCount();
	public static native boolean MV_GetNearPoiName(int _x, int _y, int delta, WrapString _name, WrapInt _sx, WrapInt _sy, WrapInt _cx, WrapInt _cy);
	 
	public static native void MV3D_SetTrans(double _laydown);//double laydown = 0.9 - ang / 100.0;
	
	// New7 
	public static native int RECENT_GetCount();
	public static native boolean RECENT_GetInfo(int _index, RECENT_DEST_ITEM _info);
	public static native boolean RECENT_Del(int _index);
	public static native boolean RECENT_MoveLast(int _index);
	
	public static native int MARK_GetCount();
	public static native int MARK_IsExist(int gIdx, String name);
	public static native boolean MARK_Add(int _x, int _y, int _symbol, String _name, String _desc, String _tel, int _isHot, int _addrIdx, int groupIdx);
	public static native boolean MARK_GetInfo(int _index, MARK_INFO _info);
	public static native boolean MARK_Del(int _index);
	public static native void SYS_SetSearchPos2(POI_INFO _info);
	
	// New8 	
	public static native int RG_GetPastDist();
	public static native int RG_GetCurrentPart();
	public static native boolean RG_GetGuideInfo(int _idx, RG_GUIDE_INFO info, boolean needNode, boolean needRoadName);
	public static native int RG_GetTotalDist(int idx);
	public static native double RG_GetPartLength(int _idx); 
	public static native int RG_GetSequenceNo();
	public static native boolean RG_IsAble();
	
	public static native int UI_GetJunctionDrawDistance();
	public static native int UI_GetJunctionViewDistance();
	

	public static native boolean TMC_FindInRouteByRoadId(int _road_id, double longitude, double latitude, TMC_ROUTE_INFO info);
	
	
	// New9 
	
	public static native boolean UI_MapRestore(boolean isRedraw);
	public static native void DLG_CLOSE_ALL();
	public static native int RG_GetIndex();
	public static native void RG_GuideRemove(int iIdx);
	public static native boolean UI_GuideStart(boolean playToll);
	public static native boolean UI_GuideDemo();
	public static native boolean RG_SaveTemp();
	public static native void MV_SetDrawRouteAll(boolean iDrawAll);
	public static native void MV_SetGrayMapColor(boolean _flag);
	//public static native void MV_Redraw();
	//public static native boolean MV_DirtyDraw();
	public static native int SYS_SetExtMode(int _ext_mode);
	public static native void UI_DATA_showIndexMap(boolean _flag);
	public static native void UI_Map_onChangeGuideSide();
	public static native void UI_Map_highway_resetPage();
	public static native void SYS_resetRouteInformation();
	public static native void MV_SetZoomInfo(int _x, int _y, int _z);
    public static native double MV_GetRotateAngle();
	public static native void PROJ_KTMtoWGS84(int _x, int _y, WrapDouble _lat, WrapDouble _long);
	
	//New 10
	public static native void changeBottomMode_Search();
	
	// New 11 
	public static native boolean SND_GetVolumeMute(boolean isSystem);
	public static native void SND_SetVolumeMute(boolean isSystem, boolean isMute);
	public static native void SYS_SetUseStairsForOverCross(boolean _bFlag);
	public static native void SYS_SetUseStairsForCrossRail(boolean _bFlag);
	public static native void SYS_SetUseStairsForOverPass(boolean _bFlag);
	public static native void SYS_SetUseStairsForSubWay(boolean _bFlag);
	public static native boolean SYS_GetUseStairsForOverCross();
	public static native boolean SYS_GetUseStairsForCrossRail();
	public static native boolean SYS_GetUseStairsForOverPass();
	public static native boolean SYS_GetUseStairsForSubWay();
	public static native void UI_SetArrived_Time(boolean _enable);
	public static native boolean  UI_IsArrived_Time(); 
	public static native void Wav_SetVolumeIndex(int index);
	
	public static native boolean UI_IsEnabledAutoReroute();
	public static native void UI_EnableAutoReroute(boolean b);

	
	// New 12 Map UI
	public static native void MV_GetZoomInfo(WrapInt _x, WrapInt _y, WrapInt _z);
	public static native boolean SYS_IsCarToCenter();
	
	// New 13 RG Info
	public static native int RG_GetRemainTime();
	public static native int ShiftMapVtc(int _type);
	
	// New 14 
	public static native boolean UI_IsShowUseInfo();
	public static native void UI_SetShowUseInfo(boolean _flag);
	
	// add 
	public static native boolean MV_ZoomOut(int _zoom, boolean _isRaster);
	public static native boolean MV_ZoomIn(int _zoom, boolean _isRaster);
	//public static native void MV_RedrawRaster();
	public static native boolean MV3D_IsVisible();
	public static native void MV_OnPaint();
	
	// RG
	public static native void RG_DemoScroll(int progress, int total);
	public static native void LANE_Reset();
	public static native void CPF_Reset();

	// camera sound on/off	
	public static final int KWT_NET_CAMERA_TYPE_FIXED	 =0;	//Speed cameras - fixed (speed limit) 
	public static final int KWT_NET_CAMERA_TYPE_MOBILE	 =1;	//Speed cameras - mobile (speed limit) 
	public static final int KWT_NET_CAMERA_TYPE_DISTANCE =2;	//Speed cameras - failure to maintain driving distance 
	public static final int KWT_NET_CAMERA_TYPE_RED_LIGHT=3;	//Speed cameras - fixed (red light violation & speed limit) 

	public static native void CAMERA_SetSound(int camera_type, int on_off); // 0:off, other:on
	public static native int CAMERA_IsSound(int camera_type); // 0:off, other:on
	
	// for auto zoom mode
	public static final int AUTO_ZOOM_OFF		= 0; // 
	public static final int AUTO_ZOOM_BY_SPEED	= 1; // 
	public static final int AUTO_ZOOM_BY_GUIDE  = 2; // 
	
	public static native int  SYS_GetAutoZoom(); // return AUTO_ZOOM_xxx mode value
	public static native void SYS_SetAutoZoom(int mode); // input AUTO_ZOOM_xxx mode value
	public static native void SYS_SetAutoZoomLevel(int[] _level);
	public static native void SYS_PauseAutoLevel(int sec);
	
	public static native void BitmapCopy(Bitmap bitmap); // 2d¥­­±¹Ï
	public static native boolean getJunctionViewBitmap(Bitmap outBitmap, WrapInt outWidth, WrapInt outHeight ,WrapString outJunctionNameId); // ¹ê´º¹Ï	
        public static native void clearJunctionViewNameId();
	public static native boolean BitmapCopy2(Bitmap bitmap, WrapInt width, WrapInt height);
	
	public static native void MV_GetExtViewSize(WrapInt width, WrapInt height);
	public static native void MV_SetNorthUP(boolean northup);
	public static native boolean MV_GetNorthUP();
	
	public static native byte[] GetIconBits(String name, ICON_INFO info);
	public static native void SYS_SetResolution(int resolution);
	
	public static native void MV3D_SetVehicleMark(int type, int idx);
	public static native int  MV3D_GetVehicleMark(int type);
	
	public static native void MV3D_ShowPoiCategory(int cate, boolean show);
	public static native boolean MV3D_IsShowPoiCategory(int cate);
	
	public static native void SYS_SetDrawGuideLine(int opt);
	public static native int SYS_GetGuideLine();
	
	public static native int SYS_SetVoiceLanguageType(int _type);
	public static native int SYS_GetVoiceLanguageType();
	public static native boolean SYS_SetUseNaviInBackground(boolean _use);
	public static native boolean SYS_GetUseNaviInBackground();
	
	public static native int SYS_GetMapColorMode();
	public static native void SYS_SetMapColorMode(int mode);
	public static native int SYS_GetArriveInfoType();
	public static native void SYS_SetArriveInfoTpe(int type);
	public static native boolean SYS_GetShowMiddleStationFirst();
	public static native void SYS_SetShowMiddleStationFirst(boolean midFirst);
	public static native int SYS_GetExtInfo2(int type, int mode);
	public static native void SYS_SetExtInfo(int type, int mode, int doit);
	public static native boolean SYS_GetExtInfo(int type, int mode);
	public static native void SYS_SetExtInfo(int type, int mode, boolean doit);
	public static native int SYS_GetShowRoadLimitRuleRingType();
	public static native void SYS_SetShowRoadLimitRuleRingType(int limit);
	public static native boolean SYS_GetRemindWarningWithCamera();
	public static native void SYS_SetRemindWarningWithCamera(boolean warn);
	public static native boolean SYS_GetUsingRoadSign();
	public static native void SYS_SetUsingRoadSign(boolean use);
	public static native boolean SYS_IsShowLaneInfo();
	public static native void SYS_SetShowLaneInfo(boolean show);
	public static native boolean SYS_IsHighwaySplit();
	public static native void  SYS_SetHighwayMode(boolean use);
	
	public static native boolean MV3D_IsShowBuilding();
	public static native void MV3D_ShowBuilding(boolean show);
	public static native boolean MV3D_IsShowLandmark();
	public static native void MV3D_ShowLandmark(boolean show);
	
	public static native String MV3D_GetPoiOnPick(int sx, int sy); // by screen position
	public static native String SYS_GetRoadNameOnPick(int sx, int sy); // by screen position
	public static native String SYS_GetPositionOnPick(int sx, int sy); // by screen position
	public static native String SYS_GetRoadName(int map_x, int map_y, int find_distance); // by map coordinates
	public static native int SYS_GetRoadId(int map_x, int map_y, int find_distance); // by map coordinates

	public static native String SYS_GetEngineVersion();
	public static native String SYS_GetEngineDate();
	
	public static native void MV3D_AddSearchMark(int idx, int mapX, int mapY, boolean selected);
	public static native void MV3D_ClearSearchMark();
	public static native void MV3D_SelectSearchMark(int idx);
	
	//parking api
	public static native void MV3D_AddParkingLotMark(int idx, int mx, int my, boolean selected);
	public static native void MV3D_ClearParkingLotMark();
	public static native void MV3D_ZoomAllParkingLotMark();
	public static native void MV3D_SelectParkingLotMark(int idx);
	public static native int MV3D_GetParkingLotResultOnPick(int sx, int sy);
	
	// gas
	public static native  void MV3D_AddGasStationMark(int idx, int stationType, int mx, int my, boolean selected);
	public static native  void MV3D_ClearGasStationMark();
	public static native  void MV3D_ZoomAllGasStationMark();
	public static native  void MV3D_SelectGasStationMark(int idx);
	public static native  int MV3D_GetGasStationResultOnPick(int sx, int sy);
	
	// NDB
	public static native boolean	NDB_Open(int _method);
	public static native void		NDB_Close();
	public static native void		NDB_Clear();
	public static native void		NDB_Param_Reset();
	public static native void		NDB_Param_SetName(final String name1, final String name2);
	public static native void		NDB_Param_AddKind(int kind_idx);
	//public static native void		NDB_Param_AddKind(int* kind_idx);
	public static native void		NDB_Param_SetRadius(int x, int y, int radius);
	public static native void		NDB_Param_AddAdmin(int admin_idx);
	public static native int		NDB_Param_AddAdmin(final String admin); 
	public static native void		NDB_Param_SetExactMatch(boolean exact_match); 
	public static native int 		NDB_SearchStart(int _method, int limit_count, int sub_limit_count);
	public static native int 		NDB_SearchStart_Fuzzy(int _method, int limit_count, int sub_limit_count);
	public static native int 		NDB_SearchNext(int res_type, int limit_count);
	public static native int 		NDB_GetMethod();
	public static native int 		NDB_SearchNear(int x, int y, int radius, int _kind_idx, int limit_count);
	public static native int 		NDB_SearchAlongRoute(int x, int y, int dist_from_route, int _kind_idx, int limit_count);
	//public static native int 		NDB_SearchNear(int x, int y, int radius, int *_kind_idx, int _kind_num, int limit_count);
	public static native int		NDB_Result_SetType(int res_type);
	public static native int		NDB_Result_GetType();
	public static native int		NDB_Result_GetCount();
	public static native int		NDB_Result_GetMaxCount();
	public static native boolean	NDB_Result_GetItem(int idx, NDB_RESULT res); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native boolean	NDB_Result_IsSearchDone();
	//public static native int		intult_BuildStringMark(int idx, BYTE* mark_array, int max_array);
	//public static native int		intult_BuildStringMark(NDB_RESULT* res, BYTE* mark_array, int max_array);
	public static native int		NDB_MakeTelSearchString(final String tel);
	public static native int   		NDB_Kind_GetChildNum(int _kind_idx);
	public static native int   		NDB_Kind_GetChildInfo(int _kind_idx, int _seq_idx, NDB_KIND_INFO info);// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int   		NDB_Kind_FindByCode(int coode);
	public static native int   		NDB_Kind_FindByCode(final String code);
	public static native int   		NDB_Kind_GetCode(int _kind_idx);
	public static native boolean 	NDB_Kind_GetInfo(int _kind_idx, NDB_KIND_INFO info); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int   		NDB_Kind_GetName(int _kind_idx, WrapString name1, WrapString name2, WrapString name3, int max_name);
	public static native boolean	NDB_Kind_IsNotUsed(int _kind_code);
	public static native int   		NDB_Admin_GetChildNum(int res_type, int _admin_idx);
	public static native int   		NDB_Admin_GetChildInfo(int res_type, int _admin_idx, int _seq_idx, NDB_ADMIN_INFO info); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int   		NDB_Admin_FindByCode(int res_type, final String code);
	public static native boolean 	NDB_Admin_GetInfo(int res_type, int _admin_idx, NDB_ADMIN_INFO info); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int	  	NDB_Admin_GetName(int res_type, int admin_idx, WrapString name, int max_name);
	public static native int	  	NDB_Admin_GetName(int res_type, int admin_idx, WrapString name1, WrapString name2, WrapString name3, int max_name);
	public static native boolean 	NDB_Admin_IsInputed(int res_type, int depth, int _admin_idx);
	public static native boolean 	NDB_Poi_GetBody(int poi_idx, NDB_POI_BODY_INFO info); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int  		NDB_Poi_GetSlave_count(int poi_idx);
	public static native int  		NDB_Poi_GetSlave(int poi_idx, int seq_idx, WrapInt relation);
	public static native int  		NDB_Poi_GetMultiKindCount(int poi_idx);
	public static native int  		NDB_Poi_GetMultiKind(int poi_idx, int seq_index);
	public static native boolean	NDB_Result_Group_GetItem(int group_idx, int idx, NDB_RESULT res); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native void		NDB_Reult_StringMark(final String poi_str, int ch_seq, int search_len, WrapInt st_ch, WrapInt ed_ch); // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public static native int  		NDB_MakeDistance(int x, int y);
	public static native int  		NDB_MakeDirectionAngle(int x, int y);

	
	public static native void SYS_PauseMoveCenter();
	public static native void MVS_SetScreenSize(int width, int height);
	public static native void MVS_SetZoom(int x, int y);
	public static native void MV_SetCameraNormal();
	public static native void MVR_SetScreenSize(int width, int height);
	public static native void MVR_SetZoomRouteAll();

	public static native void UI_SetRoutePlanOption(int idx, int vehicleType, int methodType, boolean isPay, boolean use_ferry);
	public static native void RG_SetIndex(int idx);
	public static native void MV3D_DrawRouteAll();
	public static native void MV3D_DrawRouteSelected();
	
	public static native int RG_GetTotalTime();
	public static native int RG_GetTotalFare();
	
	public static native int RG_GetForwardHiwayInfoCnt();
	public static native int RG_GetCurrentHiwayInfoIdx();
	public static native boolean RG_GetHiwayInfo(int _idx, RG_HIWAY_INFO _info);
	public static native int RG_GetHiwayInfoNum();
	public static native int RG_GetHiwayInfoDistanceFromCar(int idx);
	public static native boolean RG_IsHiway();
	
	public static native int RG_GetPartNum(int routeIdx);
	public static native void MVR_SetZoom(int x, int y);
	
	public static native int UI_GetDisplayCPF_Dist();
	public static native int UI_GetDisplayCPF_Type();
		public static native int UI_GetDisplayCPF_Limit();

	public static native int GUIDE_Underpass_IsOkay(); // return 0: none, other value is underpass view
	public static native int GUIDE_Underpass_GetNextImage(int display_time_in_ms, Bitmap bitmap, WrapInt width, WrapInt height); // return 0:none, 1:okay, 2:keep use previous
	
	public static native int GUIDE_GetCurrSignpostInfoCount(int _distance_from_ccp);
	public static native void GUIDE_GetCurrSignpostInfoAt(int _idx, SIGNPOST_GUIDEINFO gi);
	
	public static native void PROJ_WGS84toMAP (double latitude, double longitude, WrapInt mx, WrapInt my);
	public static native void PROJ_MAPtoWGS84(int x, int y, WrapDouble lat, WrapDouble lon);
	public static native void MV3D_Scr2Map(int sx, int sy, WrapInt mx, WrapInt my);
	
	public static native void MV3D_OnMouseDown(int sx, int sy);
	public static native void MV3D_OnMouseMove(int sx, int sy);
	public static native void MV3D_OnMouseUp(int sx, int sy);
	public static native int MV3D_GetZf();
	public static native void MV3D_SetZf(int zf);
	public static native void MV3D_WaitRendering(boolean wait);
	public static native void MV_OnScale(float scaleFactor, float adjustFactor);
	public static native int MM_GetCurrentRoadSpeed();
	public static native void MM_GetResult(MAP_MATCH_RESULT_INFO info);
	public static native int MV3D_GetSearchResultOnPick(int sx, int sy);
	public static native void MV3D_BeginRotate(int sx, int sy);
	public static native void MV3D_EndRotate();
	
	public static native int GUIDE_GetEntryRemainDist();
	
	public static native int SYS_CalcDistance(int map_x1, int map_y1, int map_x2, int map_y2);
	public static native int SYS_CalcDistanceWithWGS84(double lon1, double lat1, double lon2, double lat2);
	public static native boolean RG_GetRemainInfo(RG_REMAIN_INFO info);
	public static native void MV3D_SetZoomCenter(int map_x, int map_y, int zf);
	public static native void MV3D_SetScreenCenter(float dx, float dy);
	
	public static native int UI_IsInitOK();
	public static native void MV3D_SetDriveView();
	public static native void MV3D_SetMixView();
	public static native void MV3D_SetStandardView();
	public static native void MV3D_SetNorthup();
	public static native void MV3D_SetHeadingup();
	public static native void MV3D_ZoomAllSearchMark();
	public static native void MV3D_SetDriveViewForZoom();
	public static native void MV3D_SetMixViewForZoom();
	public static native void MV3D_SetStandardViewForZoom();

	public static native int SYS_GetHouseNumber_CurrMMPos(WrapString _road_str, WrapString _hn_str);
	public static native float SYS_SearchRoadMileageNumber_CurrentPos();

	// for ipc
	public static native boolean IPC_HasNearRoad();
	public static native int IPC_FindNearRoad();
	public static native void IPC_Clear();
	public static native void IPC_SetLiveTime(int time_in_seconds);
	public static native void IPC_SetLiveDistance(int dist);
	public static native int IPC_GetCandidateCount();
	public static native int IPC_GetCandidates(MM_IPC_CAND_INFO[] infos, int max_cnt);
	public static native boolean IPC_ChangeRoad(int selected_index);
	
	//public static native long MM_IPC_GetActiveId();
	//public static native int MM_IPC_GetCandidateInfos(MM_IPC_CAND_INFO[] infos, int max_cnt);
	//public static native void MM_IPC_ChangeCandidate(long ipc_cand_id, int mesh_idx, int link_idx);
	public static native String NET_GetRoadName(int mesh_idx, int link_idx);
	
	public static native int RP_AddAvoidanceWithArea(int xmin, int ymin, int xmax, int ymax);
	public static native int RP_AddAvoidanceWithRoadId(int road_id);
	public static native int RP_AddAvoidanceWithNodePosition(int posx, int posy);
	
	// return value: 0: not found, (mesh_idx << 16) + (link_idx)
	public static native int NET_FindLinkIdxByRoadId(int road_id);
	public static native int NET_GetRoadId(int mapIdx, int linkIdx);
	
	public static native boolean SYS_IsUseVoiceGuide();
	public static native void SYS_SetUseVoiceGuide(boolean _useVoice);
	
	public static native boolean SYS_IsStartPos();

	public static native double MV_Scr2MapWidth(int _scr_w);

	public static native int SYS_GetMainTimerInterval();
	public static native boolean MV_GetPickOnMap(int _scr_x, int _scr_y, int _scr_dist, MAP_PICK_INFO info);
	public static native void MV3D_SetPixelPerCentimeter(float dpc);
	public static native int SND_TTS_IsError(int is_message_displayed);
	public static native IPOINT NET_FindLinkPosWithRoadId(int x, int y, int roaid_and_se);
	
	public static native int MV3D_GetTheme();
	public static native void MV3D_SetTheme(int theme);
	
	public static native int SND_TTS_SetVolumn(int vol);
	public static native int SND_TTS_GetVolumn();
	
	public static native void MM_SetCompareHDOPValue(int compare_hdop);
	
	public static native boolean MV3D_IsCanZoomIn();
	public static native boolean MV3D_IsCanZoomOut();
	
	public static native void MV3D_SetDisplayCarMode(boolean displayCarMode);
	public static native short[] SND_GetTTSBuffer(String inputStr);
	public static native int SYS_ReloadSNDData(String dataPath);//«ü©w¹w¿ý­µÀÉ
	
	public static native char[] NDB_Bopomofo_GetChinese(String inputStr);
	public static native char[] NDB_SearchPoiCandidate(String inputStr, int adminIdx);
	public static native char[] NDB_SearchAddressCandidate(String inputStr, int adminIdx);
	
	public static native void MM_SetPlayReRoutingVoice(boolean isPlay);	//³]©w¬O§_¼½³ø­«·s³Wµe¸ô®|»y­µ
	public static native boolean MM_IsPlayReRoutingVoice();
	
	public static native int[] GUIDE_GetCurrentLaneInfo();
	
	public static native void SND_SetCapkey(String capkeyStr);
	
	public static native boolean NET_LoadExtendCamera(String path);
	public static native void NET_CloseExtendCamera();
	
	public static native void MV3D_SetCameraAngle(int viewMode);
	
	public static native void MV3D_SetArrowMark(int type);//0:default ,101:airwaves
	
	public static native int MV3D_GetZFValue3D();
	public static native int MV3D_GetZFValueDrive();
	public static native int MV3D_GetZFValue3DPinch();
	public static native int MV3D_GetZFValueDrivePinch();
	public static native int MV3D_GetDriveZoomCount();
	public static native int MV3D_Get3DZoomCount();
	
	public static native void GUIDE_EnableADNavi(int adNumber,boolean bEnable);
	public static native boolean GUIDE_GetADICON(int adNumber);	
	//Java_kr_co_citus_engine_citus_1api_GUIDE_1GetADICON
}