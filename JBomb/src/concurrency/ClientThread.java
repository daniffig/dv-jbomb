package concurrency;


import gameStates.WaitingGameState;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
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

public class ClientThread implements Runnable, Observer {

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
		while(!request.getType().equals(JBombRequestResponse.CLOSE_CONNECTION_REQUEST))
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
					this.EventHandler.startGameBarrier(this);
					//Si estoy aca es que todos nos despertamos porque comenzo el juego
					this.sendBombOwnerNotification();
					break;
				case SEND_BOMB_REQUEST:
					//recibo a quien quiere mandarle la bomba
					this.bombTargetPlayer = request.getBombTargetPlayer();
					if(this.Game.getMode().toString().equals("Rebote"))
					{
						//si el modo es con rebote, cambio el bomb_owner,despierto a todos, aviso del cambio y le mando la pregunta al jugador
						this.sendBombAndNotify();
					}
					else
					{
						//si no es modo con rebote mando la pregunta 
						this.sendQuizQuestion();
					}
					
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
		
	public void sendBombAndNotify()
	{
		this.Game.sendBomb(this.Game.getGamePlayerById(this.MyPlayer.getUID()), this.Game.getGamePlayerById(this.bombTargetPlayer.getUID()));
		
		//armo notificacion y despierto a todos
		this.EventHandler.setEvent(GameEvent.BOMB_OWNER_CHANGED);
		this.EventHandler.wakeUpAll();
		
		//Aviso quien tiene la bomba
		this.sendBombOwnerNotification();
	}
	
	public void processQuizAnswer(){

		this.Game.getBomb().deactivate();
		
		if(request.getSelectedQuizAnswer().equals(this.CurrentQuestionAnswer)){
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando respuesta correcta targetPlayer " + this.bombTargetPlayer.getName() + "[" + this.bombTargetPlayer.getUID() +"]");
			
			this.Game.getGamePlayerById(this.MyPlayer.getUID()).scoreRightAnswer();
			
			//Si no es modo con rebote le mando la bomba al que me habían dicho, sino la bomba vuelve al que me la había tirado
			if(this.Game.getMode().toString().equals("Rebote"))
			{
				this.Game.sendBomb(this.Game.getGamePlayerById(this.MyPlayer.getUID()), this.Game.getBomb().getLastPlayer());
			}
			else
			{
				this.Game.sendBomb(this.Game.getGamePlayerById(this.MyPlayer.getUID()), this.Game.getGamePlayerById(this.bombTargetPlayer.getUID()));
			}
			
			//mando pregunta
			response = new JBombComunicationObject(JBombRequestResponse.QUIZ_ANSWER_RESPONSE);
			response.setFlash("Respuesta Correcta! :)");
			response.setCorrectAnswer(true);
			this.sendResponseToClient(response);
			
			//armo notificacion y despierto a todos
			this.EventHandler.setEvent(GameEvent.BOMB_OWNER_ANSWER_RIGHT);
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondiï¿½ correctamente!");
			this.EventHandler.wakeUpAll();
			this.sendBombOwnerNotification();
		}
		else{
			System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando respuesta mal mi respuesta= " + this.CurrentQuestionAnswer + " y el tiene " + request.getSelectedQuizAnswer());
			this.Game.getGamePlayerById(this.MyPlayer.getUID()).scoreWrongAnswer();
			
			response = new JBombComunicationObject(JBombRequestResponse.QUIZ_ANSWER_RESPONSE);
			response.setFlash("Respuesta incorrecta! :C");
			response.setCorrectAnswer(false);
			this.sendResponseToClient(response);
			
			this.EventHandler.setEvent(GameEvent.BOMB_OWNER_ANSWER_WRONG);
			this.EventHandler.setEventMessage(this.MyPlayer.getName() + " respondiï¿½ incorrectamente!");
			this.EventHandler.wakeUpAll();
		}
	}
	
	public void sendQuizQuestion(){
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
		
		//armo notificaciï¿½n y despiero a todos
		this.EventHandler.setEvent(GameEvent.PLAYER_RECEIVED_QUESTION);
		this.EventHandler.setEventMessage(this.MyPlayer.getName() + " recibiï¿½ la pregunta");
		this.EventHandler.wakeUpAll();
	}
	
	public void sendBombOwnerNotification(){
		System.out.println("[Player ID " + this.MyPlayer.getUID() + "]Mando info de quien tiene la bomba");
		
		GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();
		
		if(this.bombHasBounced())
		{
			//esto sirve para el modo de rebote, si cambio el dueño de la bomba porque el flaco respondió bien y yo soy el dueño de la bomba, tengo que mandar BOMB_REJECTED_RESPONSE
			response = new JBombComunicationObject(JBombRequestResponse.BOMB_REJECTED_RESPONSE);
			response.setBombOwner(new Player(BombOwner.getId(), BombOwner.getName()));
			response.setMyPlayer(this.MyPlayer);
		
			this.sendResponseToClient(response);
		}
		else
		{
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
		
		//Si sali de aca es ó porque el flaco respondió bien así que tengo que notificar a todos quien tiene la bomba
		//o alguien le tiro la bomba a otro porque es modo rebote y en ese caso tambien tengo que notificar y mandar la pregunta
		this.sendBombOwnerNotification();
		if(event.equals(GameEvent.BOMB_OWNER_CHANGED)) this.sendQuizQuestion();
	}
	
	public void onHoldJoinHandleEvents(){
		
		while(!this.EventHandler.getEvent().equals(GameEvent.GAME_RUNNABLE))
		{
			this.sendPlayerJoinGameNotification();
		}

		System.out.println("[Player Id " + this.MyPlayer.getUID() +"] voy a enviar informaciï¿½n y adyacentes y toca al barrera de juego"  );
		response = new JBombComunicationObject(JBombRequestResponse.GAME_RUNNABLE);
		
		for(GamePlayer gp: this.Game.getGamePlayerById(this.MyPlayer.getUID()).getNeighbours())
			response.addPlayer(new Player(gp.getId(), gp.getName()));
				
		GameServer.getInstance().refreshGamesTable();
		
		this.sendResponseToClient(response);
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
			
			System.out.println("Me pidio el juego con ID: " + request.getRequestedGameId());

			if(RequestedGame.equals(null)){	
				
				jbco.setType(JBombRequestResponse.GAME_NOT_FOUND_ERROR);
				jbco.setFlash("El juego requerido no existe");
				System.out.println("mando error porque el juego requerido no existe");
			}
			else{
				Integer player_id = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayer().getName()));
				
				if(player_id == -1){
					jbco.setType(JBombRequestResponse.GAME_FULL_ERROR);
					jbco.setFlash("Juego Completo! no se pueden agregar mï¿½s jugadores");
					System.out.println("mando error porque el juego esta completo");
				}
				else{
					this.Game = RequestedGame;
					this.EventHandler = GameServer.getInstance().getEventHandlerOfGame(RequestedGame);
					this.MyPlayer = new Player(player_id, request.getMyPlayer().getName());
					this.Game.suscribeToBombDetonation(this);	
					
					//esto es lo que voy a enviarle al chambon		
					jbco.setType(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
					jbco.setGamePlayInformation(this.getGamePlayInformation());
					jbco.setMyPlayer(this.MyPlayer);
					
					System.out.println("Hasta acÃ¡ estÃ¡ todo bien.");
					
					GameServer.getInstance().refreshGamesTable();
					
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
		
		System.out.println("Tengo " + GameServer.getInstance().getAvailableGames().size() + " juegos disponibles" );
		
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
		
		System.out.println("EnviÃ© los parÃ¡metros de juego a un cliente.");
	}
	
	public void createGame()
	{
		System.out.println("Alguien quiere crear un juego.");
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
			
			System.out.println("Envie response al cliente");
		}catch(Exception e){
			System.out.println("Fallo el envio del response");
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
	
	public void configureAdjacentPlayersGraph()
	{
		this.Game.configureAdjacentPlayersGraph();
	}
	
	public void changeGameToWaitingState(){
		this.Game.setState(new WaitingGameState());
	}
	
	public Player getMyPlayer() {
		return MyPlayer;
	}

	public void setPlayer(Player player) {
		MyPlayer = player;
	}

	public boolean bombHasBounced()
	{
		return 	  (this.Game.getMode().equals("rebote") 
				&&(this.EventHandler.getEvent().equals(GameEvent.BOMB_OWNER_ANSWER_RIGHT) || this.EventHandler.getEvent().equals(GameEvent.GAME_STARTED))
				&& this.Game.getBomb().getCurrentPlayer().getId().equals(this.MyPlayer.getUID()));
	}
	
	@Override
	public void update(Observable arg0, Object event) 
	{
		GameEvent e = (GameEvent) event;
		if(e.equals(GameEvent.BOMB_EXPLODED)){
			System.out.println("[Player Id " + this.MyPlayer.getUID() +"] Acabo de recibir notificacion de explosion de bomba!");
			
			GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();
					
			this.response = new JBombComunicationObject(JBombRequestResponse.BOMB_DETONATED_RESPONSE);
			this.response.setPlayers(this.Game.getGamePlayersAsPlayers());
			this.response.setGamePlayInformation(this.getGamePlayInformation());
			this.response.setLoser(new Player(BombOwner.getId(), BombOwner.getName()));
		
			this.sendResponseToClient(this.response);
		}
	}
}
