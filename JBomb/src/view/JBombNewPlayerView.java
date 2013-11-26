package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class JBombNewPlayerView extends JFrame {
	private JPanel contentPane;
	private JTextField PlayerNameInput;
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
		
		JLabel info_server = new JLabel("Servidor xxx.xxx.xxx.xxx:pppp");
		info_server.setBounds(10, 10, 200, 15);
		contentPane.add(info_server);
		
		JLabel nombre_jugador = new JLabel("Nombre del jugador:");
		nombre_jugador.setBounds(60, 60, 153, 15);
		contentPane.add(nombre_jugador);
		
		PlayerNameInput = new JTextField();
		PlayerNameInput.setBounds(183, 60, 200, 19);
		contentPane.add(PlayerNameInput);
		PlayerNameInput.setColumns(10);
		
		JButton btnQuizSave = new JButton("Sumarme al juego!");
		btnQuizSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnQuizSave.setBounds(125, 110, 200, 25);
		contentPane.add(btnQuizSave);
	}
	
}