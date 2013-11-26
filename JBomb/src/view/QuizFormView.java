package view;

import core.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

public class QuizFormView extends JFrame {

	private JPanel contentPane;
	private AbstractTableModel QuizQuestionsTableModel;
	private JTable QuizQuestionsTable;
	
	private Quiz Quiz;
	private Vector<Vector<Object>> QuizQuestionVector;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizFormView frame = new QuizFormView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuizFormView() {
		setTitle("Cuestionario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Listado de Preguntas");
		lblNewLabel.setBounds(12, 12, 153, 15);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			}
		});
		scrollPane.setBounds(12, 39, 309, 222);
		contentPane.add(scrollPane);
		
		this.Quiz = new Quiz("Cuestionario 01");
		
		QuizQuestion q = new QuizQuestion("Fede!");
		q.addAnswer("Fede");
		q.addAnswer("Te queremos");
		q.setCorrectAnswer(0);
		
		this.Quiz.addQuizQuestion(q);
		
		Vector<String> QuizQuestionFields = new Vector<String>();
		
		QuizQuestionFields.add("Pregunta");
		QuizQuestionFields.add("Respuesta");
		
		QuizQuestionVector = new Vector<Vector<Object>>();
		
		for (QuizQuestion qq : this.Quiz.getQuizQuestions())
		{
			Vector<Object> v = new Vector<Object>();
			
			v.add(qq.getQuestion());
			v.add(qq.getAnswer(qq.getCorrectAnswer()));
			
			QuizQuestionVector.add(v);			
		}
		
		this.QuizQuestionsTableModel = new DefaultTableModel(QuizQuestionVector, QuizQuestionFields);
		
		QuizQuestionsTable = new JTable(QuizQuestionsTableModel);
		scrollPane.setViewportView(QuizQuestionsTable);
		
		JButton btnQuizQuestionNew = new JButton("Nueva");
		btnQuizQuestionNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizQuestionFormView QuizQuestionFormView = new QuizQuestionFormView(QuizFormView.this);
				
				QuizQuestionFormView.setVisible(true);
			}
		});
		btnQuizQuestionNew.setBounds(333, 40, 99, 25);
		contentPane.add(btnQuizQuestionNew);
		
		JButton btnQuizQuestionEdit = new JButton("Modificar");
		btnQuizQuestionEdit.setEnabled(false);
		btnQuizQuestionEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnQuizQuestionEdit.setBounds(333, 77, 99, 25);
		contentPane.add(btnQuizQuestionEdit);
		
		JButton btnQuizQuestionDelete = new JButton("Eliminar");
		btnQuizQuestionDelete.setBounds(333, 114, 99, 25);
		contentPane.add(btnQuizQuestionDelete);
		
		JButton btnQuizSave = new JButton("Guardar");
		btnQuizSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnQuizSave.setBounds(333, 236, 99, 25);
		contentPane.add(btnQuizSave);
	}
	
	public void addQuizQuestion(QuizQuestion QuizQuestion)
	{
		Vector<Object> v = new Vector<Object>();
		
		v.add(QuizQuestion.getQuestion());
		v.add(QuizQuestion.getAnswer(QuizQuestion.getCorrectAnswer()));
		
		this.QuizQuestionVector.add(v);
		
		this.QuizQuestionsTableModel.fireTableDataChanged();
	}

	public Quiz getQuiz() {
		return Quiz;
	}
}
