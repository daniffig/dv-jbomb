package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import core.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class QuizConfigurationFormView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JBombServerMainView parentWindow;
	
	private JPanel contentPane;
	private JTable QuizTable;

	private Vector<Quiz> QuizVector;

	/**
	 * Create the frame.
	 */
	public QuizConfigurationFormView(JBombServerMainView JBombServerMainView) {
		
		this.parentWindow = JBombServerMainView;
		
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("/home/daniffig/Escritorio/Bomb.png"));
		setTitle("Administración de Cuestionarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 249);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane QuizScrollPane = new JScrollPane();
		QuizScrollPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				
			}
		});
		QuizScrollPane.setBounds(12, 39, 459, 175);
		contentPane.add(QuizScrollPane);
		
		Vector<String> QuizFields = new Vector<String>();
		
		QuizFields.add("Nombre");
		QuizFields.add("Archivo");
		
		QuizVector = new Vector<Quiz>();
		
		Vector<Vector<Object>> ObjectVector = new Vector<Vector<Object>>();
		
		for (Quiz Quiz : QuizVector)
		{
			ObjectVector.add(Quiz.toVector());
		}
		
		QuizTable = new JTable(new DefaultTableModel(ObjectVector, QuizFields));
		QuizScrollPane.setViewportView(QuizTable);
		
		JLabel lblQuizList = new JLabel("Listado de Cuestionarios");
		lblQuizList.setBounds(12, 12, 178, 15);
		contentPane.add(lblQuizList);
		
		JButton btnQuizNew = new JButton("Nuevo");
		btnQuizNew.setEnabled(false);
		btnQuizNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizConfigurationFormView.this.setEnabled(false);;
				
				QuizFormView QuizFormView = new QuizFormView(QuizConfigurationFormView.this, new Quiz());
				
				QuizFormView.setVisible(true);
			}
		});
		btnQuizNew.setBounds(483, 41, 99, 25);
		contentPane.add(btnQuizNew);
		
		JButton btnQuizLoad = new JButton("Cargar");
		btnQuizLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(QuizConfigurationFormView.this);				

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            
		            Scanner source;
					try {
						source = new Scanner(file);
						
						Quiz Quiz = new Quiz(source.nextLine());
						Quiz.setFilename(file.getName());
												
						for (int i = Integer.parseInt(source.nextLine()); i > 0; i--)
						{							
							QuizQuestion QuizQuestion = new QuizQuestion(source.nextLine());
							
							for (int j = Integer.parseInt(source.nextLine()); j > 0; j--)
							{
								QuizQuestion.addAnswer(source.nextLine());
							}
							
							QuizQuestion.setCorrectAnswer(Integer.parseInt(source.nextLine()));
							
							Quiz.addQuizQuestion(QuizQuestion);							
						}
						
						QuizConfigurationFormView.this.QuizVector.add(Quiz);						
						((DefaultTableModel)QuizConfigurationFormView.this.QuizTable.getModel()).addRow(Quiz.toVector());
						((DefaultTableModel)QuizConfigurationFormView.this.QuizTable.getModel()).fireTableDataChanged();
					
			            source.close();
			            
			            QuizConfigurationFormView.this.parentWindow.addQuiz(Quiz);
			            
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			}
		});
		btnQuizLoad.setBounds(483, 78, 99, 25);
		contentPane.add(btnQuizLoad);
		
		JButton btnNewButton_2 = new JButton("Modificar");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuizConfigurationFormView QCFV = QuizConfigurationFormView.this;
				
				System.out.println(QCFV.QuizTable.getSelectedRow());
				
				if (QCFV.QuizTable.getSelectedRow() >= 0)
				{					
					QuizFormView QuizFormView = new QuizFormView(QuizConfigurationFormView.this, QuizConfigurationFormView.this.QuizVector.get(QCFV.QuizTable.getSelectedRow()));
					
					QuizFormView.setVisible(true);					
				}
				
			}
		});
		btnNewButton_2.setBounds(483, 115, 99, 25);
		contentPane.add(btnNewButton_2);
		
		JButton btnQuizDelete = new JButton("Eliminar");
		btnQuizDelete.setEnabled(false);
		btnQuizDelete.setBounds(483, 152, 99, 25);
		contentPane.add(btnQuizDelete);
		
		JButton btnQuizConfigurationFormClose = new JButton("Cerrar");
		btnQuizConfigurationFormClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuizConfigurationFormView.this.dispose();
			}
		});
		btnQuizConfigurationFormClose.setBounds(483, 189, 99, 25);
		contentPane.add(btnQuizConfigurationFormClose);
	}
	
	public void addQuiz(Quiz Quiz)
	{
		this.QuizVector.add(Quiz);
		((DefaultTableModel)this.QuizTable.getModel()).addRow(Quiz.toVector());
		((DefaultTableModel)this.QuizTable.getModel()).fireTableDataChanged();
	}
}
