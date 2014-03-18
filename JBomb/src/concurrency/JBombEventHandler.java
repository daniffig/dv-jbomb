package concurrency;

import java.util.ArrayList;
import java.util.List;

import network.Player;

import reference.GameEvent;



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
	
	public synchronized void wakeUpAll()
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
		this.setEventTriggerer(ct.getMyPlayer());
		
		if(this.barrier_size != this.current_barrier_size)
		{
			if(this.current_barrier_size==1) ct.changeGameToWaitingState();
			
			this.setEvent(GameEvent.PLAYER_JOINED_GAME);
			this.notifyAll();
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			ct.configureAdjacentPlayersGraph();
			this.setEvent(GameEvent.GAME_RUNNABLE);
			this.notifyAll();
		}
	}
	
	public synchronized void startGameBarrier(ClientThread clientThread)
	{
		this.current_barrier_size++;
		
		if(this.barrier_size != this.current_barrier_size)
		{
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			this.setEvent(GameEvent.GAME_STARTED);
			clientThread.startGame();
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
