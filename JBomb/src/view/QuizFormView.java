package view;

import core.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

public class QuizFormView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private QuizConfigurationFormView parentWindow;

	private JPanel contentPane;
	private JTable QuizQuestionsTable;
	
	private Quiz Quiz;
	private JTextField QuizTitleTextField;

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
		
		QuizQuestionsTable = new JTable(new DefaultTableModel(this.Quiz.getQuestionsVector(), QuizQuestionFields));
		scrollPane.setViewportView(QuizQuestionsTable);
		
		JButton btnQuizQuestionNew = new JButton("Nueva");
		btnQuizQuestionNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizQuestionFormView QuizQuestionFormView = new QuizQuestionFormView(QuizFormView.this, new QuizQuestion());
				
				QuizQuestionFormView.setVisible(true);
			}
		});
		btnQuizQuestionNew.setBounds(333, 60, 99, 25);
		contentPane.add(btnQuizQuestionNew);
		
		JButton btnQuizQuestionEdit = new JButton("Modificar");
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
		initDataBindings();
	}
	
	public void addQuizQuestion(QuizQuestion QuizQuestion)
	{
		this.Quiz.addQuizQuestion(QuizQuestion);
		((DefaultTableModel)this.QuizQuestionsTable.getModel()).addRow(QuizQuestion.toVector());
		((DefaultTableModel)this.QuizQuestionsTable.getModel()).fireTableDataChanged();
	}
	
	protected void initDataBindings() {
		BeanProperty<Quiz, String> quizBeanProperty = BeanProperty.create("title");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty.create("text");
		AutoBinding<Quiz, String, JTextField, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, Quiz, quizBeanProperty, QuizTitleTextField, jTextFieldBeanProperty);
		autoBinding.bind();
	}
}
