package core;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

	private String Title;
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

	public List<QuizQuestion> getQuizQuestions() {
		return QuizQuestions;
	}

	public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
		QuizQuestions = quizQuestions;
	}
	
	public void addQuizQuestion(QuizQuestion quizQuestion) {
		this.getQuizQuestions().add(quizQuestion);
	}
}
