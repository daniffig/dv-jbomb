package concurrency;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;


import network.GameServer;
import network.JBombComunicationObject;

import core.Game;
import core.GamePlayer;

public class ClientThread implements Runnable {

	private Socket ClientSocket;
	private JBombEventHandler EventHandler;
	private Game Game;
	private String PlayerName;
	private Integer CurrentQuestionAnswer;
	
	public ClientThread(Socket s)
	{
		this.ClientSocket = s;
	}
	
	@Override
	public void run() {
		System.out.println("Conexion establecida! Thread # " + Thread.currentThread().getName() + " creado");
		
		
		/*JBombRequestResponse request = this.receiveRequestFromClient();
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
					this.sendGamePlayInformation();
					//Barrera, el ultimo inicia el juego
					this.EventHandler.joinBarrier(this);
					String BombOwner = this.Game.getBomb().getCurrentPlayer().getName();
					if(!BombOwner.equals(this.PlayerName))
					{
					  //si no tengo la bomba, me duermo 
					  //this.sendResponseToClient(JBombRequestResponse.BOMB_OWNER_RESPONSE);
					  //this.sendBombOwner(BombOwner);
					  this.EventHandler.waitForMove();
					  //si me desperte es o porque exploto la bomba o porque me la pasaron
					  if(this.Game.getBomb().isDetonated())
					  {
						  this.sendResponseToClient(JBombRequestResponse.BOMB_DETONATED_RESPONSE);
						  this.sendBombOwner(this.Game.getBomb().getCurrentPlayer().getName());
					  }
					  else
					  {
						  this.sendResponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
					      this.sendQuizQuestion();
					      this.Game.getBomb().activate();
					  }
					}
					else
					{
					  this.sendResponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
					  this.sendQuizQuestion();
					  this.Game.getBomb().activate();
					}
				}
				break;
			case QUIZ_ANSWER_REQUEST:
				this.Game.getBomb().deactivate();
				Vector<String> QuizAnswer = this.receiveQuizAnswer();
				if(this.Game.getBomb().isDetonated())
				{
				    this.EventHandler.notifyAll();
					this.sendResponseToClient(JBombRequestResponse.BOMB_DETONATED_RESPONSE);
					this.sendBombOwner(this.Game.getBomb().getCurrentPlayer().getName());
				}
				else
				{
				  if(this.CurrentQuestionAnswer.toString() == QuizAnswer.get(0))
				  {
					  if(this.Game.getMode().toString().equals("Rebote"))
					  {
						  //this.Game.sendBomb(yo,this.Game.getBomb.previousUser());
						  //despierto a previousUser()
					  }
					  else
					  {
						  //this.Game.sendBomb(yo,QuizAnswer.get(1));
						  //despierto a get(1)
					  }
				  }
				  else
				  {
					  this.sendResponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
					  this.sendQuizQuestion();
					  this.Game.getBomb().activate();
				  }
				}
			case BOMB_DETONATED_REQUEST:
				continue;
			
			default:
				System.out.println("NO HICE UN CASE PARA ESE REQUEST TODAVIA");
				break;
			}
			
			request = this.receiveRequestFromClient();
		}*/
	}
	
	public void startGame()
	{
		this.Game.start();
	}
	
	
	
	
	public String processJoinGameRequest(Vector<String> joinGameRequest)
	{		
		try
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
						result = "Juego Completo! no se pueden agregar m�s jugadores";
					else
					{
						this.Game = RequestedGame;
						this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
						this.PlayerName = PlayerName;
						result = "ACCEPTED";
						
						GameServer.getInstance().refreshGamesTable();
					}
				}
			}

			return result;
		}
		catch (Exception e)
		{
			return "Game join request failed!";
		}
	}
	
	public void sendResponseToClient(JBombComunicationObject jbco)
	{
		try
		{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(jbco);
		}
		catch(Exception e)
		{
			System.out.println("Fallo el envio del response");
		}
	}
	
	public JBombComunicationObject receiveRequestFromClient()
	{
		try
		{
			ObjectInputStream inFromClient = new ObjectInputStream(this.ClientSocket.getInputStream());
		
			return (JBombComunicationObject) inFromClient.readObject();
		}
		catch(Exception e)
		{
			System.out.println("Fallo la recepcion del request del cliente");
			
			return null;
		}
	}
}
