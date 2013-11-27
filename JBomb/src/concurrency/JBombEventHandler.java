package concurrency;

import java.util.ArrayList;
import java.util.List;


public class JBombEventHandler {
	private int current_barrier_size;
	private int barrier_size;
	private String event;
	private String notification;
	private List<ClientThread> suscriptors = new ArrayList<ClientThread>();

	public JBombEventHandler(int cant)
	{
		this.barrier_size = cant;
		this.current_barrier_size =0;
	}
	
	public synchronized void subscribe(ClientThread  ct)
	{
		this.suscriptors.add(ct);
	}
	
	public synchronized void unsubscribe(ClientThread ct)
	{
		this.suscriptors.remove(ct);
	}
	
	public void waitForMove()
	{
	  try
	  {
		  this.wait();
	  }	
	  catch(InterruptedException e)
	  {
		  System.out.println("no pude  hacer un waitForMove porque me vi interrumpido");
	  }
	}
	
	public synchronized void joinBarrier(ClientThread ct)
	{
		this.current_barrier_size++;
		
		if(this.barrier_size != this.current_barrier_size)
		{
			try
			{
				this.wait();
			}
			catch(InterruptedException e)
			{
				System.out.println("El thread salió del wait() por una interrupción");
			}
		}
		else
		{
			this.current_barrier_size = 0;
			this.notifyAll();
			ct.startGame();
		}
	}
	
	public void setNotification(String s)
	{
		this.notification = s;
	}
	
	public String getNotification()
	{
		return this.notification;
	}
	
	public void setEvent(String s)
	{
		this.event = s;
	}
	
	public String getEvent()
	{
		return this.event;
	}
}
