package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.BoxLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import core.*;

import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class JBombMainView {

	private JFrame frmJbombV;
	private JTextField newGamePlayerInput;
	private JTextField maxGamePlayersAllowedInput;
	private JTextField textField_2;
	
	private Game Game = new Game();
	private DefaultListModel<GamePlayer> GamePlayerListModel = new DefaultListModel<GamePlayer>();	

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
				
		final JList<GamePlayer> gamePlayersList = new JList<GamePlayer>(GamePlayerListModel);
		scrollPane.setViewportView(gamePlayersList);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.getGamePlayers().remove(GamePlayerListModel.get(gamePlayersList.getSelectedIndex()));
				
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
				
				if (Game.canAddPlayer())
				{
					GamePlayer GamePlayer = new GamePlayer(newGamePlayerInput.getText());
					
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
		maxGamePlayersAllowedInput.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (maxGamePlayersAllowedInput.getText().isEmpty())
				{
					newGamePlayerInput.setText(maxGamePlayersAllowedInput.getText());
				}
			}
		});
		maxGamePlayersAllowedInput.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
			}
			public void inputMethodTextChanged(InputMethodEvent arg0) {
				if (maxGamePlayersAllowedInput.getText().isEmpty())
				{
					newGamePlayerInput.setText("FedeeeeeeeeeeeEE");
				}
				
				Game.setMaxGamePlayersAllowed(Integer.valueOf(maxGamePlayersAllowedInput.getText()));
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
		
		textField_2 = new JTextField();
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		final JLabel maxGamePlayersAllowedLabel = new JLabel("");
		maxGamePlayersAllowedLabel.setBounds(112, 12, 100, 15);
		frmJbombV.getContentPane().add(maxGamePlayersAllowedLabel);
		
		maxGamePlayersAllowedLabel.setText("max. " + Game.getMaxGamePlayersAllowed());
		
		JButton saveGameConfigurationButton = new JButton("Guardar");
		saveGameConfigurationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!maxGamePlayersAllowedInput.getText().isEmpty() && Integer.valueOf(maxGamePlayersAllowedInput.getText()) > 0)
				{
					Integer maxGamePlayersAllowed = Integer.valueOf(maxGamePlayersAllowedInput.getText());
					
					Game.setMaxGamePlayersAllowed(maxGamePlayersAllowed);
					
					maxGamePlayersAllowedLabel.setText("max. " + maxGamePlayersAllowed);
				}
			}
		});
		saveGameConfigurationButton.setBounds(665, 222, 117, 25);
		frmJbombV.getContentPane().add(saveGameConfigurationButton);
		
		JButton btnjugar = new JButton("¡Jugar!");
		btnjugar.setBounds(665, 536, 117, 25);
		frmJbombV.getContentPane().add(btnjugar);
	}
}