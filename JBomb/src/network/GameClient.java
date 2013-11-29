package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import reference.AdjacentDirections;
import reference.JBombRequestResponse;
import annotations.Server;

@Server("conf.properties.txt")
public class GameClient {

	public String username;
	public String server_ip;
	public int server_port;
	private Socket socket;
	
	private Vector<GameInformation> GamesInformation = new Vector<GameInformation>();
	/*Esto deberia ser deprecated dentro de poco*/
	public String notification;
	public String gameName;
	public int currentRound;
	public int totalRounds;
	public int totalPlayers;
	public String[] adjacentPlayers = new String[4];
	
	public GameClient()
	{
		this.readConfigurationFile();
		this.connectToServer();
		this.sendRequestToServer(JBombRequestResponse.GAMES_INFORMATION_REQUEST);
		this.receiveGamesInformationFromServer();
	}	
	
	public String joinGame()
	{
		if (this.connectToServer())
		{			
			this.sendStringToServer(this.username);
			
			return this.receiveStringFromServer();			
		}
		
		return "Error!";
	}
	
	public void receiveGameInformation()
	{
		try{
			ObjectInputStream inFromClient = new ObjectInputStream(this.socket.getInputStream());
			ArrayList<String> gameInformation = (ArrayList<String>) inFromClient.readObject();
			
			this.gameName = gameInformation.get(0);
			this.totalPlayers = Integer.parseInt(gameInformation.get(1));
			//this.currentRound = Integer.parseInt(gameInformation.get(2));
			this.totalRounds  = Integer.parseInt(gameInformation.get(2));
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong :S");
		}
	}
	
	public void readConfigurationFile()
	{
		String configuration_file = "";

		if(GameClient.class.getAnnotation(Server.class) != null)
		{
			configuration_file = GameClient.class.getAnnotation(Server.class).value();
		}
		else
		{
			System.out.println("La anotacion @Server() no esta presente!");
			System.exit(-1);
		}
		
		try{
			Scanner scanner = new Scanner(new File(configuration_file));
			String key;
			while(scanner.hasNext())
			{
				key = scanner.next();
				if(key.equals("ipServer")) this.server_ip = scanner.next();
				else if(key.equals("portServer"))this.server_port = Integer.parseInt(scanner.next());
			}
			scanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Archivo especificado en la anotacion @server() no encontrado");
			System.exit(-1);
		}
		
		System.out.println("Servidor " + this.server_ip + ":" + this.server_port);
	}
	
	public String receiveStringFromServer()
	{
		try
		{
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		
			return inFromServer.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Fallo la recepci�n desde el Servidor.");
			
			return null;
		}
	}
	
	public void sendStringToServer(String s)
	{
		try
		{
			DataOutputStream outToServer =	new DataOutputStream(this.socket.getOutputStream());
			outToServer.writeBytes(s + '\n');
		}
		catch(IOException e)
		{
			System.out.println("Fallo el envio de datos");
		}
	}
	
	public Boolean connectToServer()
	{
		try	{
			this.socket = new Socket(this.server_ip, this.server_port);
			
			return true;
		}catch(IOException e){
			System.out.println("Connection to server failed!");
			
			return false;
		}
	}
	
	public void disconnectFromServer()
	{
		try{
			this.socket.close();
		}catch(IOException e){
			System.out.println("Disconnection failed!");
		}
		
	}
	
	public void addPlayer(AdjacentDirections direction, String player)
	{
		this.adjacentPlayers[direction.index()] = player;
	}
	
	public String getPlayer(AdjacentDirections direction)
	{
		return this.adjacentPlayers[direction.index()];
	}
	
	/*NUEVO*/
	
	public void receiveGamesInformationFromServer()
	{
		try
		{
			ObjectInputStream inFromClient = new ObjectInputStream(this.socket.getInputStream());
			this.GamesInformation = (Vector<GameInformation>) inFromClient.readObject();
			
			for (GameInformation gi : this.getGamesInformation())
			{
				System.out.println(gi.getName());
			}
		}
		catch(Exception e)
		{
			System.out.println("Fall� la recepcion de la informacion de juegos");
		}	
	}

	public Vector<GameInformation> getGamesInformation() {
		return GamesInformation;
	}
	
	public void sendRequestToServer(JBombRequestResponse jbrr)
	{
		try
		{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.socket.getOutputStream());
			
			outToClient.writeObject(jbrr);
		}
		catch(Exception e)
		{
			System.out.println("Fallo el envio del request");
		}
	}
	
	public JBombRequestResponse receiveResponseFromServer()
	{
		try
		{
			ObjectInputStream inFromClient = new ObjectInputStream(this.socket.getInputStream());
		
			return (JBombRequestResponse) inFromClient.readObject();
		}
		catch(Exception e)
		{
			System.out.println("Fallo la recepcion del response del server");
			return null;
		}
	}
}
