package view;

import core.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import linkageStrategies.*;

public class JBombServerGameFormView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JBombServerMainView parentWindow;

	private JPanel contentPane;
	private JTextField GameNameTextField;
	private JTextField GamePortTextField;
	
	private Game OldGame;
	private Game NewGame;

	/**
	 * Create the frame.
	 */
	public JBombServerGameFormView(JBombServerMainView JBombServerMainView, Game Game) {
		
		this.parentWindow = JBombServerMainView;
		
		this.setOldGame(Game);
		this.setNewGame(new Game(Game));
		
		setTitle("JBomb - Configuración del Juego");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Configuraci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 290, 294);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setBounds(12, 24, 125, 15);
		panel.add(lblNewLabel);
		
		GameNameTextField = new JTextField();
		GameNameTextField.setBounds(116, 22, 162, 19);
		panel.add(GameNameTextField);
		GameNameTextField.setColumns(10);
		
		JComboBox<AbstractLinkageStrategy> GameLinkageStrategyComboBox =
				new JComboBox<AbstractLinkageStrategy>(	new DefaultComboBoxModel<AbstractLinkageStrategy>(new AbstractLinkageStrategy[]{new RingLinkageStrategy()}));
		
		GameLinkageStrategyComboBox.setBounds(116, 114, 162, 24);
		panel.add(GameLinkageStrategyComboBox);
		
		JLabel lblJugadoresmx = new JLabel("Jugadores (máx.)");
		lblJugadoresmx.setBounds(12, 191, 125, 15);
		panel.add(lblJugadoresmx);
		
		Vector<Integer> GameMaxPlayersVector = new Vector<Integer>();
		
		for (Integer i = 2; i <= 16; i++)
		{
			GameMaxPlayersVector.add(i);
		}
		
		JComboBox<Integer> GameMaxPlayersComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(GameMaxPlayersVector));
		GameMaxPlayersComboBox.setBounds(155, 186, 123, 24);
		panel.add(GameMaxPlayersComboBox);
		
		Vector<Integer> GameRoundsVector = new Vector<Integer>();
		
		GameRoundsVector.add(2);
		GameRoundsVector.add(3);
		GameRoundsVector.add(4);
		
		ComboBoxModel<Integer> GameRoundsComboBoxModel = new DefaultComboBoxModel<Integer>(GameRoundsVector);
		
		JComboBox<Integer> GameRoundsComboBox = new JComboBox<Integer>(GameRoundsComboBoxModel);
		GameRoundsComboBox.setBounds(155, 222, 123, 24);
		panel.add(GameRoundsComboBox);
		
		JLabel lblRondas = new JLabel("Rondas");
		lblRondas.setBounds(12, 227, 125, 15);
		panel.add(lblRondas);
		
		JLabel lblDuracin = new JLabel("Duración");
		lblDuracin.setBounds(12, 263, 125, 15);
		panel.add(lblDuracin);
		
		Vector<Integer> GameRoundDurationVector = new Vector<Integer>();
		
		GameRoundDurationVector.add(30);
		GameRoundDurationVector.add(60);
		GameRoundDurationVector.add(90);
		
		ComboBoxModel<Integer> GameRoundDurationComboBoxModel = new DefaultComboBoxModel<Integer>(GameRoundDurationVector);
		
		JComboBox<Integer> GameRoundDurationComboBox = new JComboBox<Integer>(GameRoundDurationComboBoxModel);
		GameRoundDurationComboBox.setBounds(155, 258, 123, 24);
		panel.add(GameRoundDurationComboBox);
		
		JLabel lblIpYPuerto = new JLabel("Dirección IP");
		lblIpYPuerto.setBounds(12, 56, 82, 15);
		panel.add(lblIpYPuerto);

		
		Vector<InetAddress> InetAddressVector = new Vector<InetAddress>();

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
			    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			    	InetAddress ipAddr = enumIpAddr.nextElement();
			    	
			    	// Que el Dios de los Objetos me perdone por esto...
			    	if (ipAddr.getClass().getSimpleName().equals("Inet4Address") && !ipAddr.isLoopbackAddress())
			    	{
			    		InetAddressVector.add(ipAddr);
			    	}
			    }
			}
		} catch (SocketException e) {
			System.out.println(" (error retrieving network interface list)");
		}
		
		ComboBoxModel<InetAddress> InetAddressesComboBoxModel = new DefaultComboBoxModel<InetAddress>(InetAddressVector);
		
		JComboBox<InetAddress> GameIPAddressComboBox = new JComboBox<InetAddress>(InetAddressesComboBoxModel);
		GameIPAddressComboBox.setBounds(116, 51, 162, 24);
		panel.add(GameIPAddressComboBox);
		
		GamePortTextField = new JTextField();
		GamePortTextField.setBounds(116, 83, 162, 19);
		panel.add(GamePortTextField);
		GamePortTextField.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(12, 85, 48, 15);
		panel.add(lblPuerto);
		
		JLabel label = new JLabel("Topología");
		label.setBounds(12, 119, 125, 15);
		panel.add(label);
		
		JLabel lblPreguntas = new JLabel("Preguntas");
		lblPreguntas.setBounds(12, 155, 125, 15);
		panel.add(lblPreguntas);
		
		JComboBox<Quiz> GameQuizComboBox = new JComboBox<Quiz>(new DefaultComboBoxModel<Quiz>(this.parentWindow.getQuizVector()));
		GameQuizComboBox.setBounds(116, 150, 162, 24);
		panel.add(GameQuizComboBox);
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (NewGame.isValid())
				{
					OldGame.deepCopy(NewGame);
					
					
				}
			}
		});
		btnNewButton.setBounds(185, 318, 117, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView.this.dispose();
			}
		});
		btnNewButton_1.setBounds(12, 318, 117, 25);
		contentPane.add(btnNewButton_1);
		initDataBindings();
	}

	public Game getOldGame() {
		return OldGame;
	}

	public void setOldGame(Game oldGame) {
		OldGame = oldGame;
	}

	public Game getNewGame() {
		return NewGame;
	}

	public void setNewGame(Game newGame) {
		NewGame = newGame;
	}
	protected void initDataBindings() {
		BeanProperty<Game, String> gameBeanProperty = BeanProperty.create("name");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty.create("text");
		AutoBinding<Game, String, JTextField, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, OldGame, gameBeanProperty, GameNameTextField, jTextFieldBeanProperty);
		autoBinding.bind();
		//
		BeanProperty<Game, Integer> gameBeanProperty_1 = BeanProperty.create("inetPort");
		BeanProperty<JTextField, String> jTextFieldBeanProperty_1 = BeanProperty.create("text");
		AutoBinding<Game, Integer, JTextField, String> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, NewGame, gameBeanProperty_1, GamePortTextField, jTextFieldBeanProperty_1);
		autoBinding_1.bind();
	}
}
