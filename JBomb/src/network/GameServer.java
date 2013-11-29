package network;

import view.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

import concurrency.ClientThread;
import concurrency.JBombEventHandler;
import core.Game;

public class GameServer implements Runnable{

	private static GameServer instance;
	
	private Vector<Game> Games = new Vector<Game>();
	private Vector<JBombEventHandler> EventHandlers = new Vector<JBombEventHandler>();
	private JBombServerMainView JBombServerMainView;
	private String InetIPAddress = "127.0.0.1";
	private Integer InetPort = 4321;
		
	public static GameServer getInstance()
	{
		if(instance.equals(null)) instance = new GameServer();
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
					
					this.JBombServerMainView.refresh();
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
		this.addEventHandler(new JBombEventHandler(g.getMaxGamePlayersAllowed()));
		this.Games.add(g);
	}
	
	public synchronized void removeGame(Game g)
	{
		this.removeEventHandler(this.getEventHandlerOfGame(g));
		this.Games.remove(g);
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
}
