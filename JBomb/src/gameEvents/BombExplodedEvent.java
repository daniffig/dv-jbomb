package gameEvents;

import reference.JBombRequestResponse;
import network.JBombCommunicationObject;
import concurrency.ClientThread;

public class BombExplodedEvent extends AbstractGameEvent {

	@Override
	public void handle(ClientThread ClientThread) {
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.BOMB_DETONATED_RESPONSE);
		
		response.setPlayers(ClientThread.getGame().getGamePlayersAsPlayers());
		response.setGamePlayInformation(ClientThread.getGame().toGamePlayInformation());
		response.setLoser(ClientThread.getGame().getBomb().getCurrentPlayer().toPlayer());
		
		ClientThread.setResponse(response);
		ClientThread.sendResponseToClient();
	}
}
