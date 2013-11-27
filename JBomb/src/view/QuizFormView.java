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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class QuizFormView extends JFrame {
	
	private QuizConfigurationFormView parentWindow;

	private JPanel contentPane;
	private AbstractTableModel QuizQuestionsTableModel;
	private JTable QuizQuestionsTable;
	
	private Quiz Quiz;
	private Vector<Vector<Object>> QuizQuestionVector;
	private JTextField QuizTitleTextField;

	/**
	 * Launch the application.
	 */
	/*
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
	*/

	/**
	 * Create the frame.
	 */
	public QuizFormView(QuizConfigurationFormView QuizConfigurationFormView, Quiz Quiz) {
		setResizable(false);
		
		this.parentWindow = QuizConfigurationFormView;
		this.Quiz = Quiz;
		
		if (this.Quiz.isNew())
		{
			setTitle("Nuevo Cuestionario");
		}
		else
		{
			setTitle("Editar Cuestionar :: " + this.Quiz.getTitle());
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			}
		});
		scrollPane.setBounds(12, 60, 309, 147);
		contentPane.add(scrollPane);
		
		Vector<String> QuizQuestionFields = new Vector<String>();
		
		QuizQuestionFields.add("Pregunta");
		QuizQuestionFields.add("Respuesta");
		
		QuizQuestionVector = new Vector<Vector<Object>>();
		
		if (!this.Quiz.isNew())
		{
			for (QuizQuestion qq : this.Quiz.getQuizQuestions())
			{
				Vector<Object> v = new Vector<Object>();
				
				v.add(qq.getQuestion());
				v.add(qq.getAnswer(qq.getCorrectAnswer()));
				
				QuizQuestionVector.add(v);			
			}			
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
		btnQuizQuestionNew.setBounds(333, 60, 99, 25);
		contentPane.add(btnQuizQuestionNew);
		
		JButton btnQuizQuestionEdit = new JButton("Modificar");
		btnQuizQuestionEdit.setEnabled(false);
		btnQuizQuestionEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnQuizQuestionEdit.setBounds(333, 97, 99, 25);
		contentPane.add(btnQuizQuestionEdit);
		
		JButton btnQuizQuestionDelete = new JButton("Eliminar");
		btnQuizQuestionDelete.setBounds(333, 134, 99, 25);
		contentPane.add(btnQuizQuestionDelete);
		
		JButton btnQuizSave = new JButton("Guardar");
		btnQuizSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizFormView.this.parentWindow.addQuiz(QuizFormView.this.Quiz);
				
				QuizFormView.this.dispose();
			}
		});
		btnQuizSave.setBounds(333, 216, 99, 25);
		contentPane.add(btnQuizSave);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "T\u00EDtulo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 420, 41);
		contentPane.add(panel);
		panel.setLayout(null);
		
		QuizTitleTextField = new JTextField();
		QuizTitleTextField.setBounds(5, 17, 403, 19);
		panel.add(QuizTitleTextField);
		QuizTitleTextField.setColumns(10);
		
		JButton btnQuizCancel = new JButton("Cancelar");
		btnQuizCancel.setBounds(12, 216, 96, 25);
		contentPane.add(btnQuizCancel);
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
