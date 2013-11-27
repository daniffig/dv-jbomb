package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import network.GameClient;

public class JBombGamePlayView extends JFrame{
	
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
		setBounds(200, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel nombre_jugador = new JLabel("Jugador unNombre");
		nombre_jugador.setBounds(275, 230, 150, 30);
		contentPane.add(nombre_jugador);
		
		JLabel jugador_adelante = new JLabel("Jugador_Adelante");
		jugador_adelante.setBounds(275, 100, 150, 30);
		contentPane.add(jugador_adelante);
		
		JLabel jugador_derecha = new JLabel("Jugador_Derecha");
		jugador_derecha.setBounds(475, 230, 150, 30);
		contentPane.add(jugador_derecha);
		
		JLabel jugador_abajo = new JLabel("Jugador_Abajo");
		jugador_abajo.setBounds(275, 360, 150, 30);
		contentPane.add(jugador_abajo);
		
		JLabel jugador_izquierda = new JLabel("Jugador_Izquierda");
		jugador_izquierda.setBounds(25, 230, 150, 30);
		contentPane.add(jugador_izquierda);
		
		JLabel notificaciones = new JLabel("Esta es una notificacion de ejemplo porque van a ser de este largo mas o menos");
		notificaciones.setBounds(150, 20, 500, 30);
		contentPane.add(notificaciones);
		
		JLabel ronda = new JLabel("Ronda: #/#");
		ronda.setBounds(10,20,80,20);
		contentPane.add(ronda);
		
		JLabel cant_jugadores = new JLabel("Cant. Jugadores: ##");
		cant_jugadores.setBounds(10,40,120,20);
		contentPane.add(cant_jugadores);
	}

}
