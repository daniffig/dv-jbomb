package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import core.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;

import linkageStrategies.AbstractLinkageStrategy;
import linkageStrategies.RingLinkageStrategy;

import javax.swing.JComboBox;

@Deprecated
public class JBombMainView {

	private JFrame frmJbombV;
	private JTextField newGamePlayerInput;
	private JTextField maxGamePlayersAllowedInput;
	private JTextField roundQtyInput;

	private Game Game = new Game();
	private DefaultListModel<GamePlayer> GamePlayerListModel = new DefaultListModel<GamePlayer>();
	private JTextField roundDurationInput;
	private JList<AbstractLinkageStrategy> linkageStrategiesList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombMainView window = new JBombMainView();
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
	public JBombMainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJbombV = new JFrame();
		frmJbombV.setTitle("JBomb v0.0.1");
		frmJbombV.setBounds(100, 100, 800, 600);
		frmJbombV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJbombV.getContentPane().setLayout(null);

		JLabel lblJugadores = new JLabel("Jugadores");
		lblJugadores.setBounds(12, 12, 100, 15);
		frmJbombV.getContentPane().add(lblJugadores);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 39, 200, 485);
		frmJbombV.getContentPane().add(scrollPane);

		final JList<GamePlayer> gamePlayersList = new JList<GamePlayer>(
				GamePlayerListModel);
		scrollPane.setViewportView(gamePlayersList);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.getGamePlayers().remove(
						GamePlayerListModel.get(gamePlayersList
								.getSelectedIndex()));

				GamePlayerListModel.remove(gamePlayersList.getSelectedIndex());
			}
		});
		btnEliminar.setBounds(12, 536, 117, 25);
		frmJbombV.getContentPane().add(btnEliminar);

		JLabel lblNuevoJugador = new JLabel("Nuevo Jugador");
		lblNuevoJugador.setBounds(224, 12, 200, 15);
		frmJbombV.getContentPane().add(lblNuevoJugador);

		newGamePlayerInput = new JTextField();
		newGamePlayerInput.setBounds(224, 39, 200, 19);
		frmJbombV.getContentPane().add(newGamePlayerInput);
		newGamePlayerInput.setColumns(10);

		final JButton addGamePlayerButton = new JButton("Agregar");
		addGamePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (Game.canAddPlayer()) {
					GamePlayer GamePlayer = new GamePlayer(newGamePlayerInput
							.getText());

					Game.addGamePlayer(GamePlayer);

					GamePlayerListModel.addElement(GamePlayer);

					newGamePlayerInput.setText("");
				}
			}
		});
		addGamePlayerButton.setBounds(307, 70, 117, 25);
		frmJbombV.getContentPane().add(addGamePlayerButton);

		JSeparator separator = new JSeparator();
		separator.setBounds(224, 107, 558, 2);
		frmJbombV.getContentPane().add(separator);

		JLabel lblConfiguracinDelJuego = new JLabel("Configuración del Juego");
		lblConfiguracinDelJuego.setBounds(224, 121, 558, 15);
		frmJbombV.getContentPane().add(lblConfiguracinDelJuego);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBounds(234, 148, 548, 25);
		frmJbombV.getContentPane().add(panel);

		JLabel lblCantidadDeJugadores = new JLabel("Cantidad de Jugadores");
		lblCantidadDeJugadores.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblCantidadDeJugadores);

		maxGamePlayersAllowedInput = new JTextField();
		maxGamePlayersAllowedInput
				.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent arg0) {
						if (maxGamePlayersAllowedInput.getText().isEmpty()) {
							newGamePlayerInput
									.setText(maxGamePlayersAllowedInput
											.getText());
						}
					}
				});
		maxGamePlayersAllowedInput
				.addInputMethodListener(new InputMethodListener() {
					public void caretPositionChanged(InputMethodEvent arg0) {
					}

					public void inputMethodTextChanged(InputMethodEvent arg0) {
						if (maxGamePlayersAllowedInput.getText().isEmpty()) {
							newGamePlayerInput.setText("FedeeeeeeeeeeeEE");
						}

						Game.setMaxGamePlayersAllowed(Integer
								.valueOf(maxGamePlayersAllowedInput.getText()));
					}
				});
		panel.add(maxGamePlayersAllowedInput);
		maxGamePlayersAllowedInput.setColumns(10);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(234, 185, 548, 25);
		frmJbombV.getContentPane().add(panel_1);

		JLabel lblNewLabel = new JLabel("Cantidad de Rondas");
		panel_1.add(lblNewLabel);

		roundQtyInput = new JTextField();
		panel_1.add(roundQtyInput);
		roundQtyInput.setColumns(10);

		final JLabel maxGamePlayersAllowedLabel = new JLabel("");
		maxGamePlayersAllowedLabel.setBounds(112, 12, 100, 15);
		frmJbombV.getContentPane().add(maxGamePlayersAllowedLabel);

		maxGamePlayersAllowedLabel.setText("max. "
				+ Game.getMaxGamePlayersAllowed());

		JButton saveGameConfigurationButton = new JButton("Guardar");
		saveGameConfigurationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!maxGamePlayersAllowedInput.getText().isEmpty()
						&& Integer.valueOf(maxGamePlayersAllowedInput.getText()) > 0) {
					Integer maxGamePlayersAllowed = Integer
							.valueOf(maxGamePlayersAllowedInput.getText());

					Game.setMaxGamePlayersAllowed(maxGamePlayersAllowed);

					maxGamePlayersAllowedLabel.setText("max. "
							+ maxGamePlayersAllowed);
				}
				
				if (!roundDurationInput.getText().isEmpty()
						&& Integer.valueOf(roundDurationInput.getText()) > 0) {
					Integer roundDuration = Integer
							.valueOf(roundDurationInput.getText());
					
					Game.setRoundDuration(roundDuration);					
				}
				
				if (linkageStrategiesList.getSelectedValue() != null) {
					Game.setLinkageStrategy(linkageStrategiesList.getSelectedValue());
				}				
			}
		});
		saveGameConfigurationButton.setBounds(665, 384, 117, 25);
		frmJbombV.getContentPane().add(saveGameConfigurationButton);

		JButton btnjugar = new JButton("¡Jugar!");
		btnjugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GamePlayView playWindow = new GamePlayView(Game);
				
				playWindow.getDialog().pack();

				playWindow.getDialog().setVisible(true);
			}
		});
		btnjugar.setBounds(665, 536, 117, 25);
		frmJbombV.getContentPane().add(btnjugar);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_2.setBounds(234, 222, 548, 25);
		frmJbombV.getContentPane().add(panel_2);

		JLabel lblDuracinDeCada = new JLabel("Duración de cada Ronda:");
		panel_2.add(lblDuracinDeCada);

		roundDurationInput = new JTextField();
		panel_2.add(roundDurationInput);
		roundDurationInput.setColumns(10);

		JLabel lblSegundos = new JLabel("segundos");
		panel_2.add(lblSegundos);
		
		JLabel lblEstrategiaDeArmado = new JLabel("Estrategia de Armado");
		lblEstrategiaDeArmado.setBounds(224, 259, 558, 15);
		frmJbombV.getContentPane().add(lblEstrategiaDeArmado);
		
		DefaultListModel<AbstractLinkageStrategy> linkageListModel = new DefaultListModel<AbstractLinkageStrategy>();
		linkageListModel.addElement(new RingLinkageStrategy());
		
		linkageStrategiesList = new JList<AbstractLinkageStrategy>(linkageListModel);
		linkageStrategiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		linkageStrategiesList.setBounds(234, 286, 548, 86);
		frmJbombV.getContentPane().add(linkageStrategiesList);
		
		JButton btnConfigurarPreguntas = new JButton("Avanzado");
		btnConfigurarPreguntas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnConfigurarPreguntas.setBounds(655, 36, 117, 25);
		frmJbombV.getContentPane().add(btnConfigurarPreguntas);
		

		
		Quiz[] Q =
		{
				new Quiz("Preguntas 01"),
				new Quiz("Preguntas 02"),
				new Quiz("Preguntas 03"),
				new Quiz("Preguntas 04")
		};
		
		ComboBoxModel<Quiz> Quizzes = new DefaultComboBoxModel<Quiz>(Q);
		
		JComboBox<Quiz> comboBox = new JComboBox<Quiz>(Quizzes);
		comboBox.setBounds(436, 36, 207, 24);
		frmJbombV.getContentPane().add(comboBox);
		
		Vector<InetAddress> InetAddressList = new Vector<InetAddress>();

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
			    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			    	InetAddress ipAddr = enumIpAddr.nextElement();
			    	
			    	// Que el Dios de los Objetos me perdone por esto...
			    	if (ipAddr.getClass().getSimpleName().equals("Inet4Address"))
			    	{
			    		InetAddressList.add(ipAddr);
			    	}
			    }
			}
		} catch (SocketException e) {
			System.out.println(" (error retrieving network interface list)");
		}
		
		ComboBoxModel<InetAddress> InetAddressesComboBoxModel = new DefaultComboBoxModel<InetAddress>(InetAddressList);
		
		final JComboBox<InetAddress> comboBox_1 = new JComboBox<InetAddress>(InetAddressesComboBoxModel);
		comboBox_1.setBounds(436, 70, 207, 24);
		frmJbombV.getContentPane().add(comboBox_1);		
	}
}
