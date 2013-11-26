package core;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

	private String Title;
	private String Filename;

	private List<QuizQuestion> QuizQuestions = new ArrayList<QuizQuestion>();
	
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

	public List<QuizQuestion> getQuizQuestions() {
		return QuizQuestions;
	}

	public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
		QuizQuestions = quizQuestions;
	}
	
	public void addQuizQuestion(QuizQuestion quizQuestion) {
		this.getQuizQuestions().add(quizQuestion);
		quizQuestion.setQuiz(this);
	}
}
