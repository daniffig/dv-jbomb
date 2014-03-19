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
			System.out.println("[Player Id " + ClientThread.getMyPlayer().getUID() +"] Voy a mandar notificacion de cambio de dueño de bomba");
			
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.NOTICE_FLASH);
			
			response.setFlash(ClientThread.getEventHandler().getEventMessage());
			
			ClientThread.setResponse(response);
			
			ClientThread.sendResponseToClient();
		}
		else
		{
			QuizQuestion qq = ClientThread.getGame().getRandomQuizQuestion();
			
			Vector<String> answers = new Vector<String>();
			for(String a: qq.getAnswers()) answers.add(a);
			
			//mando pregunta
			JBombCommunicationObject response = new JBombCommunicationObject(JBombRequestResponse.QUIZ_QUESTION_RESPONSE);
			response.setFlash("Recibiste una pregunta!");
			response.setQuizQuestion(qq.getQuestion());
			response.setQuizAnswers(answers);

			ClientThread.setResponse(response);
			ClientThread.sendResponseToClient();
			
			//activo bomba
			ClientThread.getGame().getBomb().activate();
			
			//armo notificaciï¿½n y despiero a todos
			ClientThread.getEventHandler().setEvent(new NotifyEvent());
			ClientThread.getEventHandler().setEventMessage(ClientThread.getMyPlayer().getName() + " recibiï¿½ la pregunta");
			ClientThread.getEventHandler().wakeUpAll();
		}

	}
	
	@Override
	public boolean isNotification(ClientThread ClientThread){
		return !ClientThread.getMyPlayer().getUID().equals(ClientThread.getGame().getBomb().getCurrentPlayer().getId());
	}

}
