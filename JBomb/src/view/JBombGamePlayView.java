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

public class JBombGamePlayView extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		lblGamePlayerName.setBounds(275, 298, 150, 30);
		contentPane.add(lblGamePlayerName);
		
		JLabel jugador_derecha = new JLabel("Jugador 03 (derecha)");
		jugador_derecha.setBounds(535, 363, 147, 15);
		contentPane.add(jugador_derecha);
		
		JLabel jugador_abajo = new JLabel("Jugador 04 (abajo)");
		jugador_abajo.setBounds(275, 531, 150, 30);
		contentPane.add(jugador_abajo);
		
		JLabel jugador_izquierda = new JLabel("Jugador 05 (izquierda)");
		jugador_izquierda.setBounds(12, 363, 156, 15);
		contentPane.add(jugador_izquierda);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Informaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 670, 73);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel notificaciones = new JLabel("Esta es una notificacion de ejemplo porque van a ser de este largo mas o menos");
		notificaciones.setBounds(145, 17, 500, 30);
		panel.add(notificaciones);
		
		JLabel ronda = new JLabel("Ronda: #/#");
		ronda.setBounds(5, 17, 80, 20);
		panel.add(ronda);
		
		JLabel cant_jugadores = new JLabel("Cant. Jugadores: ##");
		cant_jugadores.setBounds(5, 37, 120, 20);
		panel.add(cant_jugadores);
		
		JLabel lblJugador = new JLabel("Jugador 02 (arriba)");
		lblJugador.setBounds(275, 97, 132, 15);
		contentPane.add(lblJugador);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		button.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/LeftArrow.png")));
		button.setToolTipText("Pasar al Jugador 02");
		button.setBounds(12, 259, 92, 92);
		contentPane.add(button);
		
		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		button_1.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/RightArrow.png")));
		button_1.setToolTipText("Pasar al Jugador 02");
		button_1.setBounds(590, 259, 92, 92);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		button_2.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/DownArrow.png")));
		button_2.setToolTipText("Pasar al Jugador 02");
		button_2.setBounds(275, 427, 92, 92);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView JBombClientAnswerQuestionFormView = new JBombClientAnswerQuestionFormView(JBombGamePlayView.this);
				
				JBombClientAnswerQuestionFormView.setVisible(true);
			}
		});
		button_3.setIcon(new ImageIcon(JBombGamePlayView.class.getResource("/images/UpArrow.png")));
		button_3.setToolTipText("Pasar al Jugador 02");
		button_3.setBounds(275, 126, 92, 92);
		contentPane.add(button_3);
	}
}
