package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
	
	public Boolean isNew()
	{
		return this.Question == null;
	}
	
	public Vector<Object> toVector()
	{
		Vector<Object> v = new Vector<Object>();
		
		v.add(this.Question);
		v.add(this.getAnswer(this.getCorrectAnswer()));
		
		return v;
	}
	
	public Vector<Vector<Object>> getAnswersVector()
	{
		Vector<Vector<Object>> v = new Vector<Vector<Object>>();
		
		int i = 0;
		
		for (String s : this.getAnswers())
		{			
			Vector<Object> a = new Vector<Object>();
			
			a.add(s);
			a.add(i == this.getCorrectAnswer());
			
			v.add(a);
			
			i++;
		}
		
		return v;
	}
	
	
}
