package gameEvents;

import network.GameServer;
import network.Player;
import reference.JBombRequestResponse;
import network.JBombCommunicationObject;
import concurrency.ClientThread;
import core.Game;
import core.GamePlayer;

public class GameJoinedEvent extends AbstractGameEvent {

	private Game RequestedGame;
	private Integer PlayerId;
		
	public GameJoinedEvent(Game RequestedGame, Integer PlayerId)
	{
		this.RequestedGame = RequestedGame;
		this.PlayerId = PlayerId;
	}
	
	@Override
	public void handle(ClientThread ClientThread) {
		
		ClientThread.setGame(RequestedGame);
		
		ClientThread.getGame().suscribeToBombDetonation(ClientThread);	
		
		ClientThread.setEventHandler(GameServer.getInstance().getEventHandlerOfGame(ClientThread.getGame()));
		
		ClientThread.setMyPlayer(new Player(PlayerId, ClientThread.getRequest().getMyPlayer().getName()));
		
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.GAMEPLAY_INFORMATION_RESPONSE);
		
		response.setGamePlayInformation(ClientThread.getGamePlayInformation());
		
		response.setMyPlayer(ClientThread.getMyPlayer());
		
		ClientThread.setResponse(response);
		
		GameServer.getInstance().refreshGamesTable();
		
		System.out.println("[GameJoinedEvent] El Player con ID " + ClientThread.getMyPlayer().getUID() + " se agregó al juego y le mande la información");
		
		ClientThread.sendResponseToClient();
	}

}
