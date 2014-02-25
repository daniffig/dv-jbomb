package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import network.GameClient;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
@Deprecated //Ahora se usa el cliente android

public class JBombGamePlayView extends JFrame{
	
/*	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GameClient game_client = new GameClient();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombGamePlayView frame = new JBombGamePlayView(new GameClient());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public JBombGamePlayView(GameClient gc)
	{
		this.game_client = gc;
		this.game_client.receiveGameInformation();
		
		setTitle(this.game_client.gameName);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblGamePlayerName = new JLabel(gc.username);
		lblGamePlayerName.setBounds(290, 298, 150, 30);
		contentPane.add(lblGamePlayerName);
		
		JLabel jugador_derecha = new JLabel("Jugador 03 (derecha)");
		jugador_derecha.setBounds(535, 363, 147, 15);
		contentPane.add(jugador_derecha);
		
		JLabel jugador_abajo = new JLabel("Jugador 04 (abajo)");
		jugador_abajo.setBounds(290, 531, 150, 30);
		contentPane.add(jugador_abajo);
		
		JLabel jugador_izquierda = new JLabel("Jugador 05 (izquierda)");
		jugador_izquierda.setBounds(12, 363, 156, 15);
		contentPane.add(jugador_izquierda);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Informaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 670, 73);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel notificaciones = new JLabel("");
		notificaciones.setBounds(145, 17, 500, 30);
		panel.add(notificaciones);
		
		JLabel ronda = new JLabel("Ronda: #/#");
		ronda.setBounds(5, 17, 80, 20);
		panel.add(ronda);
		
		JLabel cant_jugadores = new JLabel("Cant. Jugadores: " + this.game_client.totalPlayers);
		cant_jugadores.setBounds(5, 37, 120, 20);
		panel.add(cant_jugadores);
		
		JLabel lblJugador = new JLabel("Jugador 02 (arriba)");
		lblJugador.setBounds(290, 97, 132, 15);
		contentPane.add(lblJugador);
		
		JLabel sendUp = new JLabel("");
		sendUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		sendUp.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/UpArrow.png")));
		sendUp.setBounds(290, 124, 90, 90);
		contentPane.add(sendUp);
		
		JLabel sendLeft = new JLabel("");
		sendLeft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		sendLeft.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/LeftArrow.png")));
		sendLeft.setBounds(78, 261, 90, 90);
		contentPane.add(sendLeft);
		
		JLabel sendRight = new JLabel("");
		sendRight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		sendRight.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/RightArrow.png")));
		sendRight.setHorizontalAlignment(SwingConstants.CENTER);
		sendRight.setBounds(535, 261, 90, 90);
		contentPane.add(sendRight);
		
		JLabel sendDown = new JLabel("");
		sendDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		sendDown.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/DownArrow.png")));
		sendDown.setBounds(290, 429, 90, 90);
		contentPane.add(sendDown);
	}*/
}
