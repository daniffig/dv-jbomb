package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import network.GameClient;

import java.awt.BorderLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.FlowLayout;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.util.Vector;

import javax.swing.JScrollPane;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import reference.JBombRequestResponse;
@Deprecated
public class JBombNewPlayerView extends JFrame {
	/**
	 * 
	 */
/*	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField PlayerNameInput;
	private GameClient game_client = new GameClient();
//	private JTextField GameServerInetIPAddressTextField;
//	private JTextField GameServerInetPortTextField;
	private JTable GameClientGameInformationTable;
	
	private GameInformation selectedGameInformation;
	private JButton btnGameJoin;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombNewPlayerView frame = new JBombNewPlayerView();
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
	/*public JBombNewPlayerView() {		
		setResizable(false);
		setTitle("JBomb v0.2.1 - Cliente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 334);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnServidor = new JMenu("Servidor");
		menuBar.add(mnServidor);
		
		JMenuItem mntmActualizar = new JMenuItem("Actualizar");
		mntmActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JBombNewPlayerView.this.refreshGamesInformation();
			}
		});
		mnServidor.add(mntmActualizar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JLabel nombre_jugador = new JLabel("Nombre del jugador:");
		panel_1.add(nombre_jugador);
		
		PlayerNameInput = new JTextField();
		panel_1.add(PlayerNameInput);
		PlayerNameInput.setColumns(10);
		
		btnGameJoin = new JButton("Conectar");
		btnGameJoin.setEnabled(false);
		panel_1.add(btnGameJoin);
		btnGameJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombNewPlayerView JBNPV = JBombNewPlayerView.this;
				
				if (JBNPV.isFormValid())
				{					
					game_client.username = PlayerNameInput.getText();
					game_client.GameInformation = JBombNewPlayerView.this.getSelectedGameInformation();
					
					if(game_client.joinGame())
					{
						JBombNewPlayerView.this.refreshGamesInformation();
						
						JBombGamePlayView gameplayview = new JBombGamePlayView(game_client);
						
						gameplayview.setVisible(true);
						
						JBombNewPlayerView.this.dispose();			
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane, "Ocurrió un error al intentar conectarse");
					}					
				}
			}
		});
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);
		contentPane.add(panel_2, BorderLayout.NORTH);
		
		JLabel info_server = new JLabel("Servidor " + this.game_client.server_ip + ":" + this.game_client.server_port);
		panel_2.add(info_server);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		GameClientGameInformationTable = new JTable();
		scrollPane.setViewportView(GameClientGameInformationTable);

		this.refreshGamesInformation();
		
		GameClientGameInformationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		GameClientGameInformationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()	{
			public void valueChanged(ListSelectionEvent e)
			{
				JBombNewPlayerView JBNPV = JBombNewPlayerView.this;
				
				JBNPV.getBtnQuizSave().setEnabled(!((ListSelectionModel)e.getSource()).isSelectionEmpty());
				
				if (!((ListSelectionModel)e.getSource()).isSelectionEmpty())
				{
					JBNPV.setSelectedGameInformation(JBNPV.getGame_client().getGamesInformation().get(JBNPV.getGameClientGameInformationTable().convertRowIndexToModel(JBNPV.getGameClientGameInformationTable().getSelectedRow())));
				}
				else
				{
					JBNPV.setSelectedGameInformation(null);
				}
			}	
		});
			
	}

	public Boolean isFormValid()
	{
		/*
		if (this.GameServerInetIPAddressTextField.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Debe seleccionar una dirección IP.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		if (this.GameServerInetPortTextField.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Debe ingresar un puerto.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		else
		{
			try
			{
				Integer.parseInt(this.GameServerInetPortTextField.getText());
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, "El número de puerto es inválido.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
				
				return false;
			}
		}
		
		
		if (this.PlayerNameInput.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de jugador.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		return true;
	}

	public GameInformation getSelectedGameInformation() {
		return selectedGameInformation;
	}

	public void setSelectedGameInformation(GameInformation selectedGameInformation) {
		this.selectedGameInformation = selectedGameInformation;
	}
	protected void initDataBindings() {
	}
	public JButton getBtnQuizSave() {
		return btnGameJoin;
	}
	
	public void refreshGamesInformation()
	{
		this.game_client.retrieveGamesInformation();
		
		Vector<String> GameInformationColumns = new Vector<String>();
		
		GameInformationColumns.add("Nombre");
		GameInformationColumns.add("Modo");
		GameInformationColumns.add("Jugadores");
		GameInformationColumns.add("Estado");		
		
		Vector<Vector<Object>> GameInformationVector = new Vector<Vector<Object>>();
		
		for (GameInformation gi : game_client.getGamesInformation())
		{
			GameInformationVector.add(gi.toVector());
		}
		
		this.GameClientGameInformationTable.setModel(this.getGameInformationTableModel(GameInformationVector, GameInformationColumns));
	}
	
	public DefaultTableModel getGameInformationTableModel(Vector<Vector<Object>> GameInformationVector, Vector<String> GameInformationColumns)
	{
		return new DefaultTableModel(
				GameInformationVector,
				GameInformationColumns
		) {
			private static final long serialVersionUID = -569683951481313495L;
			
			Class[] columnTypes = new Class[] {
			};
			
			public Class getColumnClass(int columnIndex) {
				return String.class;
			}
		};
	}

	public JTable getGameClientGameInformationTable() {
		return GameClientGameInformationTable;
	}

	public void setGameClientGameInformationTable(
			JTable gameClientGameInformationTable) {
		GameClientGameInformationTable = gameClientGameInformationTable;
	}

	public GameClient getGame_client() {
		return game_client;
	}

	public void setGame_client(GameClient game_client) {
		this.game_client = game_client;
	}	*/
}
