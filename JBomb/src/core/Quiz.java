package core;

import java.util.Vector;

public class Quiz {

	private String Title;
	private String Filename;

	private Vector<QuizQuestion> QuizQuestions = new Vector<QuizQuestion>();
	
	public Quiz(){}
	
	public Quiz(String title) {
		this.Title = title;
	}
	
	public String toString() {
		return this.getTitle();
	}
	
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}
	
	public String getFilename() {
		return Filename;
	}

	public void setFilename(String filename) {
		Filename = filename;
	}

	public QuizQuestion getRandomQuizQuestion()
	{
		return this.getQuizQuestions().get((int)(Math.random() * this.getQuizQuestions().size()));
	}

	public Vector<QuizQuestion> getQuizQuestions() {
		return QuizQuestions;
	}

	public void setQuizQuestions(Vector<QuizQuestion> quizQuestions) {
		QuizQuestions = quizQuestions;
		
		for (QuizQuestion qq : this.getQuizQuestions())
		{
			qq.setQuiz(this);
		}
	}
	
	public void addQuizQuestion(QuizQuestion quizQuestion) {
		this.getQuizQuestions().add(quizQuestion);
		quizQuestion.setQuiz(this);
	}
	
	public Boolean isNew()
	{
		return this.Title == null;
	}
	
	public Vector<Object> toVector()
	{
		Vector<Object> v = new Vector<Object>();
		
		v.add(this.Title);
		v.add(this.Filename);
		
		return v;
	}
	
	public Vector<Vector<Object>> getQuestionsVector()
	{
		Vector<Vector<Object>> v = new Vector<Vector<Object>>();
		
		for (QuizQuestion q : this.getQuizQuestions())
		{			
			Vector<Object> a = new Vector<Object>();
			
			a.add(q.getQuestion());
			a.add(q.getAnswer(q.getCorrectAnswer()));
			
			v.add(a);
		}
		
		return v;
	}
}
