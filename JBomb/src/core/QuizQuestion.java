package core;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestion {

	private List<String> Answers = new ArrayList<String>();
	private Integer CorrectAnswer;
	
	public List<String> getAnswers() {
		return Answers;
	}
	
	public void setAnswers(List<String> answers) {
		Answers = answers;
	}
	
	public Integer getCorrectAnswer() {
		return CorrectAnswer;
	}
	
	public void setCorrectAnswer(Integer correctAnswer) {
		CorrectAnswer = correctAnswer;
	}
	
	
	
	
}
