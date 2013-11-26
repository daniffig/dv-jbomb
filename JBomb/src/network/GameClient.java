package network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import annotations.Server;

@Server("conf.properties.txt")
public class GameClient {

	public String server_ip;
	public String server_port;
	
	public static void main(String[] args) {
		
		GameClient game_client = new GameClient();
		
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
				if(key.equals("ipServer"))
					game_client.server_ip = scanner.next();
				if(key.equals("portServer"))
					game_client.server_port = scanner.next();
			}
			scanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println("El archivo especificado en la anotacion @server() no encontrado");
			System.exit(-1);
		}
		
		System.out.println("Servidor " + game_client.server_ip + ":" + game_client.server_port);
	}

}
