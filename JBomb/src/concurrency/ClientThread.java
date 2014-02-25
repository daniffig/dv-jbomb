package concurrency;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import reference.JBombRequestResponse;


import network.GameInformation;
import network.GamePlayInformation;
import network.GameServer;
import network.JBombComunicationObject;

import core.Game;
import core.GamePlayer;

public class ClientThread implements Runnable {

	private Socket ClientSocket;
	private JBombEventHandler EventHandler;
	private Game Game;
	private Integer PlayerId;
	
	private JBombComunicationObject request;
	private JBombComunicationObject response;
	
	private Integer CurrentQuestionAnswer;
	
	public ClientThread(Socket s)
	{
		this.ClientSocket = s;
	}
	
	@Override
	public void run() {
		System.out.println("Conexion establecida! Thread # " + Thread.currentThread().getName() + " creado");
		
		request = this.receiveRequestFromClient();
		while(!request.getType().equals(JBombRequestResponse.FINISH_CONNECTION_REQUEST))
		{
			switch(request.getType()){
				case GAME_LIST_REQUEST:
					System.out.println("I received a game list request from the client");
					this.sendGameListInformation();
				break;
				case JOIN_GAME_REQUEST:
					System.out.println("I received a game join request from the client");
					this.processJoinGameRequest();
				default:
				break;
				
			}
			
			request = this.receiveRequestFromClient();
		}
		
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
						  this.sendRsponseToClient(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
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
	
	
	
	
	public void processJoinGameRequest()
	{		
		JBombComunicationObject jbco = new JBombComunicationObject();
		try
		{			
			Game RequestedGame = GameServer.getInstance().getGameById(request.getRequestedGameId());
			
			if(RequestedGame == null)
			{	
				jbco.setType(JBombRequestResponse.ERROR_FLASH);
				jbco.setFlash("El juego requerido no existe");
			}
			else
			{
				Integer player_id = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayerName()));
				if(player_id.equals(-1)){
					jbco.setType(JBombRequestResponse.ERROR_FLASH);
					jbco.setFlash("Juego Completo! no se pueden agregar m�s jugadores");
				}
				else{
					this.Game = RequestedGame;
					this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
					this.PlayerId = player_id;
						
					GameServer.getInstance().refreshGamesTable();
					
					//esto es lo que voy a enviarle al chambon
					
					GamePlayInformation gpi = new GamePlayInformation();
					gpi.setId(this.Game.getId());
					gpi.setName(this.Game.getName());
					gpi.setGamePlayersOverMaxGamePlayers(this.Game.getGamePlayersOverMaxGamePlayers());
					gpi.setCurrentRound(this.Game.getCurrentRound());
					gpi.setMaxRounds(this.Game.getMaxRounds());
					
					
					jbco.setType(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
					jbco.setGamePlayInformation(gpi);
					jbco.setMyPlayerId(this.PlayerId);
				}

			}

			this.sendResponseToClient(jbco);
		}
		catch (Exception e)
		{
			System.out.println("Game Join Process Request FAILED");
		}
	}
	
	//LO NUEVOOOOOO
	public void sendGameListInformation(){
		JBombComunicationObject response = new JBombComunicationObject();
		
		response.setType(JBombRequestResponse.GAME_LIST_RESPONSE);
		
		for(Game g :GameServer.getInstance().getGames())
		{
			GameInformation gi = new GameInformation();
			gi.setId(g.getId());
			gi.setName(g.getName());
			gi.setMode(g.getMode().toString());
			gi.setGamePlayersOverMaxGamePlayers(g.getGamePlayersOverMaxGamePlayers());

			response.addGameInformation(gi);
		}

		this.sendResponseToClient(response);
	}
	
	
	public void sendResponseToClient(JBombComunicationObject jbco){
		try
		{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(jbco);
			
			System.out.println("Envie el response al cliente");
		}
		catch(Exception e)
		{
			System.out.println("Fallo el envio del response");
		}
	}
	
	public JBombComunicationObject receiveRequestFromClient(){
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
