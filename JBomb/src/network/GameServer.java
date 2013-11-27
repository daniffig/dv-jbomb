package network;


import java.io.IOException;
import java.net.ServerSocket;

import concurrency.ClientThread;

public class GameServer {

	public static void main(String[] args)
	{
		ServerSocket server = null;
		
		try
		{ 
			server = new ServerSocket(6621);
		} 
		catch (IOException e)
		{
			System.out.println("No fue posible utilizar el puerto 6621");
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
				}
				catch (IOException e)
				{
					System.out.println("Fallo acept() en puerto 6621");
					System.exit(-1);
				}
			}
			else break;
		}
	}

}
