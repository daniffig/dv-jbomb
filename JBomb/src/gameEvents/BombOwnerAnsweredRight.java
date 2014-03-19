package gameEvents;

import network.JBombCommunicationObject;
import reference.JBombRequestResponse;
import concurrency.ClientThread;

public class BombOwnerAnsweredRight extends AbstractGameEvent {

	@Override
	public void handle(ClientThread ClientThread) {
		System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] BombOwnerAnsweredRight");
		
		JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.NOTICE_FLASH);
		
		response.setFlash(ClientThread.getEventHandler().getEventMessage());
		
		ClientThread.setResponse(response);
		
		ClientThread.sendResponseToClient();

		ClientThread.sendBombOwnerNotification();
	}

}
