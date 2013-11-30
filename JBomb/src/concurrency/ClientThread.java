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
		while(!request.equals(JBombRequestResponse.BOMB_DETONATED_REQUEST))
		{
			switch (request){
			
			case GAMES_INFORMATION_REQUEST:
				this.sendGamesInformation();
				break;
			case JOIN_GAME_REQUEST:
				String joinGameResult = this.processJoinGameRequest(this.receiveJoinGameRequest());
				
				this.sendResponseToClient(JBombRequestResponse.JOIN_GAME_RESPONSE);
				this.sendJoinGameRequestResponse(joinGameResult);
				
				if(joinGameResult.equals("ACCEPTED"))
				{
					this.sendResponseToClient(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
					//this.sendGamePlayInformation();
					this.EventHandler.joinBarrier(this);
					//if bombOwner==me
					  this.sendResponseToClient(JBombRequestResponse.BOMB_OWNER_RESPONSE);
					  //this.sendBombOwner();
					  //me voy a dormirla
					  //si detono la bomba
					  //  mando bomb_detonated_response
					  //  mando perdedor
					  //else
					  //  this.sendResponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
					  //  this.sendQuizQuestion();
					  //  PRENDER BOMBA
					//else
					  this.sendResponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
					  //this.sendQuizQuestion();
					  //PRENDER BOMBA
				}
				break;
			case QUIZ_ANSWER_REQUEST:
				//APAGAR BOMBA
				//this.receiveQuizAnswer()
				//Si la bomba detono
				//  Despierto a todos
				//  mando BOMB_DETONATED_RESPONSE
				//  mando perdedor
				//else
				  //proceso respuesta 
				  //this.sendResponseToClient(JBombRequestResponse.QUIZ_ANSWER_RESPONSE);
				  //this.sendQuizAnswerResponse()
			//case CHANGE_BOMB_OWNER_REQUEST
				//espero nombre jugador
				//lo despierto y yo me voy a dormir
			case BOMB_DETONATED_REQUEST:
				continue;
			
			default:
				System.out.println("NO HICE UN CASE PARA ESE REQUEST TODAVIA");
				break;
			}
			
			request = this.receiveRequestFromClient();
		}
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
