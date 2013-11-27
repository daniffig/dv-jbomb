package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import core.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JScrollPane;

import network.GameServer;

import java.awt.Toolkit;

public class JBombServerMainView {

	private JFrame frmJbombV;
	private JTable GamesTable;
	
	private Vector<Game> GameVector = new Vector<Game>();
	private Vector<Quiz> QuizVector = new Vector<Quiz>();
	private Vector<GameServer> GameServerVector = new Vector<GameServer>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombServerMainView window = new JBombServerMainView();
					window.frmJbombV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JBombServerMainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJbombV = new JFrame();
		frmJbombV.setIconImage(Toolkit.getDefaultToolkit().getImage(JBombServerMainView.class.getResource("/images/icon.png")));
		frmJbombV.setTitle("JBomb v0.2 - Servidor");
		frmJbombV.setResizable(false);
		frmJbombV.setBounds(100, 100, 600, 392);
		frmJbombV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJbombV.getContentPane().setLayout(null);
		
		Vector<String> GameFields = new Vector<String>();
		
		GameFields.add("Nombre");
		GameFields.add("Dirección IP");
		GameFields.add("Puerto");
		GameFields.add("Modo");
		GameFields.add("Jugadores");
		
		Vector<Vector<Object>> ObjectVector = new Vector<Vector<Object>>();
		
		for (Game g : GameVector)
		{			
			ObjectVector.add(g.toVector());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 39, 459, 280);
		frmJbombV.getContentPane().add(scrollPane);
		
		GamesTable = new JTable(new DefaultTableModel(ObjectVector, GameFields));
		scrollPane.setViewportView(GamesTable);
		
		JLabel lblJuegosActivos = new JLabel("Listado de Juegos");
		lblJuegosActivos.setBounds(12, 12, 127, 15);
		frmJbombV.getContentPane().add(lblJuegosActivos);
		
		JButton btnNewButton = new JButton("Nuevo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(JBombServerMainView.this, new Game());
				
				GameFormWindow.setVisible(true);
			}
		});
		btnNewButton.setBounds(483, 39, 99, 25);
		frmJbombV.getContentPane().add(btnNewButton);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(483, 113, 99, 25);
		frmJbombV.getContentPane().add(btnEliminar);
		
		JButton btnNewButton_1 = new JButton("Modificar");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JBombServerMainView JBSMV = JBombServerMainView.this;
				
				if (GamesTable.getSelectedRow() >= 0)
				{					
					JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(JBSMV, JBSMV.GameVector.get(JBSMV.GamesTable.getSelectedRow()));
					
					GameFormWindow.setVisible(true);					
				}
			}
		});
		btnNewButton_1.setBounds(483, 76, 99, 25);
		frmJbombV.getContentPane().add(btnNewButton_1);
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerMainView JBSMV = JBombServerMainView.this;
				
				if (JBSMV.GamesTable.getSelectedRow() >= 0)
				{
					Game Game = JBSMV.GameVector.get(JBSMV.GamesTable.convertRowIndexToModel(JBSMV.GamesTable.getSelectedRow()));
					
					GameServer GameServer = new GameServer(Game, JBSMV);
					
					JBSMV.GameServerVector.add(GameServer);
					
					GameServer.listen();
				}
				
			}
		});
		btnIniciar.setBounds(483, 294, 99, 25);
		frmJbombV.getContentPane().add(btnIniciar);
		
		JButton btnConfigurarPreguntas = new JButton("Administrar Cuestionarios");
		btnConfigurarPreguntas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizConfigurationFormView QuizConfigurationFormView = new QuizConfigurationFormView(JBombServerMainView.this);
				
				QuizConfigurationFormView.setVisible(true);
			}
		});
		btnConfigurarPreguntas.setBounds(12, 331, 219, 25);
		frmJbombV.getContentPane().add(btnConfigurarPreguntas);
	}
	
	public void addGame(Game Game)
	{
		if (!this.GameVector.contains(Game))
		{
			this.GameVector.add(Game);
			((DefaultTableModel)this.GamesTable.getModel()).addRow(Game.toVector());			
		}
		else
		{
			// TODO: Agregar lógica para poder modificar un juego;
		}
		
		this.refresh();
		//((DefaultTableModel)this.GamesTable.getModel()).fireTableDataChanged();
	}
	
	public void addQuiz(Quiz Quiz)
	{
		this.QuizVector.add(Quiz);
	}
	
	public Vector<Quiz> getQuizVector()
	{
		return this.QuizVector;
	}
	
	public void refresh()
	{
		((DefaultTableModel)this.GamesTable.getModel()).fireTableDataChanged();
	}
}
