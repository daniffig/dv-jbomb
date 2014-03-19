package gameEvents;

import java.util.Vector;

import network.JBombCommunicationObject;
import reference.JBombRequestResponse;
import concurrency.ClientThread;
import core.QuizQuestion;

public class BombOwnerChanged extends AbstractGameEvent {
	
	@Override
	public void handle(ClientThread ClientThread) {
		if(this.isNotification(ClientThread))
		{
			System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] BombOwnerChange - Voy a mandar notificacion de cambio de due�o de bomba");
			
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.BOMB_OWNER_RESPONSE);
			
			response.setBombOwner(ClientThread.getGame().getBomb().getCurrentPlayer().toPlayer());
			response.setFlash("Le lanzaron la bomba a " + ClientThread.getGame().getBomb().getCurrentPlayer().getName() + " y recibio la pregunta");
			
			ClientThread.setResponse(response);
			ClientThread.sendResponseToClient();
		}
		else
		{
			System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] BombOwnerChange - Soy el dueño de la bomba, voy a enviar question ");
			
			QuizQuestion qq = ClientThread.getGame().getRandomQuizQuestion();
			
			Vector<String> answers = new Vector<String>();
			for(String a: qq.getAnswers()) answers.add(a);
			
			//mando pregunta
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
			response.setFlash("Recibiste una pregunta!");
			response.setQuizQuestion(qq.getQuestion());
			response.setQuizAnswers(answers);

			//activo bomba
			ClientThread.getGame().getBomb().activate();
			
			ClientThread.setResponse(response);
			ClientThread.sendResponseToClient();
		}

	}
	
	@Override
	public boolean isNotification(ClientThread ClientThread){
		return (!(ClientThread.getMyPlayer().getUID().equals(ClientThread.getGame().getBomb().getCurrentPlayer().getId())));
	}

}
