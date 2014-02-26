package concurrency;

import java.util.ArrayList;
import java.util.List;

import reference.GameEvent;
import reference.JBombRequestResponse;


public class JBombEventHandler {
	private int current_barrier_size;
	private int barrier_size;
	private GameEvent event;
	private int eventTriggererId;
	private List<ClientThread> suscriptors = new ArrayList<ClientThread>();

	public JBombEventHandler(int cant)
	{
		this.barrier_size = cant;
		this.current_barrier_size = 0;
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
	
	public void wakeUpAll()
	{
		this.notifyAll();
	}
	
	public void goToSleep()
	{
		try{
			this.wait();
		}catch(InterruptedException e){
			System.out.println("El thread sali� del wait() por una interrupci�n");
		}
	}
	
	public synchronized void joinBarrier(ClientThread ct)
	{
		this.current_barrier_size++;
		
		if(this.barrier_size != this.current_barrier_size)
		{
			this.setEvent(GameEvent.PLAYER_JOINED_GAME);
			this.eventTriggererId = ct.getPlayerId();
			this.notifyAll();
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			this.setEvent(GameEvent.GAME_STARTED);
			this.notifyAll();
			ct.startGame();
		}
	}
		
	public void setEvent(GameEvent e)
	{
		this.event = e;
	}
	
	public GameEvent getEvent()
	{
		return this.event;
	}

	public int getEventTriggererId() {
		return eventTriggererId;
	}

	public void setEventTriggererId(int eventTriggererId) {
		this.eventTriggererId = eventTriggererId;
	}
	
}
