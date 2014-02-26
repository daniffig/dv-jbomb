package concurrency;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Vector;

import reference.GameEvent;
import reference.JBombRequestResponse;


import network.GameInformation;
import network.GamePlayInformation;
import network.GameServer;
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
				default:
					break;
				
			}
			request = this.receiveRequestFromClient();
		}
	}
	
	public void startGame(){
		this.Game.start();
	}
	
	public void sendQuizQuestion(){
		this.bombTargetPlayer = request.getBombTargetPlayer();
		
		QuizQuestion qq = this.Game.getQuiz().getRandomQuizQuestion();
		
		Vector<String> answers = new Vector<String>();
		for(String a: qq.getAnswers()) answers.add(a);
		
		//mando pregunta
		response = new JBombComunicationObject(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
		response.setQuizQuestion(qq.getQuestion());
		response.setQuizAnswers(answers);
		sendResponseToClient(response);
		//activo bomba
		this.Game.getBomb().activate();
		
		//armo notificación y despiero a todos
		this.EventHandler.setEvent(GameEvent.PLAYER_RECEIVED_QUESTION);
		this.EventHandler.setEventMessage(this.MyPlayer.getName() + " recibió la pregunta");
		this.EventHandler.wakeUpAll();
	}
	
	public void sendBombOwnerNotification(){
		GamePlayer BombOwner = this.Game.getBomb().getCurrentPlayer();
		
		response = new JBombComunicationObject(JBombRequestResponse.BOMB_OWNER_RESPONSE);
		response.setBombOwner(new Player(BombOwner.getId(), BombOwner.getName()));
		
		String flash = (BombOwner.getId().equals(this.MyPlayer.getUID())) ? "Tenes la bomba!": BombOwner.getName() + " tiene la bomba";
		response.setFlash(flash);
		response.setMyPlayer(this.MyPlayer);
		
		this.sendResponseToClient(response);
		
		//si no soy yo el que tiene la bomba el cliente no me va a mandar nada, yo me voy a dormir hasta que haya que notificar algo
		if(!BombOwner.getId().equals(this.MyPlayer.getUID())){
			this.EventHandler.goToSleep();
		    //si me despierto aca es que me van a notificar de algo
			this.handleGameEvent();
		}	
		//si sigo es que tengo la bomba asi que tengo que esperar una respuesta del usuario
	}
	
	public void handleGameEvent(){
		GameEvent event = this.EventHandler.getEvent();
		while(!event.equals(GameEvent.BOMB_OWNER_CHANGED))
		{
			switch(event){
				case PLAYER_RECEIVED_QUESTION:
					response = new JBombComunicationObject(JBombRequestResponse.NOTICE_FLASH);
					response.setFlash(this.EventHandler.getEventMessage());
					this.sendResponseToClient(response);
					this.EventHandler.goToSleep();
					break;
				default:
					break;
			}
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
		this.Game.getLinkageStrategy().link(this.Game.getGamePlayers());
		
		response = new JBombComunicationObject(JBombRequestResponse.ADJACENT_PLAYERS);
		for(GamePlayer gp: this.Game.getGamePlayerById(this.MyPlayer.getUID()).getNeighbours())
			response.addPlayer(new Player(gp.getId(), gp.getName()));
		
		this.sendResponseToClient(response);
		this.EventHandler.startGameBarrier(this);
	}
	
	public void sendPlayerJoinGameNotification(){
		System.out.println("recibi player_added notification");
		response = new JBombComunicationObject(JBombRequestResponse.PLAYER_ADDED);
		response.setGamePlayInformation(this.getGamePlayInformation());
		response.setFlash(this.EventHandler.getEventTriggerer().getName());
	
		this.sendResponseToClient(response);
		this.EventHandler.goToSleep();
	}
	
	public boolean processJoinGameRequest(){		
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
				Integer player_id = RequestedGame.addGamePlayer(new GamePlayer(request.getMyPlayer().getName()));
				if(player_id == -1){
					jbco.setType(JBombRequestResponse.ERROR_FLASH);
					jbco.setFlash("Juego Completo! no se pueden agregar mï¿½s jugadores");
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
					
					System.out.println("nombre del juego " + jbco.getGamePlayInformation().getName());
					System.out.println("player_id " + jbco.getMyPlayer().getUID());
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

	public Player getMyPlayer() {
		return MyPlayer;
	}

	public void setPlayer(Player player) {
		MyPlayer = player;
	}
	
}
