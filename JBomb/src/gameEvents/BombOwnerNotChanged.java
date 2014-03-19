package gameEvents;

import java.util.Vector;

import network.JBombCommunicationObject;
import reference.JBombRequestResponse;
import concurrency.ClientThread;
import core.QuizQuestion;

public class BombOwnerNotChanged extends AbstractGameEvent {

	@Override
	public void handle(ClientThread ClientThread) {
		if(ClientThread.getGame().getBomb().getCurrentPlayer().getId().equals(ClientThread.getMyPlayer().getUID()))
		{	
			System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] BombOwnerNotChange - Soy el dueño de la bomba, voy a enviar question ");
			QuizQuestion qq = ClientThread.getGame().getRandomQuizQuestion();
		
			Vector<String> answers = new Vector<String>();
			for(String a: qq.getAnswers()) answers.add(a);

			//activo bomba
			ClientThread.getGame().getBomb().activate();
			
			//mando pregunta
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
			response.setFlash("Recibiste una pregunta!");
			response.setQuizQuestion(qq.getQuestion());
			response.setQuizAnswers(answers);

			ClientThread.setResponse(response);
			ClientThread.sendResponseToClient();
		}
		else
		{
			System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] Voy a mandar un notice_flash");
			
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.NOTICE_FLASH);
			
			response.setFlash(ClientThread.getGame().getBomb().getCurrentPlayer().getName() + " recibio la pregunta");
			
			ClientThread.setResponse(response);
			
			ClientThread.sendResponseToClient();
		}
	}
	
	@Override
	public boolean isNotification(ClientThread ClientThread){
		return true;
	}

}
