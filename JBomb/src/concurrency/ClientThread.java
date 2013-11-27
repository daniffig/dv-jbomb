package concurrency;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import core.Game;
import core.GamePlayer;

public class ClientThread implements Runnable {

	private Socket client_socket;
	private Game current_game;
	
	public ClientThread(Socket s, Game g)
	{
		this.client_socket = s;
		this.current_game = g;
	}
	
	@Override
	public void run() {
		System.out.println("Conexión establecida! Thread # " + Thread.currentThread().getName() + " creado");

	}

	public void processJoinGameRequest()
	{
		String player_name = this.receiveStringFromClient();
		
		if(this.current_game.existPlayer(player_name))
		{
			this.sendStringToClient("El nombre de usuario ya existe en el juego");
		}
		else
		{
			GamePlayer gp = new GamePlayer(player_name);
			
			if(!this.current_game.addGamePlayer(gp))
				this.sendStringToClient("Juego completo! no se pueden agregar mas jugadores");
			else
				this.sendStringToClient("ACCEPTED");
		}
	}
	
	public String receiveStringFromClient()
	{
		try
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
		
			return inFromClient.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Fallo la recepción de datos del cliente");
			return null;
		}
	}
	
	public void sendStringToClient(String message)
	{
		try
		{
			DataOutputStream outToClient = new DataOutputStream(this.client_socket.getOutputStream());
		
			outToClient.writeBytes(message);
		}
		catch(IOException e)
		{
			System.out.println("Fallo el envio de datos al cliente");
		}
	}
	
}
