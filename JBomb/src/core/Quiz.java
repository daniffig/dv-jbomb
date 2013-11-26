package core;

import java.util.Vector;

public class Quiz {

	private String Title;
	private String Filename;

	private Vector<QuizQuestion> QuizQuestions = new Vector<QuizQuestion>();
	
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
}
