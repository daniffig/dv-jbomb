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
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel info_server = new JLabel("Servidor " + this.game_client.server_ip + ":" + this.game_client.server_port);
		info_server.setBounds(10, 10, 200, 15);
		contentPane.add(info_server);
		
		JLabel nombre_jugador = new JLabel("Nombre del jugador:");
		nombre_jugador.setBounds(12, 107, 145, 15);
		contentPane.add(nombre_jugador);
		
		PlayerNameInput = new JTextField();
		PlayerNameInput.setBounds(175, 105, 257, 19);
		contentPane.add(PlayerNameInput);
		PlayerNameInput.setColumns(10);
		
		JButton btnQuizSave = new JButton("Sumarme al juego!");
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
		btnQuizSave.setBounds(232, 136, 200, 25);
		contentPane.add(btnQuizSave);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Servidor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 37, 422, 58);
		contentPane.add(panel);
		panel.setLayout(null);
		
		GameServerInetIPAddressTextField = new JTextField();
		GameServerInetIPAddressTextField.setEnabled(false);
		GameServerInetIPAddressTextField.setBounds(43, 27, 179, 19);
		panel.add(GameServerInetIPAddressTextField);
		GameServerInetIPAddressTextField.setColumns(10);
		
		GameServerInetPortTextField = new JTextField();
		GameServerInetPortTextField.setEnabled(false);
		GameServerInetPortTextField.setBounds(296, 27, 114, 19);
		panel.add(GameServerInetPortTextField);
		GameServerInetPortTextField.setColumns(10);
		
		JLabel lblDireccinIp = new JLabel("IP");
		lblDireccinIp.setBounds(12, 29, 13, 15);
		panel.add(lblDireccinIp);
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(230, 29, 48, 15);
		panel.add(lblPuerto);
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
