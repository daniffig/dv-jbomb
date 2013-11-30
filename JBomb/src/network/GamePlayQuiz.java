package network;

import java.util.Vector;

public class GamePlayQuiz {

	private String Question;
	private Vector<String> Answers= new Vector<String>();
  
  
  public GamePlayQuiz(String Question, Vector<String> Answers)
  {
	  this.Question = Question;
	  this.Answers = Answers;
  }
  
  public String getQuestion()
  {
		return Question;
  }


  public void setQuestion(String question)
  {
		Question = question;
  }


  public Vector<String> getAnswers()
  {
		return Answers;
  }


  public void setAnswers(Vector<String> answers)
  {
		Answers = answers;
  }

}
