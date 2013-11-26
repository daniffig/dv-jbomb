package core;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestion {

	private Quiz Quiz;
	private String Question;
	private List<String> Answers = new ArrayList<String>();
	private Integer CorrectAnswer;

	public QuizQuestion(){}
	
	public QuizQuestion(String Question)
	{
		this.setQuestion(Question);
	}
	
	public Quiz getQuiz() {
		return Quiz;
	}

	public void setQuiz(Quiz quiz) {
		Quiz = quiz;
	}
	
	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String question) {
		Question = question;
	}
	
	public String getAnswer(int index)
	{
		if (index <= this.getAnswers().size())
		{
			return this.getAnswers().get(index);
		}
		
		return null;
	}
	
	public List<String> getAnswers() {
		return Answers;
	}
	
	public void setAnswers(List<String> answers) {
		Answers = answers;
	}
	
	public void addAnswer(String answer)
	{
		this.getAnswers().add(answer);
	}
	
	public void removeAnswer(int index)
	{
		if (index <= this.getAnswers().size())
		{
			this.getAnswers().remove(index);
		}
	}
	
	public Integer getCorrectAnswer() {
		return CorrectAnswer;
	}
	
	public void setCorrectAnswer(Integer correctAnswer) {
		CorrectAnswer = correctAnswer;
	}
	
	
	
	
}
