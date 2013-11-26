package network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import annotations.Server;

@Server("conf.properties.txt")
public class GameClient {

	public String server_ip;
	public String server_port;
	
	public GameClient()
	{
		this.readConfigurationFile();
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
				else if(key.equals("portServer"))this.server_port = scanner.next();
			}
			scanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Archivo especificado en la anotacion @server() no encontrado");
			System.exit(-1);
		}
		
		System.out.println("Servidor " + this.server_ip + ":" + this.server_port);
	}
	
	public static void main(String[] args) {

	}

}
