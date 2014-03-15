package network;

import view.*;
import gameModes.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import roundDurations.*;
import linkageStrategies.*;
import concurrency.ClientThread;
import concurrency.JBombEventHandler;
import core.Game;
import core.Quiz;

public class GameServer implements Runnable{

	private static GameServer instance;
	
	private Vector<Game> Games = new Vector<Game>();
	private Vector<JBombEventHandler> EventHandlers = new Vector<JBombEventHandler>();
	private JBombServerMainView JBombServerMainView;
	private String InetIPAddress = "127.0.0.1";
	private Integer InetPort = 4321;
	private Integer IncrementalGameId = 1;
	
	private Map<Integer, AbstractLinkageStrategy> availableLinkageStrategies = new HashMap<Integer, AbstractLinkageStrategy>();
	private Map<Integer, Quiz> availableQuizzes = new HashMap<Integer, Quiz>();
	private Map<Integer, AbstractGameMode> availableGameModes  = new HashMap<Integer, AbstractGameMode>();
	private Map<Integer, AbstractRoundDuration> availableRoundDurations = new HashMap<Integer, AbstractRoundDuration>();
	
	public static GameServer buildInstance(JBombServerMainView JBombServerMainView)
	{
		GameServer gs = getInstance();
		
		gs.JBombServerMainView = JBombServerMainView;
		
		instance = gs;
		
		return instance;
	}
		
	public static GameServer getInstance()
	{
		if (instance == null)
		{
			instance = new GameServer();				

			Map<Integer, AbstractLinkageStrategy> topologies = new HashMap<Integer, AbstractLinkageStrategy>();

			topologies.put(topologies.size(), new RingLinkageStrategy());
			topologies.put(topologies.size(), new ConexantLinkageStrategy());

			instance.setAvailableLinkageStrategies(topologies);

			Map<Integer, AbstractGameMode> gameModes = new HashMap<Integer, AbstractGameMode>();

			gameModes.put(gameModes.size(), new NormalGameMode());
			gameModes.put(gameModes.size(), new BouncingGameMode());

			instance.setAvailableGameModes(gameModes);

			Map<Integer, AbstractRoundDuration> roundDurations = new HashMap<Integer, AbstractRoundDuration>();

			roundDurations.put(roundDurations.size(), new ShortRoundDuration());
			roundDurations.put(roundDurations.size(), new NormalRoundDuration());
			roundDurations.put(roundDurations.size(), new LongRoundDuration());

			instance.setAvailableRoundDurations(roundDurations);
		}
		
		return instance;
	}
	
	public GameServer()
	{
	}
	
	public GameServer(JBombServerMainView JBombServerMainView)
	{
		this.JBombServerMainView = JBombServerMainView;
	}
	
	public GameServer(Vector<Game> Games, JBombServerMainView JBombServerMainView)
	{
		this.Games = Games;
		this.JBombServerMainView = JBombServerMainView;
	}
	
	public void run()
	{
		ServerSocket server = null;
		System.out.println("Comienzo de ejecuci�n del JBomb Game Server en el puerto " + this.getInetPort());
		
		try
		{ 
			server = new ServerSocket(this.getInetPort());
		} 
		catch (IOException e)
		{
			System.out.println("No fue posible utilizar el puerto " + this.getInetPort());
			
			System.exit(-1);
		}

		while(true)
		{
			if(server != null)
			{
				try
				{
					ClientThread stp = new ClientThread(server.accept());
					Thread t = new Thread(stp);
					t.start();
					
//					this.JBombServerMainView.refresh();
				}
				catch (IOException e)
				{
					System.out.println("Fallo acept() en puerto " + this.getInetPort());
					
					System.exit(-1);
				}
			}
			else break;
		}
		
	}
	
	public synchronized Vector<Game> getGames()
	{
		return this.Games;
	}
	
	public synchronized void setGames(Vector<Game> gs)
	{
		this.Games = gs;
	}
	
	public synchronized void addGame(Game g)
	{
		g.setUID(this.IncrementalGameId);
		this.IncrementalGameId++;
		
		this.Games.add(g);
		
		this.addEventHandler(new JBombEventHandler(g.getMaxGamePlayersAllowed()));
		
		this.JBombServerMainView.refreshGameTable();
		//this.JBombServerMainView.addGame(g);
	}
	
	public synchronized void removeGame(Game g)
	{
		this.removeEventHandler(this.getEventHandlerOfGame(g));
		this.Games.remove(g);
	}
	
	public synchronized Game getGameByName(String name)
	{
		for(Game g : this.Games)
			if(g.getName().equals(name)) return g;
		
		return null;
	}
	
	public synchronized Game getGameById(Integer Id)
	{
		for(Game g: this.Games)
			if(g.getUID().equals(Id))
				return g;
		return null;
	}
	
	public synchronized Vector<Game> getAvailableGames()
	{
		Vector<Game> games = new Vector<Game>();
		
		for(Game g : this.Games)
			if(g.canAddPlayer()) games.add(g);
		
		return games;
	}
	
	public synchronized Vector<JBombEventHandler> getEventHandlers()
	{
		return this.EventHandlers;
	}
	
	public synchronized void setEventHandlers(Vector<JBombEventHandler> event_handlers)
	{
		this.EventHandlers = event_handlers;
	}
	
	public synchronized void addEventHandler(JBombEventHandler event_handler)
	{
		this.EventHandlers.add(event_handler);
	}
	
	public synchronized void removeEventHandler(JBombEventHandler event_handler)
	{
		this.EventHandlers.remove(event_handler);
	}
	
	public synchronized JBombEventHandler getEventHandlerOfGame(Game g)
	{
		return this.EventHandlers.get(this.Games.indexOf(g));
	}
	
	public void setInetIPAddress(String ip)
	{
		this.InetIPAddress = ip;
	}
	
	public String getInetIPAddress()
	{
		return this.InetIPAddress;
	}
	
	public void setInetPort(Integer port_number)
	{
		this.InetPort = port_number;
	}
	
	public Integer getInetPort()
	{
		return this.InetPort;
	}
	
	public void refreshGamesTable()
	{
		this.JBombServerMainView.refreshGameTable();
	}

	public Map<Integer, AbstractLinkageStrategy> getAvailableLinkageStrategies() {
		return availableLinkageStrategies;
	}

	public void setAvailableLinkageStrategies(
			Map<Integer, AbstractLinkageStrategy> availableLinkageStrategies) {
		this.availableLinkageStrategies = availableLinkageStrategies;
	}

	public Map<Integer, Quiz> getAvailableQuizzes() {
		return availableQuizzes;
	}

	public void setAvailableQuizzes(Map<Integer, Quiz> availableQuizzes) {
		this.availableQuizzes = availableQuizzes;
	}

	public Map<Integer, AbstractGameMode> getAvailableGameModes() {
		return availableGameModes;
	}

	public void setAvailableGameModes(
			Map<Integer, AbstractGameMode> availableGameModes) {
		this.availableGameModes = availableGameModes;
	}

	public Map<Integer, AbstractRoundDuration> getAvailableRoundDurations() {
		return availableRoundDurations;
	}

	public void setAvailableRoundDurations(
			Map<Integer, AbstractRoundDuration> availableRoundDurations) {
		this.availableRoundDurations = availableRoundDurations;
	}

}
