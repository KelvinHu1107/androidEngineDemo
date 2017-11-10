package kr.co.citus.engine;

import java.util.ArrayList;

public class MessageQue 
{
	public ArrayList<MSG_INFO> msg_array = new ArrayList<MSG_INFO>(32);
	public MessageQue() {
	}
	
	public synchronized MSG_INFO get_message(int _time_out) 
	{
		MSG_INFO msg = null;
	
		if (msg_array.size() > 0)
		{
			msg = msg_array.get(0);
			msg_array.remove(0);
			return msg;
		}
		
//		try
//		{
//			this.wait(_time_out);
//			
//			if (msg_array.size() > 0)
//			{
//				msg = msg_array.get(0);
//				msg_array.remove(0);
//				return msg;
//			}
//		}
//		catch(Exception e)
//		{
//			System.out.println(e);
//		}
		return msg;
	}
	
	public synchronized void add_message(MSG_INFO msg)
	{
//		this.notify();
		msg_array.add(msg);
	}	

}

