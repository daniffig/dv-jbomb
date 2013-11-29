package concurrency;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import network.GameInformation;
import network.GameServer;
import reference.JBombRequestResponse;
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
		
		JBombRequestResponse request = this.receiveRequestFromClient();
		while(!request.equals(JBombRequestResponse.BOMB_DETONATED_RESPONSE))
		{
			switch (request){
			
			case GAMES_INFORMATION_REQUEST:
				this.sendResponseToClient(JBombRequestResponse.GAMES_INFORMATION_RESPONSE);
				this.sendGamesInformation();
				break;
			case JOIN_GAME_REQUEST:
				this.sendResponseToClient(JBombRequestResponse.JOIN_GAME_RESPONSE);
				this.sendJoinGameRequestResponse(this.processJoinGameRequest(this.receiveJoinGameRequest()));
				break;
			case BOMB_DETONATED_RESPONSE:
				continue;
			
			default:
				System.out.println("NO HICE UN CASE PARA ESE REQUEST TODAVIA");
				break;
			}
			
			request = this.receiveRequestFromClient();
		}
		
		this.ClientSocket.close();
		
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
	
	public Vector<String> receiveJoinGameRequest()
	{
		try
		{
			ObjectInputStream inFromClient = new ObjectInputStream(this.ClientSocket.getInputStream());
		
			return (Vector<String>) inFromClient.readObject();
		}
		catch(Exception e)
		{
			System.out.println("Fallo la recepcion de datos del cliente");
			return null;
		}
	}
	
	public String processJoinGameRequest(Vector<String> joinGameRequest)
	{
		String GameName = joinGameRequest.get(0);
		String PlayerName = joinGameRequest.get(1);
		
		Game RequestedGame = GameServer.getInstance().getGameByName(GameName);
		
		String result;
		
		if(RequestedGame == null) result = "El juego requerido no existe";
		else
		{
			if(RequestedGame.existPlayer(PlayerName))
				result = "El nombre de jugador ya existe en el juego " + GameName;
			else
			{
				if(!RequestedGame.addGamePlayer(new GamePlayer(PlayerName)))
					result = "Juego Completo! no se pueden agregar más jugadores";
				else
				{
					this.Game = RequestedGame;
					this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
					this.PlayerName = PlayerName;
					result = "ACCEPTED";
				}
			}
		}
		return result;
	}
	
	public void sendJoinGameRequestResponse(String result)
	{
		try
		{
			DataOutputStream outToClient = new DataOutputStream(this.ClientSocket.getOutputStream());
		
			outToClient.writeBytes(result + '\n');
		}
		catch(IOException e)
		{
			System.out.println("Fallo el envio de datos al cliente");
		}
	}
	
	public void sendResponseToClient(JBombRequestResponse jbrr)
	{
		try
		{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(jbrr);
		}
		catch(Exception e)
		{
			System.out.println("Fallo el envio del response");
		}
	}
	
	public JBombRequestResponse receiveRequestFromClient()
	{
		try
		{
			ObjectInputStream inFromClient = new ObjectInputStream(this.ClientSocket.getInputStream());
		
			return (JBombRequestResponse) inFromClient.readObject();
		}
		catch(Exception e)
		{
			System.out.println("Fallo la recepcion del request del cliente");
			return null;
		}
	}
}
