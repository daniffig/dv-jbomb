package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JScrollPane;

import network.GameServer;

import java.awt.Toolkit;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;

public class JBombServerMainView {

	private JFrame frmJbombV;
	private JTable GamesTable;
	
	private Vector<Game> GameVector = new Vector<Game>();
	private Vector<Quiz> QuizVector = new Vector<Quiz>();
//	private Vector<GameServer> GameServerVector = new Vector<GameServer>();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	
	private String InetIPAddress = "127.0.0.1";
	private Integer InetPort = 4321;
	
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
		frmJbombV.setTitle("JBomb v0.2.1 - Servidor");
		frmJbombV.setResizable(false);
		frmJbombV.setBounds(100, 100, 600, 392);
		frmJbombV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJbombV.getContentPane().setLayout(null);
		
		Vector<String> GameFields = new Vector<String>();
		
		GameFields.add("Nombre");
		GameFields.add("Modo");
		GameFields.add("Estado");
		GameFields.add("Jugadores");
		
		Vector<Vector<Object>> ObjectVector = new Vector<Vector<Object>>();
		
		for (Game g : GameServer.getInstance().getGames())
		{			
			ObjectVector.add(g.toVector());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 570, 320);
		frmJbombV.getContentPane().add(scrollPane);
		
		GamesTable = new JTable(new DefaultTableModel(ObjectVector, GameFields){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        //all cells false
		        return false;
		    }
		});

		scrollPane.setViewportView(GamesTable);
		
		JMenuBar menuBar = new JMenuBar();
		frmJbombV.setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Juego");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Nuevo");
		buttonGroup.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(JBombServerMainView.this, new Game());
				
				GameFormWindow.setVisible(true);
			}
		});
		mnArchivo.add(mntmNewMenuItem);
		
		JMenuItem mntmEditar = new JMenuItem("Editar");
		mntmEditar.setEnabled(false);
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerMainView JBSMV = JBombServerMainView.this;
				
				if (GamesTable.getSelectedRow() >= 0)
				{					
					JBombServerGameFormView GameFormWindow = new JBombServerGameFormView(JBSMV, JBSMV.GameVector.get(JBSMV.GamesTable.getSelectedRow()));
					
					GameFormWindow.setVisible(true);					
				}
			}
		});
		buttonGroup.add(mntmEditar);
		mnArchivo.add(mntmEditar);
		
		JMenuItem mntmEliminar = new JMenuItem("Eliminar");
		mntmEliminar.setEnabled(false);
		buttonGroup.add(mntmEliminar);
		mnArchivo.add(mntmEliminar);
		
		mnArchivo.addSeparator();
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Iniciar");
		buttonGroup_1.add(mntmNewMenuItem_3);
		mnArchivo.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Ver estado");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerMainView JBSMV = JBombServerMainView.this;
				
				if (GamesTable.getSelectedRow() >= 0)
				{					
					JBombGamePlayView GameplayWindow = new JBombGamePlayView(JBSMV, JBSMV.GameVector.get(JBSMV.GamesTable.getSelectedRow()));
					
					GameplayWindow.setVisible(true);					
				}
			}
		});
		buttonGroup_1.add(mntmNewMenuItem_4);
		mnArchivo.add(mntmNewMenuItem_4);
		
		JMenu mnNewMenu = new JMenu("Cuestionario");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Administración");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuizConfigurationFormView QuizConfigurationFormView = new QuizConfigurationFormView(JBombServerMainView.this);
				
				QuizConfigurationFormView.setVisible(true);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("Servidor");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Configuración");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JBombServerInetConfigurationFormView JBombServerInetConfigurationFormView = new JBombServerInetConfigurationFormView(JBombServerMainView.this);
				
				JBombServerInetConfigurationFormView.setVisible(true);			
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		
		JMenu mnNewMenu_2 = new JMenu("Ayuda");
		menuBar.add(mnNewMenu_2);

		Thread t = new Thread(GameServer.buildInstance(this));
		
		t.start();
	}
	
	/*
	
	public void addGame(Game g)
	{		
		this.GameVector = GameServer.getInstance().getGames();
		
		/*
		if (!this.GameVector.contains(g))
		{
			this.GameVector.add(g);
			((DefaultTableModel)this.GamesTable.getModel()).addRow(g.toVector());			
		}
		else
		{
			System.out.println("Ya lo tengo!!!!");
			// TODO: Agregar lógica para poder modificar un juego;
		}
		
		
		this.refreshGameTable();
	}
	
	*/
	
	public void refreshGameTable()
	{
		
		Vector<String> GameFields = new Vector<String>();
		
		GameFields.add("Nombre");
		GameFields.add("Modo");
		GameFields.add("Estado");
		GameFields.add("Jugadores");
		
		Vector<Vector<Object>> ObjectVector = new Vector<Vector<Object>>();
		
		for (Game g : GameServer.getInstance().getGames())
		{			
			ObjectVector.add(g.toVector());
		}
		
		System.out.printf("Mi vector tiene %s juegos.\n", ObjectVector.size());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 570, 320);
		frmJbombV.getContentPane().add(scrollPane);
		
		GamesTable.setModel(new DefaultTableModel(ObjectVector, GameFields){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        //all cells false
		        return false;
		    }
		});
		
		/*
		
		GamesTable = new JTable(new DefaultTableModel(ObjectVector, GameFields){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        //all cells false
		        return false;
		    }
		});
		
		*/
		
		((DefaultTableModel)this.GamesTable.getModel()).fireTableDataChanged();		
	}
	
	public void addQuiz(Quiz Quiz)
	{
		this.QuizVector.add(Quiz);
		
		GameServer.getInstance().getAvailableQuizzes().put(GameServer.getInstance().getAvailableQuizzes().size(), Quiz);
	}
	
	public Vector<Quiz> getQuizVector()
	{
		return this.QuizVector;
	}
	
	public String getInetIPAddress() {
		return InetIPAddress;
	}

	public Integer getInetPort() {
		return InetPort;
	}
	
	public void updateInetData(String InetIPAddress, String InetPort)
	{
		this.InetIPAddress = InetIPAddress;
		this.InetPort = Integer.parseInt(InetPort);
	}
}
