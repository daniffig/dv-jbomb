package gameEvents;

import reference.JBombRequestResponse;
import network.JBombCommunicationObject;
import concurrency.ClientThread;

public class GameFullEvent extends SetupEvent {

	public boolean JoinedToGameSucceded(){
		return false;
	}

	public void handle(ClientThread ClientThread) {
		System.out.println("mando error porque el juego esta completo");
		
		JBombCommunicationObject response = new JBombCommunicationObject();
		
		response.setType(JBombRequestResponse.GAME_FULL_ERROR);
		
		response.setFlash("[GameFullEvent] Juego Completo! no se pueden agregar m�s jugadores");
		
		ClientThread.setResponse(response);
		
		ClientThread.sendResponseToClient();
	}

}
