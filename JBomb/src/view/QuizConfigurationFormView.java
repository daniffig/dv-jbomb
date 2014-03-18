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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;

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
		//setIconImage(Toolkit.getDefaultToolkit().getImage("/home/daniffig/Escritorio/Bomb.png"));
		setTitle("Administración de Cuestionarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 367);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Archivo");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmCargar = new JMenuItem("Cargar");
		mntmCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			            
					} catch (FileNotFoundException f) {
						// TODO Auto-generated catch block
						f.printStackTrace();
					}
		        }
			}
		});
		mnNewMenu.add(mntmCargar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Guardar");
		mnNewMenu.add(mntmNewMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		mnNewMenu.addSeparator();
		
		JMenuItem mntmWizard = new JMenuItem("Wizard");
		mnNewMenu.add(mntmWizard);
		
		mnNewMenu.addSeparator();
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cerrar");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizConfigurationFormView.this.dispose();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		Vector<String> QuizFields = new Vector<String>();
		
		QuizFields.add("Nombre");
		QuizFields.add("Archivo");
		
		QuizVector = new Vector<Quiz>();
		
		Vector<Vector<Object>> ObjectVector = new Vector<Vector<Object>>();
		
		for (Quiz Quiz : QuizVector)
		{
			ObjectVector.add(Quiz.toVector());
		}
		
		for (Quiz Quiz : this.parentWindow.getQuizVector())
		{
			ObjectVector.add(Quiz.toVector());
		}
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane QuizScrollPane = new JScrollPane();
		QuizScrollPane.setBounds(12, 12, 560, 285);
		panel.add(QuizScrollPane);
		
		QuizTable = new JTable(new DefaultTableModel(ObjectVector, QuizFields) {
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        //all cells false
		        return false;
		    }			
		});
		QuizScrollPane.setViewportView(QuizTable);
	}
	
	public void addQuiz(Quiz Quiz)
	{
		this.QuizVector.add(Quiz);
		((DefaultTableModel)this.QuizTable.getModel()).addRow(Quiz.toVector());
		((DefaultTableModel)this.QuizTable.getModel()).fireTableDataChanged();
	}
}
