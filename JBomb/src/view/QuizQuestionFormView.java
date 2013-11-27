package view;

import core.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.util.Vector;

public class QuizQuestionFormView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private QuizFormView parentWindow;
	
	private JPanel contentPane;
	private JTable QuizAnswerTable;

	private QuizQuestion QuizQuestion;
	private JTextField QuizQuestionTextField;
	
	/**
	 * Create the frame.
	 */
	public QuizQuestionFormView(QuizFormView QuizFormView, QuizQuestion QuizQuestion) {
		
		this.parentWindow = QuizFormView;
		this.QuizQuestion = QuizQuestion;
		
		if (this.QuizQuestion.isNew())
		{
			setTitle("Nueva Pregunta");
		}
		else
		{
			setTitle("Editar Pregunta");
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Vector<String> QuizAnswerFields = new Vector<String>();
		
		QuizAnswerFields.add("Respuesta");
		QuizAnswerFields.add("Correcta");
		
		JButton btnQuizQuestionSave = new JButton("Guardar");
		btnQuizQuestionSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
					JOptionPane.showMessageDialog(QuizQuestionFormView.this,
						    "Debe marcar alguna respuesta como válida.",
						    "Datos incorrectos",
						    JOptionPane.WARNING_MESSAGE);
				*/
				
				QuizQuestionFormView QQFV = QuizQuestionFormView.this;
				
				QQFV.QuizQuestion.setQuestion(QQFV.QuizQuestionTextField.getText());
										
				Vector<String> qa = new Vector<String>();
				Integer ca = -1;
					
				for (int i = 0; i < QQFV.QuizAnswerTable.getRowCount(); i++)
				{
					qa.add(QQFV.QuizAnswerTable.getValueAt(i, 0).toString());
					
					if ((Boolean)QQFV.QuizAnswerTable.getValueAt(i, 1))
					{
						ca = i;
					}
				}
					
				QQFV.QuizQuestion.setAnswers(qa);
				QQFV.QuizQuestion.setCorrectAnswer(ca);
					
				QQFV.parentWindow.addQuizQuestion(QQFV.QuizQuestion);
				
				QQFV.parentWindow.setEnabled(true);
				
				QQFV.dispose();
			}
		});
		btnQuizQuestionSave.setBounds(333, 256, 99, 25);
		contentPane.add(btnQuizQuestionSave);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Pregunta", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(15, 12, 417, 54);
		contentPane.add(panel);
		panel.setLayout(null);
		
		QuizQuestionTextField = new JTextField();
		QuizQuestionTextField.setBounds(12, 23, 396, 19);
		panel.add(QuizQuestionTextField);
		QuizQuestionTextField.setColumns(10);
		
		JButton btnQuizAnswerEdit = new JButton("Modificar");
		btnQuizAnswerEdit.setEnabled(false);
		btnQuizAnswerEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				if (QuizAnswerTable.getSelectedRow() > 0)
				{
					QuizQuestion aQuizQuestion = (QuizQuestion) QuizAnswerVector.elementAt(QuizAnswerTable.convertRowIndexToModel(QuizAnswerTable.getSelectedRow()));
					
				}
				*/
			}
		});
		btnQuizAnswerEdit.setBounds(333, 78, 99, 25);
		contentPane.add(btnQuizAnswerEdit);
		
		JScrollPane QuizAnswerScrollPane = new JScrollPane();
		QuizAnswerScrollPane.setBounds(15, 78, 306, 135);
		contentPane.add(QuizAnswerScrollPane);
		
		QuizAnswerTable = new JTable(new DefaultTableModel(this.QuizQuestion.getAnswersVector(), QuizAnswerFields));
		QuizAnswerScrollPane.setViewportView(QuizAnswerTable);
		
		JButton btnQuizAnswerDelete = new JButton("Eliminar");
		btnQuizAnswerDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizQuestionFormView QQFV = QuizQuestionFormView.this;
				
				if (QQFV.QuizAnswerTable.getSelectedRow() >= 0)
				{
					QQFV.QuizQuestion.removeAnswer(QQFV.QuizAnswerTable.getSelectedRow());					
					((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).removeRow(QuizAnswerTable.getSelectedRow());				
					((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).fireTableDataChanged();
				}
				else
				{
					//	TODO: Agregar un mensaje de error.
				}
			}
		});
		btnQuizAnswerDelete.setBounds(333, 115, 99, 25);
		contentPane.add(btnQuizAnswerDelete);
		
		JButton btnQuizAnswerOK = new JButton("Correcta");
		btnQuizAnswerOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizQuestionFormView QQFV = QuizQuestionFormView.this;
				
				if (QQFV.QuizAnswerTable.getSelectedRow() >= 0)
				{					
					for (int i = 0; i < ((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).getRowCount(); i++)
					{
						((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).setValueAt(i == QuizAnswerTable.getSelectedRow(), i, 1);
					}
					
					((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).fireTableDataChanged();
				}
			}
		});
		btnQuizAnswerOK.setBounds(333, 152, 99, 25);
		contentPane.add(btnQuizAnswerOK);
		
		final JTextField QuizAnswerTextField = new JTextField();
		QuizAnswerTextField.setBounds(15, 225, 306, 19);
		contentPane.add(QuizAnswerTextField);
		QuizAnswerTextField.setColumns(10);
		
		JButton btnQuizAnswerAdd = new JButton("Agregar");
		btnQuizAnswerAdd.setBounds(333, 219, 99, 25);
		contentPane.add(btnQuizAnswerAdd);
		
		JButton btnQuizQuestionCancel = new JButton("Cancelar");
		btnQuizQuestionCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuizQuestionFormView.this.dispose();
			}
		});
		btnQuizQuestionCancel.setBounds(15, 256, 96, 25);
		contentPane.add(btnQuizQuestionCancel);
		btnQuizAnswerAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizQuestionFormView QQFV = QuizQuestionFormView.this;
				
				if (!QuizAnswerTextField.getText().equals(""))
				{				
					QQFV.QuizQuestion.addAnswer(QuizAnswerTextField.getText());				
					((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).addRow(new Object[]{QuizAnswerTextField.getText(), false});
					((DefaultTableModel)QQFV.QuizAnswerTable.getModel()).fireTableDataChanged();
					
					QuizAnswerTextField.setText("");
				}
			}
		});
		initDataBindings();
	}
	protected void initDataBindings() {
	}
}
