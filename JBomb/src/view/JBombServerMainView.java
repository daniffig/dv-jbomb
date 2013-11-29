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
import java.net.InetAddress;
import java.util.Vector;

import javax.swing.JScrollPane;

import network.GameServer;

import java.awt.Toolkit;

import javax.swing.JToolBar;

import java.awt.Choice;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.Action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.ButtonGroup;

import java.awt.Component;

import javax.swing.Box;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class JBombServerMainView {

	private JFrame frmJbombV;
	private JTable GamesTable;
	
	private Vector<Game> GameVector = new Vector<Game>();
	private Vector<Quiz> QuizVector = new Vector<Quiz>();
	private Vector<GameServer> GameServerVector = new Vector<GameServer>();
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
		scrollPane.setBounds(12, 12, 570, 320);
		frmJbombV.getContentPane().add(scrollPane);
		
		GamesTable = new JTable(new DefaultTableModel(ObjectVector, GameFields));
		GamesTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getClass());
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
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerMainView JBSMV = JBombServerMainView.this;
				
				if (JBSMV.GamesTable.getSelectedRow() >= 0)
				{
					Game Game = JBSMV.GameVector.get(JBSMV.GamesTable.convertRowIndexToModel(JBSMV.GamesTable.getSelectedRow()));
					
					GameServer GameServer = new GameServer(JBSMV);
					
					JBSMV.GameServerVector.add(GameServer);
					
					Thread t = new Thread(GameServer);
					t.start();
				}
			}
		});
		buttonGroup_1.add(mntmNewMenuItem_3);
		mnArchivo.add(mntmNewMenuItem_3);

		mnArchivo.addSeparator();
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Cerrar");
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
	}
	
	public void addGame(Game Game)
	{
		Game.setInetIPAddress(this.InetIPAddress);
		Game.setInetPort(this.InetPort);
		
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
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
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
