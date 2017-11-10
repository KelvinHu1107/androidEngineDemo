package kr.co.citus.engine;

public class ApiProxy {

	public static final int RG_PAST_DIST         = 1;
	public static final int UI_CURRENT_ROAD_NAME = 2;
	public static final int MV_ROTATE_ANGLE      = 3;
	public static final int SYS_CURRENT_SPEED    = 4;
	public static final int CPF_CAMERA_DISTANCE  = 5;
	public static final int CPF_CAMERA_TYPE      = 6;
	public static final int CPF_CAMERA_LIMIT     = 7;
	public static final int MV_LAYDOWN           = 8;
	public static final int TEST_ROUTE_SEARCH    = 9;
	public static final int SYS_GUIDE_MODE       = 10;
	public static final int GUIDE_STOP_DEMO      = 11;
	public static final int GUIDE_START_DEMO     = 12;
	public static final int SYS_DEMO_SLOWER		 = 13;
	public static final int SYS_DEMO_FASTER      = 14;
	public static final int SYS_DEMO_PAUSE       = 15;
	public static final int SYS_DEMO_STOP        = 16;
	public static final int RG_TOTAL_DIST        = 17;
	public static final int SYS_EXT_MODE         = 18;
	public static final int SYS_CAR_ANGLE        = 19;
	
	
	public static native int getInteger(int code, int defVal);
	public static native float getFloat(int code, float defVal);
	public static native String getString(int code, String defVal);
	
	public static native void setInteger(int code, int val);
	public static native void setFloat(int code, float val);
	public static native void setString(int code, String val);
	
	public static native boolean execute(int code);
}
