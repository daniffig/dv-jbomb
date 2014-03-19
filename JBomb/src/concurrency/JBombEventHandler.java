package concurrency;

import gameStates.RunnableGameState;
import gameStates.WaitingGameState;

import java.util.ArrayList;
import java.util.List;

import network.Player;

import gameEvents.*;
import core.Game;


public class JBombEventHandler {
	private Game Game;
	
	private int current_barrier_size;
	private int barrier_size;
	
	private AbstractGameEvent event;
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
			if(this.current_barrier_size==1){
				this.Game = ct.getGame();
				this.Game.setState(new WaitingGameState());
			}
			
			this.setEvent(new PlayerJoinedGameEvent());
			this.notifyAll();
			this.goToSleep();
		}
		else
		{
			this.current_barrier_size = 0;
			this.Game.configureAdjacentPlayersGraph();
			this.Game.setState(new RunnableGameState());
			this.setEvent(new GameRunnableEvent());
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
			this.Game.start();
			this.setEvent(new GameStartedEvent());
			this.notifyAll();
		}
	}
	
	public void setEvent(AbstractGameEvent e)
	{
		this.event = e;
	}
	
	public AbstractGameEvent getEvent()
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
