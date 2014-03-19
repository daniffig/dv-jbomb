package concurrency;


import gameEvents.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import reference.JBombRequestResponse;
import network.GameInformation;
import network.GameServer;
import network.GameSettings;
import network.GameSettingsInformation;
import network.JBombCommunicationObject;
import network.Player;
import core.Game;
import core.GamePlayer;

public class ClientThread implements Runnable, Observer {

	private Socket ClientSocket;
	private JBombEventHandler EventHandler;
	private Game Game;
	private Player MyPlayer;
	
	private JBombCommunicationObject request;
	private JBombCommunicationObject response;
	
	public ClientThread(Socket s)
	{
		this.ClientSocket = s;
	}
	
	public JBombCommunicationObject getRequest() {
		return request;
	}

	public void setRequest(JBombCommunicationObject request) {
		this.request = request;
	}

	public JBombCommunicationObject getResponse() {
		return response;
	}

	public void setResponse(JBombCommunicationObject response) {
		this.response = response;
	}

	public Game getGame(){
		return this.Game;
	}
	
	public JBombEventHandler getEventHandler() {
		return EventHandler;
	}

	public void setEventHandler(JBombEventHandler eventHandler) {
		EventHandler = eventHandler;
	}

	public void setGame(Game game) {
		Game = game;
	}

	public void setMyPlayer(Player myPlayer) {
		MyPlayer = myPlayer;
	}

	public Player getMyPlayer() {
		return MyPlayer;
	}

	public void setPlayer(Player player) {
		MyPlayer = player;
	}
	
	@Override
	public void run() {
		System.out.println("Conexion establecida! Thread # " + Thread.currentThread().getName() + " creado");
		
		this.receiveRequestFromClient();
		while(!request.getType().equals(JBombRequestResponse.CLOSE_CONNECTION_REQUEST))
		{
			switch(request.getType()){
				case GAME_SETTINGS_INFORMATION_REQUEST:
					this.sendGameSettingsInformation();
					break;
				case CREATE_GAME_REQUEST:
					this.createGame();
					break;					
				case GAME_LIST_REQUEST:
					this.sendGameListInformation();
					break;
				case JOIN_GAME_REQUEST:
					if(this.processJoinGameRequest())
					{
						this.EventHandler.joinBarrier(this);
						this.handleGameEvents();
					}
					break;
				case START_GAME_REQUEST:
					this.EventHandler.startGameBarrier(this);
					this.sendBombOwnerNotification();
					break;
				case SEND_BOMB_REQUEST:
					this.Game.processNextPlayerRequest(request.getBombTargetPlayer().getUID()).handle(this);
					break;
				case QUIZ_ANSWER_REQUEST:
					this.processQuizAnswer();
					break;
				case START_NEW_ROUND_REQUEST:
					this.EventHandler.joinBarrier(this);
					this.handleGameEvents();
					break;
				case CONNECTION_RESET_REQUEST:
					this.resetConnection();
					break;
				default:
					break;
				
			}
			this.receiveRequestFromClient();
		}
	}
	
	public void processQuizAnswer(){
		
		this.Game.getBomb().deactivate();
		
		if(this.Game.processQuizQuestionAnswer(request.getSelectedQuizAnswer()))
		{
			this.EventHandler.setEvent(new BombOwnerAnsweredRight());
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondi� correctamente!");
			this.EventHandler.wakeUpAll();
			this.sendBombOwnerNotification();
		}
		else
		{
			this.EventHandler.setEvent(new NotifyEvent());
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondi� incorrectamente!");
			this.EventHandler.wakeUpAll();
		}
	}
	
	public void sendBombOwnerNotification(){
		System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando info de quien tiene la bomba");
		
		GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();

		response = new JBombCommunicationObject(JBombRequestResponse.BOMB_OWNER_RESPONSE);
		response.setBombOwner(new Player(BombOwner.getId(), BombOwner.getName()));
		
		String flash = (BombOwner.getId().equals(this.MyPlayer.getUID())) ? "Tenes la bomba!": BombOwner.getName() + " tiene la bomba";
		response.setFlash(flash);
		response.setMyPlayer(this.MyPlayer);
		this.setResponse(response);
		this.sendResponseToClient();
		
		//si no soy yo el que tiene la bomba el cliente no me va a mandar nada, yo me voy a dormir hasta que haya que notificar algo
		if(!BombOwner.getId().equals(this.MyPlayer.getUID())){
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Me voy a dormir porque no tengo la bomba");
			this.EventHandler.goToSleep();
			this.handleGameEvents();
		}	
	}
	
	public void handleGameEvents(){
		 
		while(this.EventHandler.getEvent().isNotification(this))
		{
			this.EventHandler.getEvent().handle(this);
			this.EventHandler.goToSleep();
		}

		this.EventHandler.getEvent().handle(this);
	}
	  
	public boolean processJoinGameRequest(){		
		System.out.println("I received a game join request from the client");
		try{			
			Game RequestedGame = GameServer.getInstance().getGameById(request.getRequestedGameId());
			
			System.out.println("Me pidio el juego con ID: " + request.getRequestedGameId());

			if(RequestedGame.equals(null))
			{	
				System.out.println("mando error porque el juego requerido no existe");
				
				JBombCommunicationObject response = new JBombCommunicationObject();
				response.setType(JBombRequestResponse.GAME_NOT_FOUND_ERROR);
				response.setFlash("El juego requerido no existe");
				
				this.setResponse(response);
				this.sendResponseToClient();
				
				return false;
			}
			else
			{
				SetupEvent GameEvent = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayer().getName()));;
				
				GameEvent.handle(this);	
				
				return GameEvent.JoinedToGameSucceded();
			}
		}
		catch (Exception e)
		{
			System.out.println("Game Join Process Request FAILED " + e.toString());
			return false;
		}
	}
	
	public void sendGameListInformation(){
		System.out.println("Recibi un GAME_LIST_REQUEST del cliente y tengo " + GameServer.getInstance().getAvailableGames().size() + " juegos disponibles");
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.GAME_LIST_RESPONSE);
		
		for(Game g :GameServer.getInstance().getAvailableGames())
		{
			GameInformation gi = new GameInformation();
			gi.setUID(g.getUID());
			gi.setName(g.getName());
			gi.setMode(g.getMode().toString());
			gi.setMaxPlayers(g.getMaxGamePlayersAllowed());
			gi.setTotalPlayers(g.getTotalGamePlayers());

			response.addGameInformation(gi);
		}

		this.setResponse(response);
		this.sendResponseToClient();
	}
	
	public void sendGameSettingsInformation()
	{
		System.out.println("I received a game settings information request from the client.");
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.GAME_SETTINGS_INFORMATION_RESPONSE);
		
		GameSettingsInformation gsi = new GameSettingsInformation();
		
		gsi.setTopologies(new HashMap<Integer, String>());
		
		for (Integer i : GameServer.getInstance().getAvailableLinkageStrategies().keySet())
		{
			gsi.getTopologies().put(i, GameServer.getInstance().getAvailableLinkageStrategies().get(i).toString());
		}
		
		gsi.setQuizzes(new HashMap<Integer, String>());
		
		for (Integer i : GameServer.getInstance().getAvailableQuizzes().keySet())
		{
			gsi.getQuizzes().put(i, GameServer.getInstance().getAvailableQuizzes().get(i).toString());
		}
		
		gsi.setModes(new HashMap<Integer, String>());
		
		for (Integer i : GameServer.getInstance().getAvailableGameModes().keySet())
		{
			gsi.getModes().put(i, GameServer.getInstance().getAvailableGameModes().get(i).toString());
		}
		
		gsi.setMaxPlayersAllowed(16);
		gsi.setMaxRoundsAllowed(7);
		
		gsi.setRoundDurations(new HashMap<Integer, String>());
		
		for (Integer i : GameServer.getInstance().getAvailableRoundDurations().keySet())
		{
			gsi.getRoundDurations().put(i, GameServer.getInstance().getAvailableRoundDurations().get(i).toString());
		}
		
		response.setGameSettingsInformation(gsi);
		
		this.setResponse(response);
		
		this.sendResponseToClient();
		
		System.out.println("Envié los parámetros de juego a un cliente.");
	}
	
	public void createGame()
	{
		System.out.println("I received a create game request from the client.");
		GameSettings gs = this.request.getGameSettings();
		
		Game g = new Game();
		
		g.setName(gs.getName());
		g.setLinkageStrategy(GameServer.getInstance().getAvailableLinkageStrategies().get(gs.getTopologyId()));
		g.setQuiz(GameServer.getInstance().getAvailableQuizzes().get(gs.getQuizId()));
		g.setMode(GameServer.getInstance().getAvailableGameModes().get(gs.getModeId()));
		g.setMaxGamePlayersAllowed(gs.getMaxPlayers());
		g.setMaxRounds(gs.getMaxRounds());
		g.setRoundDuration(GameServer.getInstance().getAvailableRoundDurations().get(gs.getRoundDurationId()));		
		
		GameServer.getInstance().addGame(g);
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.CREATE_GAME_RESPONSE);
		
		GameInformation gi = new GameInformation();
		
		gi.setUID(g.getUID());
		gi.setName(g.getName());
		gi.setMaxPlayers(g.getMaxGamePlayersAllowed());
		gi.setTotalPlayers(g.getTotalGamePlayers());
		
		response.addGameInformation(gi);
		
		this.setResponse(response);
		this.sendResponseToClient();
	}
	
	public void resetConnection(){
		System.out.println("[Player ID " + this.MyPlayer.getUID() + "] Voy a resetear mi conexi�n");
		
		this.EventHandler = null;
		this.Game = null;
		this.MyPlayer = null;
		
		this.request=null;
		this.response=null;
	}
	
	public void sendResponseToClient(){
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(this.response);
			
			System.out.println("Envie response al cliente");
		}catch(Exception e){
			System.out.println("Fallo el envio del response");
		}
	}
	
	public void receiveRequestFromClient(){
		try{
			ObjectInputStream inFromClient = new ObjectInputStream(this.ClientSocket.getInputStream());
		
			this.request = (JBombCommunicationObject) inFromClient.readObject();
		}catch(Exception e){
			System.out.println("Fallo la recepcion del request del cliente");
			
			this.request = null;
		}
	}

	@Override
	public void update(Observable arg0, Object event) 
	{
		reference.BombEvent e = (reference.BombEvent) event;
		
		if(e.equals(reference.BombEvent.BOMB_EXPLODED)){
			System.out.println("[Player Id " + this.MyPlayer.getUID() +"] Acabo de recibir notificacion de explosion de bomba!");
			
			GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();
			
			if(BombOwner.getId().equals(this.MyPlayer.getUID()))
			{
				this.EventHandler.setEvent(new BombExplodedEvent());
				this.EventHandler.wakeUpAll();
				this.EventHandler.getEvent().handle(this);
			}
		}
	}
}
