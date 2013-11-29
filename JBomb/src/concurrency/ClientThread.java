package concurrency;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import network.GameInformation;
import network.GameServer;
import reference.JBombGameEvent;
import core.Game;
import core.GamePlayer;
import core.QuizQuestion;

public class ClientThread implements Runnable {

	private Socket ClientSocket;
	private JBombEventHandler EventHandler;
	private Game Game;
	private String PlayerName;
	
	public ClientThread(Socket s)
	{
		this.ClientSocket = s;
	}
	
	@Override
	public void run() {
		System.out.println("Conexion establecida! Thread # " + Thread.currentThread().getName() + " creado");
		
		this.sendGamesInformation();
		/*this.sendGamesInformation();
		this.processJoinGameRequest();
		
		this.sendCurrentGameInformation();
		
		this.EventHandler.joinBarrier(this); //esperos a que todos tengan la informaciï¿½n del juego, el ultimo inicia el juego.
		
		
		while(!this.Game.getBomb().isDetonated())
		{
			//pregunto quien tiene la bomba
			String player_with_bomb = this.Game.getBomb().getCurrentPlayer().getName();
		
			this.sendStringToClient(player_with_bomb);
		
			if(!player_with_bomb.equals(PlayerName))
			{
				//si no soy yo, me voy a dormir
				this.EventHandler.waitForMove();
			}
			else
			{
				QuizQuestion qq = this.Game.getQuiz().getRandomQuizQuestion();
			
				this.sendQuizQuestion(qq);
				this.Game.getBomb().activate();
				String answer = this.receiveStringFromClient();//TODO falta siguiente jugador
				this.Game.getBomb().deactivate();
			
				if(!this.Game.getBomb().isDetonated())
				{	
					if(qq.getCorrectAnswer().equals(answer))
					{
						this.EventHandler.setEvent(JBombGameEvent.CORRECT_ANSWER);
						//TODO paso la bomba
					}
					else
						this.EventHandler.setEvent(JBombGameEvent.WRONG_ANSWER);
				}
				else
					this.EventHandler.setEvent(JBombGameEvent.BOMB_EXPLODED);
				
				this.EventHandler.wakeUpAll();
			}
		}
		*/
	}
	
	public void startGame()
	{
		this.Game.start();
	}
	
	public void sendQuizQuestion(QuizQuestion qq)
	{
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
		
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
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
		
			ArrayList<String> game_info = new ArrayList<String>();
			game_info.add(this.Game.getName());
			game_info.add(this.Game.getMaxGamePlayersAllowed().toString());
			game_info.add(this.Game.getMaxRounds().toString());
			//TODO Mando vecinos(nombre) en sentido horario comenzando por arriba, si no existe pongo en null.
			
			outToClient.writeObject(game_info);
		}
		catch(Exception e)
		{}
	}
	
	public void processJoinGameRequest()
	{
		String player_name = this.receiveStringFromClient();

		if(this.Game.existPlayer(player_name))
		{
			this.sendStringToClient("El nombre de usuario ya existe en el juego");
		}
		else
		{
			GamePlayer gp = new GamePlayer(player_name);
			
			if(!this.Game.addGamePlayer(gp))
				this.sendStringToClient("Juego completo! no se pueden agregar mas jugadores");
			else
			{
				this.PlayerName = player_name;
				this.sendStringToClient("ACCEPTED");
			}
		}
	}
	
	public String receiveStringFromClient()
	{
		try
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.ClientSocket.getInputStream()));
		
			return inFromClient.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Fallo la recepciï¿½n de datos del cliente");
			return null;
		}
	}
	
	public void sendStringToClient(String message)
	{
		try
		{
			DataOutputStream outToClient = new DataOutputStream(this.ClientSocket.getOutputStream());
		
			outToClient.writeBytes(message + '\n');
		}
		catch(IOException e)
		{
			System.out.println("Fallo el envio de datos al cliente");
		}
	}
	/*NUEVAS COSAS*/
	public Vector<GameInformation> getGamesInformation()
	{		
		Vector<GameInformation> games_information = new Vector<GameInformation>();
		
		for(Game g :GameServer.getInstance().getGames())
		{
			GameInformation gi = new GameInformation();
			gi.setName(g.getName());
			gi.setGamePlayersOverMaxGamePlayers(g.getGamePlayersOverMaxGamePlayers());
			gi.setRoundDuration(g.getRoundDuration());
			gi.setMaxRounds(g.getMaxRounds());
			gi.setGameMode(g.getMode().toString());
			
			games_information.add(gi);
		}
		
		return games_information;
	}
	
	public void sendGamesInformation()
	{
		try
		{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(this.getGamesInformation());
		}
		catch(Exception e)
		{
			System.out.println("Fallo el envio de información de juegos");
		}
	}
}
