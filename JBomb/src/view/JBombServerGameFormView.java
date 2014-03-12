package view;

import core.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

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
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;

public class JBombServerGameFormView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JBombServerMainView parentWindow;

	private JPanel contentPane;
	
	private JTextField GameNameTextField;
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
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JBombServerGameFormView.class.getResource("/images/icon.png")));
		
		this.parentWindow = JBombServerMainView;
		
		this.setGame(Game);
		
		setTitle("JBomb - Configuración del Juego");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblGameName = new JLabel("Nombre");
		lblGameName.setBounds(12, 24, 55, 15);
		panel.add(lblGameName);
		
		GameNameTextField = new JTextField();
		GameNameTextField.setBounds(130, 24, 162, 19);
		panel.add(GameNameTextField);
		GameNameTextField.setColumns(10);
		
		GameLinkageStrategyComboBox = new JComboBox<AbstractLinkageStrategy>(new DefaultComboBoxModel<AbstractLinkageStrategy>(new AbstractLinkageStrategy[]{new RingLinkageStrategy(), new ConexantLinkageStrategy()}));
		
		GameLinkageStrategyComboBox.setBounds(130, 53, 162, 24);
		panel.add(GameLinkageStrategyComboBox);
		
		JLabel lblGameMaxPlayers = new JLabel("Jugadores (máx.)");
		lblGameMaxPlayers.setBounds(12, 164, 119, 15);
		panel.add(lblGameMaxPlayers);
		
		GameMaxPlayersComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}));
		GameMaxPlayersComboBox.setBounds(169, 161, 123, 24);
		panel.add(GameMaxPlayersComboBox);
		
		GameRoundsComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{2, 3, 4}));
		GameRoundsComboBox.setBounds(169, 197, 123, 24);
		panel.add(GameRoundsComboBox);
		
		JLabel lblGameRounds = new JLabel("Rondas");
		lblGameRounds.setBounds(12, 200, 53, 15);
		panel.add(lblGameRounds);
		
		JLabel lblGameDuration = new JLabel("Duración");
		lblGameDuration.setBounds(12, 236, 63, 15);
		panel.add(lblGameDuration);
		
		GameRoundDurationComboBox = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>(new Integer[]{30, 60, 90}));
		GameRoundDurationComboBox.setBounds(169, 233, 123, 24);
		panel.add(GameRoundDurationComboBox);
		
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
		
		JLabel lblGameLinkageStrategy = new JLabel("Topología");
		lblGameLinkageStrategy.setBounds(12, 56, 70, 15);
		panel.add(lblGameLinkageStrategy);
		
		JLabel lblGameQuiz = new JLabel("Preguntas");
		lblGameQuiz.setBounds(12, 92, 74, 15);
		panel.add(lblGameQuiz);
		
		GameQuizComboBox = new JComboBox<Quiz>(new DefaultComboBoxModel<Quiz>(this.parentWindow.getQuizVector()));
		GameQuizComboBox.setBounds(130, 89, 162, 24);
		panel.add(GameQuizComboBox);
		
		GameModeComboBox = new JComboBox<AbstractGameMode>(new DefaultComboBoxModel<AbstractGameMode>(new AbstractGameMode[]{new NormalGameMode(), new BouncingGameMode()}));
		GameModeComboBox.setBounds(130, 125, 162, 24);
		panel.add(GameModeComboBox);
		
		JLabel lblGameMode = new JLabel("Modo");
		lblGameMode.setBounds(12, 128, 39, 15);
		panel.add(lblGameMode);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnNewButton = new JButton("Guardar");
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancelar");
		panel_1.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView.this.dispose();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombServerGameFormView JBSGFV = JBombServerGameFormView.this;
				
				if (JBSGFV.isFormValid())
				{
					JBSGFV.Game.setName(JBSGFV.GameNameTextField.getText());
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
