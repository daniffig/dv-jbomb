package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
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

	private JPanel contentPane;
	private JTable QuizTable;

	private Vector<Vector<Object>> QuizVector;
	private AbstractTableModel QuizTableModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizConfigurationFormView frame = new QuizConfigurationFormView();
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
	public QuizConfigurationFormView() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("/home/daniffig/Escritorio/Bomb.png"));
		setTitle("Administración de Cuestionarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 218);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane QuizScrollPane = new JScrollPane();
		QuizScrollPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				
			}
		});
		QuizScrollPane.setBounds(12, 39, 459, 138);
		contentPane.add(QuizScrollPane);
		
		Vector<String> QuizFields = new Vector<String>();
		
		QuizFields.add("Nombre");
		
		QuizVector = new Vector<Vector<Object>>();
		
		QuizTableModel = new DefaultTableModel(QuizVector, QuizFields);
		
		QuizTable = new JTable(QuizTableModel);
		QuizScrollPane.setViewportView(QuizTable);
		
		JLabel lblQuizList = new JLabel("Listado de Cuestionarios");
		lblQuizList.setBounds(12, 12, 178, 15);
		contentPane.add(lblQuizList);
		
		JButton btnQuizNew = new JButton("Nuevo");
		btnQuizNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizFormView QuizFormView = new QuizFormView();
				
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
						
						Vector<Object> v = new Vector<Object>();
						
						v.add(Quiz.getTitle());
						
						QuizConfigurationFormView.this.QuizVector.add(v);
						QuizConfigurationFormView.this.QuizTableModel.fireTableDataChanged();
					
			            source.close();
			            
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
		btnNewButton_2.setBounds(483, 115, 99, 25);
		contentPane.add(btnNewButton_2);
		
		JButton btnQuizDelete = new JButton("Eliminar");
		btnQuizDelete.setBounds(483, 152, 99, 25);
		contentPane.add(btnQuizDelete);
	}

}
