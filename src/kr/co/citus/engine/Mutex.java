package kr.co.citus.engine;


public class Mutex 
{
	boolean m_is_enter=false;
	public Mutex() {
	}
	
	public synchronized boolean set_enter() // true �̸� �����Ѵ�..
	{
		if (m_is_enter) return false;
		m_is_enter = true;
		return true;
	}
	public synchronized void set_leave()
	{
		m_is_enter = false;
	}

	
	public synchronized boolean is_entered() 
	{
		return m_is_enter;
	}
	public synchronized boolean is_locked() 
	{
		return m_is_enter;
	}
	
	public synchronized void lock() 
	{	
		try
		{
			while (m_is_enter != false)
			{
				this.wait(1000);			
			}
			m_is_enter = true;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public synchronized void unlock()
	{
		m_is_enter = false;
		this.notifyAll();
	}	
}

