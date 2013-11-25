package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import core.Game;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.util.Vector;

import javax.swing.JScrollPane;

public class JBombServerMainView {

	private JFrame frmJbombV;
	private JTable GamesTable;

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
		frmJbombV.setTitle("JBomb v0.2 - Servidor");
		frmJbombV.setResizable(false);
		frmJbombV.setBounds(100, 100, 600, 392);
		frmJbombV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJbombV.getContentPane().setLayout(null);
		
		final Vector<Game> Games = new Vector<Game>();
		
		Games.add(new Game("Juego 01"));
		Games.add(new Game("Juego 02"));
		
		Vector<String> GameFields = new Vector<String>();
		
		GameFields.add("Name");
		
		Vector<Vector<Object>> GameVector = new Vector<Vector<Object>>();
		
		for (int i = 0; i < Games.size(); i++)
		{
			Game Game = Games.elementAt(i);
			
			Vector<Object> v = new Vector<Object>();
			
			v.add(Game.getName());
			
			GameVector.add(v);
		}
		
		AbstractTableModel GameTableModel = new DefaultTableModel(GameVector, GameFields);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 39, 459, 280);
		frmJbombV.getContentPane().add(scrollPane);
		
		GamesTable = new JTable(GameTableModel);
		scrollPane.setViewportView(GamesTable);
		
		JLabel lblJuegosActivos = new JLabel("Listado de Juegos");
		lblJuegosActivos.setBounds(12, 12, 127, 15);
		frmJbombV.getContentPane().add(lblJuegosActivos);
		
		JButton btnNewButton = new JButton("Nuevo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(new Game());
				
				GameFormWindow.setVisible(true);
			}
		});
		btnNewButton.setBounds(483, 39, 99, 25);
		frmJbombV.getContentPane().add(btnNewButton);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(483, 113, 99, 25);
		frmJbombV.getContentPane().add(btnEliminar);
		
		JButton btnNewButton_1 = new JButton("Modificar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				if (GamesTable.getSelectedRow() > 0)
				{					
					Game aGame = Games.elementAt(GamesTable.convertRowIndexToModel(GamesTable.getSelectedRow()));
					
					JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(aGame);
					
					GameFormWindow.setVisible(true);					
				}
			}
		});
		btnNewButton_1.setBounds(483, 76, 99, 25);
		frmJbombV.getContentPane().add(btnNewButton_1);
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBounds(483, 294, 99, 25);
		frmJbombV.getContentPane().add(btnIniciar);
		
		JButton btnConfigurarPreguntas = new JButton("Administrar Cuestionarios");
		btnConfigurarPreguntas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizConfigurationFormView QuizConfigurationFormView = new QuizConfigurationFormView();
				
				QuizConfigurationFormView.setVisible(true);
			}
		});
		btnConfigurarPreguntas.setBounds(12, 331, 219, 25);
		frmJbombV.getContentPane().add(btnConfigurarPreguntas);
	}
}
