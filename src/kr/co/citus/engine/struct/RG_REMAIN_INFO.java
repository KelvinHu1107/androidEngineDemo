package kr.co.citus.engine.struct;

public class RG_REMAIN_INFO {

	public RG_REMAIN_INFO() {
		isToDest = 1;
		dist = 0;
		etaDate = 0;
		etaTime = 0;
		time = 0;
	}
	public int isToDest; // 1:to destination, 0:to mid(VIA) positions
	public int dist; // remain distance in meter
	public int etaDate; // YYYYMMDD
	public int etaTime; // HHMMSS
	public int time; // remain time in minute

}
