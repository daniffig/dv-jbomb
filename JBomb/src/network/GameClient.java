package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import annotations.Server;

@Server("conf.properties.txt")
public class GameClient {

	public String username;
	public String server_ip;
	public int server_port;
	private Socket socket;
	
	public GameClient()
	{
		this.readConfigurationFile();
	}
	
	public static void main(String[] args) {

	}
	
	public String joinGame()
	{
		this.connectToServer();
		
		this.sendStringToServer(this.username);
		
		return this.receiveStringFromServer();
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
			System.out.println("Fallo la recepción desde el Servidor.");
			
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
	
	public void connectToServer()
	{
		try	{
			this.socket = new Socket(this.server_ip, this.server_port);
		}catch(IOException e){
			System.out.println("Connection to server failed!");
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
	
	
}
