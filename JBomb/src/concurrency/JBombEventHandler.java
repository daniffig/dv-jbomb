package concurrency;

import java.util.ArrayList;
import java.util.List;

import network.Player;

import reference.GameEvent;
import reference.JBombRequestResponse;


public class JBombEventHandler {
	private int current_barrier_size;
	private int barrier_size;
	private GameEvent event;
	private Player eventTriggerer;
	private String eventMessage;
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
	
	public synchronized void goToSleep()
	{
		try{
			this.wait();
		}catch(InterruptedException e){
			System.out.println("El thread salió del wait() por una interrupción");
		}
	}
	
	public synchronized void joinBarrier(ClientThread ct)
	{
		this.current_barrier_size++;
		this.eventTriggerer = ct.getMyPlayer();
		
		if(this.barrier_size != this.current_barrier_size)
		{
			this.setEvent(GameEvent.PLAYER_JOINED_GAME);
			this.notifyAll();
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			this.setEvent(GameEvent.MAX_PLAYERS_REACHED);
			this.notifyAll();
		}
	}
	
	public synchronized void startGameBarrier(ClientThread ct)
	{
		this.current_barrier_size++;
		
		if(this.barrier_size != this.current_barrier_size)
		{
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			ct.startGame();
			this.setEvent(GameEvent.GAME_STARTED);
			this.notifyAll();
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

	public Player getEventTriggerer() {
		return eventTriggerer;
	}

	public void setEventTriggerer(Player eventTriggerer) {
		this.eventTriggerer = eventTriggerer;
	}
	
	public String getEventMessage(){
		return this.eventMessage;
	}
	
	public void setEventMessage(String m){
		this.eventMessage = m;
	}
}
