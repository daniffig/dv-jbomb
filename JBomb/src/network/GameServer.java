package network;

import view.*;

import java.io.IOException;
import java.net.ServerSocket;

import concurrency.ClientThread;
import concurrency.JBombEventHandler;
import core.Game;

public class GameServer {

	private Game current_game;
	private JBombServerMainView JBombServerMainView;
	
	public GameServer()
	{
		current_game = new Game();
		current_game.setInetPort(4321);
		current_game.setName("JBomb!");
		current_game.setMaxRounds(1);
		current_game.setMaxGamePlayersAllowed(2);
	}
	
	public GameServer(Game Game, JBombServerMainView JBombServerMainView)
	{
		this.current_game = Game;
		this.JBombServerMainView = JBombServerMainView;
	}
	
	public Game getGame()
	{
		return this.current_game;
	}
	
	public void setGame(Game g)
	{
		this.current_game = g;
	}
	
	public void listen()
	{
		JBombEventHandler event_handler = new JBombEventHandler(this.getGame().getMaxGamePlayersAllowed());
		ServerSocket server = null;
		System.out.println(this.getGame().getInetPort());
		
		try
		{ 
			server = new ServerSocket(4321);
		} 
		catch (IOException e)
		{
			System.out.println("No fue posible utilizar el puerto " + this.getGame().getInetPort());
			
			System.exit(-1);
		}
		
		//while(game_server.getGame().canAddPlayer())
		while(true)
		{
			if(server != null)
			{
				try
				{
					ClientThread stp = new ClientThread(server.accept(), this.current_game, event_handler);
					Thread t = new Thread(stp);
					t.start();
					
					this.JBombServerMainView.refresh();
				}
				catch (IOException e)
				{
					System.out.println("Fallo acept() en puerto " + this.getGame().getInetPort());
					
					System.exit(-1);
				}
			}
			else break;
		}
		
	}
	
	public static void main(String[] args)
	{
		GameServer game_server = new GameServer();
		JBombEventHandler event_handler = new JBombEventHandler(game_server.getGame().getMaxGamePlayersAllowed());
		ServerSocket server = null;
		
		try
		{ 
			server = new ServerSocket(game_server.getGame().getInetPort());
		} 
		catch (IOException e)
		{
			System.out.println("No fue posible utilizar el puerto 4321");
			System.exit(-1);
		}
		
		//while(game_server.getGame().canAddPlayer())
		while(true)
		{
			if(server != null)
			{
				try
				{
					ClientThread stp = new ClientThread(server.accept(), game_server.current_game, event_handler);
					Thread t = new Thread(stp);
					t.start();
				}
				catch (IOException e)
				{
					System.out.println("Fallo acept() en puerto 4321");
					System.exit(-1);
				}
			}
			else break;
		}
	}

}
