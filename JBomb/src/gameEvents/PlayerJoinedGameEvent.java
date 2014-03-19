package gameEvents;

import network.GamePlayInformation;
import network.JBombCommunicationObject;
import reference.JBombRequestResponse;
import concurrency.ClientThread;

public class PlayerJoinedGameEvent extends AbstractGameEvent {

	@Override
	public void handle(ClientThread ClientThread) {
		System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] Recibi Player Joined Game Event");
		
		GamePlayInformation gpi = new GamePlayInformation();
		gpi.setId(ClientThread.getGame().getUID());
		gpi.setName(ClientThread.getGame().getName());
		gpi.setMaxPlayers(ClientThread.getGame().getMaxGamePlayersAllowed());
		gpi.setTotalPlayers(ClientThread.getGame().getTotalGamePlayers());
		gpi.setCurrentRound(ClientThread.getGame().getCurrentRound());
		gpi.setMaxRounds(ClientThread.getGame().getMaxRounds());
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.PLAYER_ADDED);
		response.setGamePlayInformation(gpi);
		response.setFlash(ClientThread.getEventHandler().getEventTriggerer().getName());
	
		ClientThread.setResponse(response);
		ClientThread.sendResponseToClient();
	}
	
	@Override
	public boolean isNotification(ClientThread ClientThread){
		return true;
	}
}
