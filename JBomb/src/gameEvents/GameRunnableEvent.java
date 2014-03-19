package gameEvents;

import network.GameServer;
import network.JBombComunicationObject;
import network.Player;
import reference.JBombRequestResponse;
import concurrency.ClientThread;
import core.GamePlayer;

public class GameRunnableEvent extends AbstractGameEvent {

	@Override
	public void handle(ClientThread ClientThread) {
		System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] voy a enviar informaci�n y adyacentes y toca al barrera de juego"  );
		
		JBombComunicationObject response = new JBombComunicationObject(JBombRequestResponse.GAME_RUNNABLE);
		
		for(GamePlayer gp: ClientThread.getGame().getGamePlayerById(ClientThread.getMyPlayer().getUID()).getNeighbours())
			response.addPlayer(new Player(gp.getId(), gp.getName()));
				
		GameServer.getInstance().refreshGamesTable();
		
		ClientThread.setResponse(response);
		ClientThread.sendResponseToClient();
	}

}
