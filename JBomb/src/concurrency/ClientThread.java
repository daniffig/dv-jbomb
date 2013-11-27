package concurrency;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import core.Game;
import core.GamePlayer;
import core.QuizQuestion;

public class ClientThread implements Runnable {

	private Socket client_socket;
	private JBombEventHandler event_handler;
	private String client_player_name;
	private Game current_game;
	
	public ClientThread(Socket s, Game g, JBombEventHandler eh)
	{
		this.client_socket = s;
		this.event_handler = eh;
		this.current_game = g;
		
		this.event_handler.subscribe(this);
	}
	
	@Override
	public void run() {
		System.out.println("Conexión establecida! Thread # " + Thread.currentThread().getName() + " creado");
		
		this.processJoinGameRequest();
		
		this.sendCurrentGameInformation();
		
		this.event_handler.joinBarrier(this); //esperos a que todos tengan la información del juego, el ultimo inicia el juego.
		
		//pregunto quien tiene la bomba
		String player_with_bomb = this.current_game.getBomb().getCurrentPlayer().getName();
		
		this.sendStringToClient(player_with_bomb);
		
		if(!player_with_bomb.equals(client_player_name))
		{
			//si no soy yo, me voy a dormir
			this.event_handler.waitForMove();
		}
		else
		{
			QuizQuestion qq = this.current_game.getQuiz().getRandomQuizQuestion();
			this.sendQuizQuestion(qq);
			String answer = this.receiveStringFromClient();
			if(qq.getCorrectAnswer().equals(answer))
			{
				
			}
			else
			{
				
			}
			//this.event_handler.wakeUpAll();
		}
		
	}
	
	public void startGame()
	{
		this.current_game.start();
	}
	
	public void sendQuizQuestion(QuizQuestion qq)
	{
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.client_socket.getOutputStream());
		
			Vector<String> quiz = new Vector<String>();
			quiz.add(qq.getQuestion());
			
			for(String s : qq.getAnswers())
			{
				quiz.add(s);
			}
			
			outToClient.writeObject(quiz);
		}
		catch(Exception e)
		{
			System.out.println("The QuizQuestion couldn't be send to the client");
		}
	}
	
	public void sendCurrentGameInformation()
	{
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.client_socket.getOutputStream());
		
			ArrayList<String> game_info = new ArrayList<String>();
			game_info.add(this.current_game.getName());
			game_info.add(this.current_game.getMaxGamePlayersAllowed().toString());
			game_info.add(this.current_game.getMaxRounds().toString());
			//Mando vecinos(nombre) en sentido horario comenzando por arriba, si no existe pongo en null.
			
			outToClient.writeObject(game_info);
		}
		catch(Exception e)
		{}
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
			{
				this.client_player_name = player_name;
				this.sendStringToClient("ACCEPTED");
			}
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
		
			outToClient.writeBytes(message + '\n');
		}
		catch(IOException e)
		{
			System.out.println("Fallo el envio de datos al cliente");
		}
	}
	
}
