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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import linkageStrategies.*;
import gameModes.*;

import java.awt.Toolkit;

public class JBombServerGameFormView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JBombServerMainView parentWindow;

	private JPanel contentPane;
	
	private JTextField GameNameTextField;
	private JComboBox<InetAddress> GameInetIPAddressComboBox;
	private JTextField GameInetPortTextField;	
	private JComboBox<AbstractLinkageStrategy> GameLinkageStrategyComboBox;
	private JComboBox<Quiz> GameQuizComboBox;
	private JComboBox<AbstractGameMode> GameModeComboBox;
	private JComboBox<Integer> GameMaxPlayersComboBox;
	private JComboBox<Integer> GameRoundsComboBox;
	private JComboBox<Integer> GameRoundDurationComboBox;
	
	private Game Game;

	/**
	 * Create the frame.
	 */
	public JBombServerGameFormView(JBombServerMainView JBombServerMainView, Game Game) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(JBombServerGameFormView.class.getResource("/images/icon.png")));
		
		this.parentWindow = JBombServerMainView;
		
		this.setGame(Game);
		
		setTitle("JBomb - Configuración del Juego");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Configuraci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 290, 330);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblGameName = new JLabel("Nombre");
		lblGameName.setBounds(12, 24, 125, 15);
		panel.add(lblGameName);
		
		GameNameTextField = new JTextField();
		GameNameTextField.setBounds(116, 22, 162, 19);
		panel.add(GameNameTextField);
		GameNameTextField.setColumns(10);
		
		GameLinkageStrategyComboBox = new JComboBox<AbstractLinkageStrategy>(new DefaultComboBoxModel<AbstractLinkageStrategy>(new AbstractLinkageStrategy[]{new RingLinkageStrategy()}));
		
		GameLinkageStrategyComboBox.setBounds(116, 114, 162, 24);
		panel.add(GameLinkageStrategyComboBox);
		
		JLabel lblGameMaxPlayers = new JLabel("Jugadores (máx.)");
		lblGameMaxPlayers.setBounds(12, 227, 125, 15);
		panel.add(lblGameMaxPlayers);
		
		GameMaxPlayersComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}));
		GameMaxPlayersComboBox.setBounds(155, 222, 123, 24);
		panel.add(GameMaxPlayersComboBox);
		
		GameRoundsComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{2, 3, 4}));
		GameRoundsComboBox.setBounds(155, 258, 123, 24);
		panel.add(GameRoundsComboBox);
		
		JLabel lblGameRounds = new JLabel("Rondas");
		lblGameRounds.setBounds(12, 263, 125, 15);
		panel.add(lblGameRounds);
		
		JLabel lblGameDuration = new JLabel("Duración");
		lblGameDuration.setBounds(12, 299, 125, 15);
		panel.add(lblGameDuration);
		
		GameRoundDurationComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{30, 60, 90}));
		GameRoundDurationComboBox.setBounds(155, 294, 123, 24);
		panel.add(GameRoundDurationComboBox);
		
		JLabel lblInetIPAddress = new JLabel("Dirección IP");
		lblInetIPAddress.setBounds(12, 56, 82, 15);
		panel.add(lblInetIPAddress);
		
		Vector<InetAddress> InetAddressVector = new Vector<InetAddress>();

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
			    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			    	InetAddress ipAddr = enumIpAddr.nextElement();
			    	
			    	// Que el Dios de los Objetos me perdone por esto...
			    	if (ipAddr.getClass().getSimpleName().equals("Inet4Address"))
			    	{
			    		InetAddressVector.add(ipAddr);
			    	}
			    }
			}
		} catch (SocketException e) {
			System.out.println(" (error retrieving network interface list)");
		}
		
		GameInetIPAddressComboBox = new JComboBox<InetAddress>(new DefaultComboBoxModel<InetAddress>(InetAddressVector));
		GameInetIPAddressComboBox.setBounds(116, 51, 162, 24);
		panel.add(GameInetIPAddressComboBox);
		
		GameInetPortTextField = new JTextField();
		GameInetPortTextField.setBounds(116, 83, 162, 19);
		panel.add(GameInetPortTextField);
		GameInetPortTextField.setColumns(10);
		
		JLabel lblInetPort = new JLabel("Puerto");
		lblInetPort.setBounds(12, 85, 48, 15);
		panel.add(lblInetPort);
		
		JLabel lblGameLinkageStrategy = new JLabel("Topología");
		lblGameLinkageStrategy.setBounds(12, 119, 125, 15);
		panel.add(lblGameLinkageStrategy);
		
		JLabel lblGameQuiz = new JLabel("Preguntas");
		lblGameQuiz.setBounds(12, 155, 125, 15);
		panel.add(lblGameQuiz);
		
		GameQuizComboBox = new JComboBox<Quiz>(new DefaultComboBoxModel<Quiz>(this.parentWindow.getQuizVector()));
		GameQuizComboBox.setBounds(116, 150, 162, 24);
		panel.add(GameQuizComboBox);
		
		GameModeComboBox = new JComboBox<AbstractGameMode>(new DefaultComboBoxModel<AbstractGameMode>(new AbstractGameMode[]{new NormalGameMode(), new BouncingGameMode()}));
		GameModeComboBox.setBounds(116, 186, 162, 24);
		panel.add(GameModeComboBox);
		
		JLabel lblGameMode = new JLabel("Modo");
		lblGameMode.setBounds(12, 191, 125, 15);
		panel.add(lblGameMode);
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView JBSGFV = JBombServerGameFormView.this;
				
				if (JBSGFV.isFormValid())
				{
					JBSGFV.Game.setName(JBSGFV.GameNameTextField.getText());
					JBSGFV.Game.setInetIPAddress((InetAddress)JBSGFV.GameInetIPAddressComboBox.getSelectedItem());
					JBSGFV.Game.setInetPort(Integer.parseInt(JBSGFV.GameInetPortTextField.getText()));
					JBSGFV.Game.setLinkageStrategy((AbstractLinkageStrategy)JBSGFV.GameLinkageStrategyComboBox.getSelectedItem());
					JBSGFV.Game.setQuiz((Quiz)JBSGFV.GameQuizComboBox.getSelectedItem());
					JBSGFV.Game.setMode((AbstractGameMode)JBSGFV.GameModeComboBox.getSelectedItem());
					JBSGFV.Game.setMaxGamePlayersAllowed((Integer)JBSGFV.GameMaxPlayersComboBox.getSelectedItem());
					JBSGFV.Game.setMaxRounds((Integer)JBSGFV.GameRoundsComboBox.getSelectedItem());					
					JBSGFV.Game.setRoundDuration((Integer)JBSGFV.GameRoundDurationComboBox.getSelectedItem());
										
					JBSGFV.parentWindow.addGame(JBombServerGameFormView.this.Game);		
					
					JBSGFV.dispose();			
				}
			}
		});
		btnNewButton.setBounds(185, 354, 117, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView.this.dispose();
			}
		});
		btnNewButton_1.setBounds(12, 354, 117, 25);
		contentPane.add(btnNewButton_1);
	}

	public Game getGame() {
		return Game;
	}

	public void setGame(Game game) {
		Game = game;
	}
	
	public Boolean isFormValid()
	{
		if (this.GameNameTextField.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Debe ingresar un nombre.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		if (this.GameInetIPAddressComboBox.getSelectedIndex() < 0)
		{
			JOptionPane.showMessageDialog(this, "Debe seleccionar una dirección IP.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		if (this.GameInetPortTextField.getText().equals(""))
		{			
			JOptionPane.showMessageDialog(this, "Debe ingresar un puerto.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		else
		{
			try
			{
				Integer.parseInt(this.GameInetPortTextField.getText());
			}
			catch (Exception e)
			{				
				JOptionPane.showMessageDialog(this, "El número de puerto es inválido.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
				
				return false;
			}			
		}
		
		if (this.GameLinkageStrategyComboBox.getSelectedIndex() < 0)
		{
			JOptionPane.showMessageDialog(this, "Debe seleccionar una topología.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		if (this.GameQuizComboBox.getSelectedIndex() < 0)
		{
			JOptionPane.showMessageDialog(this, "Debe seleccionar un cuestionario.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}		
		
		return true;
	}
}
