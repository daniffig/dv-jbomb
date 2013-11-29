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

import network.GameClient;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;

public class JBombNewPlayerView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField PlayerNameInput;
	private GameClient game_client = new GameClient();
	private JTextField GameServerInetIPAddressTextField;
	private JTextField GameServerInetPortTextField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	public JBombNewPlayerView() {
		setTitle("Nuevo Juego");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 334);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnServidor = new JMenu("Servidor");
		menuBar.add(mnServidor);
		
		JMenuItem mntmActualizar = new JMenuItem("Actualizar");
		mnServidor.add(mntmActualizar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 7, 412, 193);
		panel.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JLabel nombre_jugador = new JLabel("Nombre del jugador:");
		panel_1.add(nombre_jugador);
		
		PlayerNameInput = new JTextField();
		panel_1.add(PlayerNameInput);
		PlayerNameInput.setColumns(10);
		
		JButton btnQuizSave = new JButton("Conectar");
		panel_1.add(btnQuizSave);
		btnQuizSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombNewPlayerView JBNPV = JBombNewPlayerView.this;
				
				if (JBNPV.isFormValid())
				{					
					game_client.username = PlayerNameInput.getText();
					
					String connection_result = game_client.joinGame();
					System.out.println("recibi " + connection_result);
					if(connection_result.equals("ACCEPTED"))
					{
						JBombGamePlayView gameplayview = new JBombGamePlayView(game_client);
						gameplayview.setVisible(true);
						dispose();			
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane, connection_result);
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
		*/
		
		if (this.PlayerNameInput.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de jugador.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		return true;
	}
}
