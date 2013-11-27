package network;


import java.io.IOException;
import java.net.ServerSocket;

import concurrency.ClientThread;
import core.Game;

public class GameServer {

	private Game current_game;
	
	public GameServer()
	{
		current_game = new Game();
		current_game.setInetPort(4321);
		current_game.setName("JBomb!");
		current_game.setMaxRounds(1);
		current_game.setMaxGamePlayersAllowed(2);
	}
	
	public Game getGame()
	{
		return this.current_game;
	}
	
	public void setGame(Game g)
	{
		this.current_game = g;
	}
	
	public static void main(String[] args)
	{
		GameServer game_server = new GameServer();
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
		
		while(game_server.getGame().canAddPlayer())
		{
			if(server != null)
			{
				try
				{
					ClientThread stp = new ClientThread(server.accept(), game_server.current_game);
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
