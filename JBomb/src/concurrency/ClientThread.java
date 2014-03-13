package concurrency;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import reference.GameEvent;
import reference.JBombRequestResponse;
import network.GameInformation;
import network.GamePlayInformation;
import network.GameServer;
import network.GameSettings;
import network.GameSettingsInformation;
import network.JBombComunicationObject;
import network.Player;
import core.Game;
import core.GamePlayer;
import core.QuizQuestion;

public class ClientThread implements Runnable {

	private Socket ClientSocket;
	private JBombEventHandler EventHandler;
	private Game Game;
	private Player MyPlayer;
	
	private JBombComunicationObject request;
	private JBombComunicationObject response;
	
	private Player bombTargetPlayer;
	
	private String CurrentQuestionAnswer;
	
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
				case GAME_SETTINGS_INFORMATION_REQUEST:
					System.out.println("I received a game settings information request from the client.");
					this.sendGameSettingsInformation();
					break;
				case CREATE_GAME_REQUEST:
					System.out.println("I received a create game request from the client.");
					this.createGame();
					break;					
				case GAME_LIST_REQUEST:
					System.out.println("I received a game list request from the client");
					this.sendGameListInformation();
					break;
				case JOIN_GAME_REQUEST:
					System.out.println("I received a game join request from the client");
					if(this.processJoinGameRequest())
					{
						this.EventHandler.joinBarrier(this);
						this.onHoldJoinHandleEvents();
					}
					break;
				case START_GAME_REQUEST:
					this.sendGamePlayersInformation();
					//Si estoy aca es que todos nos despertamos porque comenzo el juego
					this.sendBombOwnerNotification();
					break;
				case SEND_BOMB_REQUEST:
					//recibo a quien quiere mandarle la bomba, mando la pregunta e inicio timer
					this.sendQuizQuestion();
					break;
				case QUIZ_ANSWER_REQUEST:
					//paro el timer, analizo respuesta y repondo bien o mal
					this.processQuizAnswer();
				default:
					break;
				
			}
			request = this.receiveRequestFromClient();
		}
	}
		
	public void processQuizAnswer(){

		this.Game.getBomb().deactivate();
		
		if(request.getSelectedQuizAnswer().equals(this.CurrentQuestionAnswer)){
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando respuesta correcta targetPlayer " + this.bombTargetPlayer.getName() + "[" + this.bombTargetPlayer.getUID() +"]");
			
			this.Game.sendBomb(this.Game.getGamePlayerById(this.MyPlayer.getUID()), this.Game.getGamePlayerById(this.bombTargetPlayer.getUID()));
			
			//mando pregunta
			response = new JBombComunicationObject(JBombRequestResponse.QUIZ_ANSWER_RESPONSE);
			response.setFlash("Respuesta Correcta! :)");
			response.setCorrectAnswer(true);
			this.sendResponseToClient(response);
			
			//armo notificacion y despiero a todos
			this.EventHandler.setEvent(GameEvent.BOMB_OWNER_ANSWER_RIGHT);
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondi� correctamente!");
		}
		else{
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando respuesta mal mi respuesta= " + this.CurrentQuestionAnswer + " y el tiene " + request.getSelectedQuizAnswer());
			response = new JBombComunicationObject(JBombRequestResponse.QUIZ_ANSWER_RESPONSE);
			response.setFlash("Respuesta incorrecta! :C");
			response.setCorrectAnswer(false);
			this.sendResponseToClient(response);
			
			this.EventHandler.setEvent(GameEvent.BOMB_OWNER_ANSWER_WRONG);
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondi� incorrectamente!");	
		}
		
		this.EventHandler.wakeUpAll();
	}
	
	public void sendQuizQuestion(){
		this.bombTargetPlayer = request.getBombTargetPlayer();
		
		QuizQuestion qq = this.Game.getQuiz().getRandomQuizQuestion();
		
		Vector<String> answers = new Vector<String>();
		for(String a: qq.getAnswers()) answers.add(a);
		
		//me guardo al respuesta para despues
		this.CurrentQuestionAnswer = qq.getAnswers().get(qq.getCorrectAnswer());
		
		//mando pregunta
		response = new JBombComunicationObject(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
		response.setFlash("Recibiste una pregunta!");
		response.setQuizQuestion(qq.getQuestion());
		response.setQuizAnswers(answers);
		sendResponseToClient(response);
		//activo bomba
		this.Game.getBomb().activate();
		
		//armo notificaci�n y despiero a todos
		this.EventHandler.setEvent(GameEvent.PLAYER_RECEIVED_QUESTION);
		this.EventHandler.setEventMessage(this.MyPlayer.getName() + " recibi� la pregunta");
		this.EventHandler.wakeUpAll();
	}
	
	public void sendBombOwnerNotification(){
		System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando info de quien tiene la bomba");
		
		GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();
		
		response = new JBombComunicationObject(JBombRequestResponse.BOMB_OWNER_RESPONSE);
		response.setBombOwner(new Player(BombOwner.getId(), BombOwner.getName()));
		
		String flash = (BombOwner.getId().equals(this.MyPlayer.getUID())) ? "Tenes la bomba!": BombOwner.getName() + " tiene la bomba";
		response.setFlash(flash);
		response.setMyPlayer(this.MyPlayer);
		
		this.sendResponseToClient(response);
		
		//si no soy yo el que tiene la bomba el cliente no me va a mandar nada, yo me voy a dormir hasta que haya que notificar algo
		if(!BombOwner.getId().equals(this.MyPlayer.getUID())){
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Me voy a dormir porque no tengo la bomba");
			this.EventHandler.goToSleep();
		    
			this.handleGameEvent();//si me despierto aca es que me van a notificar de algo
		}	
		//si sigo es que tengo la bomba asi que tengo que esperar una respuesta del usuario
	}
	
	public void handleGameEvent(){
		System.out.println("desperte a todos porque hubo un evento");
		GameEvent event = this.EventHandler.getEvent();
		
		mainLoop:
		while(!event.equals(GameEvent.BOMB_OWNER_CHANGED))
		{
			switch(event){
				case PLAYER_RECEIVED_QUESTION:
					System.out.println("Mando flash porque el jugador de la bomba recibio la preguntaaa");
					this.sendNoticeFlash();
					this.EventHandler.goToSleep();
					break;
				case BOMB_OWNER_ANSWER_RIGHT:
					System.out.println("MAndo flash porque el jugador contesto bien");
					this.sendNoticeFlash();
					break mainLoop;
				case BOMB_OWNER_ANSWER_WRONG:
					System.out.println("Mando Flash porque el jugador contesto mal");
					this.sendNoticeFlash();
					this.EventHandler.goToSleep();
					break;
				default:
					break;
			}
			
			event = this.EventHandler.getEvent();
		}
		
		this.sendBombOwnerNotification();
	}
	
	public void onHoldJoinHandleEvents(){
		
		while(!this.EventHandler.getEvent().equals(GameEvent.MAX_PLAYERS_REACHED))
		{
			this.sendPlayerJoinGameNotification();
		}

		this.sendResponseToClient(new JBombComunicationObject(JBombRequestResponse.MAX_PLAYERS_REACHED));
	}
	
	public void sendGamePlayersInformation(){
		System.out.println("[Player Id " + this.MyPlayer.getUID() +"] voy a enviar informaci�n y adyacentes y toca al barrera de juego"  );
		
		response = new JBombComunicationObject(JBombRequestResponse.ADJACENT_PLAYERS);
		
		this.Game.getLinkageStrategy().link(this.Game.getGamePlayers());		
		for(GamePlayer gp: this.Game.getGamePlayerById(this.MyPlayer.getUID()).getNeighbours())
			response.addPlayer(new Player(gp.getId(), gp.getName()));
		
		this.sendResponseToClient(response);
		this.EventHandler.startGameBarrier(this);
	}
	
	public void sendPlayerJoinGameNotification(){
		System.out.println("[Player Id " + this.MyPlayer.getUID() +"]recibi player_added notification");
		
		response = new JBombComunicationObject(JBombRequestResponse.PLAYER_ADDED);
		response.setGamePlayInformation(this.getGamePlayInformation());
		response.setFlash(this.EventHandler.getEventTriggerer().getName());
	
		this.sendResponseToClient(response);
		this.EventHandler.goToSleep();
	}
	
	public boolean processJoinGameRequest(){		
		JBombComunicationObject jbco = new JBombComunicationObject();
		
		try{			
			Game RequestedGame = GameServer.getInstance().getGameById(request.getRequestedGameId());

			if(RequestedGame.equals(null)){	
				
				jbco.setType(JBombRequestResponse.ERROR_FLASH);
				jbco.setFlash("El juego requerido no existe");
				System.out.println("mando error porque el juego requerido no existe");
			}
			else{
				Integer player_id = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayer().getName()));
				
				if(player_id == -1){
					jbco.setType(JBombRequestResponse.ERROR_FLASH);
					jbco.setFlash("Juego Completo! no se pueden agregar m�s jugadores");
					System.out.println("mando error porque el juego esta completo");
				}
				else{
					this.Game = RequestedGame;
					this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
					this.MyPlayer = new Player(player_id, request.getMyPlayer().getName());
						
					GameServer.getInstance().refreshGamesTable();
					
					//esto es lo que voy a enviarle al chambon		
					jbco.setType(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
					jbco.setGamePlayInformation(this.getGamePlayInformation());
					jbco.setMyPlayer(this.MyPlayer);
					
					this.sendResponseToClient(jbco);
					return true;
				}
			}
			
			this.sendResponseToClient(jbco);
		}
		catch (Exception e)
		{
			System.out.println("Game Join Process Request FAILED " + e.toString());
		}
		return false;
	}
	
	public void sendGameListInformation(){
		JBombComunicationObject response = new JBombComunicationObject(JBombRequestResponse.GAME_LIST_RESPONSE);
		
		for(Game g :GameServer.getInstance().getGames())
		{
			GameInformation gi = new GameInformation();
			gi.setUID(g.getUID());
			gi.setName(g.getName());
			gi.setMode(g.getMode().toString());
			gi.setMaxPlayers(g.getMaxGamePlayersAllowed());
			gi.setTotalPlayers(g.getTotalGamePlayers());

			response.addGameInformation(gi);
		}

		this.sendResponseToClient(response);
	}
	
	public void sendGameSettingsInformation()
	{
		JBombComunicationObject response = new JBombComunicationObject(JBombRequestResponse.GAME_SETTINGS_INFORMATION_RESPONSE);
		
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
		
		this.sendResponseToClient(response);
	}
	
	public void createGame()
	{
		GameSettings gs = this.request.getGameSettings();
		
		Game g = new Game();
		
		g.setName(gs.getName());
		g.setLinkageStrategy(GameServer.getInstance().getAvailableLinkageStrategies().get(gs.getTopologyId()));
		g.setQuiz(GameServer.getInstance().getAvailableQuizzes().get(gs.getQuizId()));
		g.setMode(GameServer.getInstance().getAvailableGameModes().get(gs.getModeId()));
		g.setMaxGamePlayersAllowed(gs.getMaxPlayers());
		g.setMaxRounds(gs.getMaxRounds());
		
		/* 
		 * FIXME
		 */
		g.setRoundDuration(30);		
		
		GameServer.getInstance().addGame(g);
		
		JBombComunicationObject response = new JBombComunicationObject(JBombRequestResponse.CREATE_GAME_RESPONSE);
		
		GameInformation gi = new GameInformation();
		
		gi.setUID(g.getUID());
		gi.setName(g.getName());
		gi.setMaxPlayers(g.getMaxGamePlayersAllowed());
		gi.setTotalPlayers(g.getTotalGamePlayers());
		
		response.addGameInformation(gi);
		
		this.sendResponseToClient(response);
	}
	
	public void sendNoticeFlash(){
		
		response = new JBombComunicationObject(JBombRequestResponse.NOTICE_FLASH);
		response.setFlash(this.EventHandler.getEventMessage());
		this.sendResponseToClient(response);
	}
	
	public GamePlayInformation getGamePlayInformation(){
		
		GamePlayInformation gpi = new GamePlayInformation();
		gpi.setId(this.Game.getUID());
		gpi.setName(this.Game.getName());
		gpi.setMaxPlayers(this.Game.getMaxGamePlayersAllowed());
		gpi.setTotalPlayers(this.Game.getTotalGamePlayers());
		gpi.setCurrentRound(this.Game.getCurrentRound());
		gpi.setMaxRounds(this.Game.getMaxRounds());
		
		return gpi;
	}
	
	public void sendResponseToClient(JBombComunicationObject jbco){
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(this.ClientSocket.getOutputStream());
			
			outToClient.writeObject(jbco);
			
			System.out.println("[Player Id " + this.MyPlayer.getUID() +"] Envie el response al cliente.");
		}catch(Exception e){
			System.out.println("[Player Id " + this.MyPlayer.getUID() +"]Fallo el envio del response");
		}
	}
	
	public JBombComunicationObject receiveRequestFromClient(){
		try{
			ObjectInputStream inFromClient = new ObjectInputStream(this.ClientSocket.getInputStream());
		
			return (JBombComunicationObject) inFromClient.readObject();
		}catch(Exception e){
			System.out.println("Fallo la recepcion del request del cliente");
			
			return null;
		}
	}

	public void startGame(){
		this.Game.start();
	}
	
	public Player getMyPlayer() {
		return MyPlayer;
	}

	public void setPlayer(Player player) {
		MyPlayer = player;
	}
	
}
