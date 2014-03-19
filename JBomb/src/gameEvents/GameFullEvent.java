package gameEvents;

import reference.JBombRequestResponse;
import network.JBombComunicationObject;
import concurrency.ClientThread;

public class GameFullEvent extends AbstractGameEvent {

	//gameEvents.AbstractGameEvent

	public void handle(ClientThread ClientThread) {
		System.out.println("mando error porque el juego esta completo");
		
		JBombComunicationObject response = new JBombComunicationObject();
		
		response.setType(JBombRequestResponse.GAME_FULL_ERROR);
		
		response.setFlash("[GameFullEvent] Juego Completo! no se pueden agregar m�s jugadores");
		
		ClientThread.setResponse(response);
		
		ClientThread.sendResponseToClient();
	}

}
