package kr.co.citus.engine;

public interface citus_listener {
	public int SNDplayer_GetVolume();
	public void SNDplayer_SetVolume(int value);
	public void SNDplayer_Play(String nSndFile);
	public boolean SNDfileLoad(String SNDfile);
	public boolean RouteSearchFinished();
	
}
