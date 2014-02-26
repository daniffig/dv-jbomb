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
					if(this.processJoinGameRequest())
					{
						this.EventHandler.joinBarrier(this);
						this.handleEvent();
						//El juego empezó, envio la información del juego y me voy a dormir
					}
				default:
				break;
				
			}
			request = this.receiveRequestFromClient();
		}
	}
	
	public void startGame()
	{
		this.Game.start();
	}
	
	
	public void handleEvent()
	{
		switch(this.EventHandler.getEvent())
		{
			case PLAYER_JOINED_GAME:
				this.sendPlayerJoinGameNotification();
			break;
			case GAME_STARTED:
				//Envio información del juego al cliente y me voy a dormir
			break;
			case BOMB_OWNER_CHANGED:
				//Envio información de quien tiene la bomba a todos, si yo tengo la bomba, mando pregunta
			break;
			case BOMB_OWNER_ANSWER_RIGHT:
			break;
			case BOMB_OWNER_ANSWER_WRONG:
			break;
			case BOMB_EXPLODED:
			break;
		}
	}
	
	public void sendPlayerJoinGameNotification()
	{
		JBombComunicationObject jbco = new JBombComunicationObject(JBombRequestResponse.PLAYER_ADDED);
		
		jbco.setFlash(this.Game.getGamePlayerById(this.EventHandler.getEventTriggererId()).getName());
	
		this.sendResponseToClient(jbco);
		this.EventHandler.goToSleep();
	}
	
	public boolean processJoinGameRequest()
	{		
		JBombComunicationObject jbco = new JBombComunicationObject();
		try
		{			
			Game RequestedGame = GameServer.getInstance().getGameById(request.getRequestedGameId());

			if(RequestedGame.equals(null))
			{	
				jbco.setType(JBombRequestResponse.ERROR_FLASH);
				jbco.setFlash("El juego requerido no existe");
				System.out.println("mando error porque el juego requerido no existe");
			}
			else
			{
				Integer player_id = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayerName()));
				if(player_id == -1){
					jbco.setType(JBombRequestResponse.ERROR_FLASH);
					jbco.setFlash("Juego Completo! no se pueden agregar mï¿½s jugadores");
					System.out.println("mando error porque el juego esta completo");
				}
				else{
					this.Game = RequestedGame;
					this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
					this.PlayerId = player_id;
						
					GameServer.getInstance().refreshGamesTable();
					
					//esto es lo que voy a enviarle al chambon
					
					GamePlayInformation gpi = new GamePlayInformation();
					gpi.setId(this.Game.getUID());
					gpi.setName(this.Game.getName());
					gpi.setMaxPlayers(this.Game.getMaxGamePlayersAllowed());
					gpi.setTotalPlayers(this.Game.getTotalGamePlayers());
					gpi.setCurrentRound(this.Game.getCurrentRound());
					gpi.setMaxRounds(this.Game.getMaxRounds());
					
					
					jbco.setType(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
					jbco.setGamePlayInformation(gpi);
					jbco.setMyPlayerId(this.PlayerId);
					
					System.out.println("nombre del juego " + jbco.getGamePlayInformation().getName());
					System.out.println("player_id " + jbco.getMyPlayerId());
					this.sendResponseToClient(jbco);
					return true;
				}
				this.sendResponseToClient(jbco);
			}
		}
		catch (Exception e)
		{
			System.out.println("Game Join Process Request FAILED " + e.toString());
		}
		return false;
	}
	
	public void sendGameListInformation(){
		JBombComunicationObject response = new JBombComunicationObject();
		
		response.setType(JBombRequestResponse.GAME_LIST_RESPONSE);
		
		for(Game g :GameServer.getInstance().getGames())
		{
			GameInformation gi = new GameInformation();
			gi.setUID(g.getUID());
			gi.setName(g.getName());
			gi.setMode(g.getMode().toString());
			gi.setMaxPlayers(g.getMaxGamePlayersAllowed());
			gi.setTotalPlayers(g.getTotalGamePlayers());
			System.out.println("Mande el game con id " + gi.getUID());
			response.addGameInformation(gi);
		}

		for(GameInformation gi: response.getAvailableGames())
		{
			System.out.println("Juego disponible id " + gi.getUID());
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

	public Integer getPlayerId() {
		return PlayerId;
	}

	public void setPlayerId(Integer playerId) {
		PlayerId = playerId;
	}
	
}
