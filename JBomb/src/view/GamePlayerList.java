package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.GamePlayer;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GamePlayerList extends JFrame {

	private JPanel contentPane;
	
	private List<GamePlayer> GamePlayers;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GamePlayerList frame = new GamePlayerList();
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
	public GamePlayerList() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 240);
		contentPane = new JPanel();
		
		JPanel panel = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final DefaultListModel<GamePlayer> listModel = new DefaultListModel<GamePlayer>();
		
		GamePlayer andres = new GamePlayer(),
				lucio = new GamePlayer(),
				duilio = new GamePlayer(),
				matias = new GamePlayer(),
				federico = new GamePlayer();
		
		andres.setName("Andrés");
		lucio.setName("Lucio");
		duilio.setName("Duilio");
		matias.setName("Matías");
		federico.setName("Federico");
		
		listModel.addElement(andres);
		listModel.addElement(lucio);
		listModel.addElement(duilio);
		listModel.addElement(matias);
		listModel.addElement(federico);
		
		final JList<GamePlayer> list = new JList<GamePlayer>(listModel);
		list.setBounds(12, 39, 410, 110);
		contentPane.add(list);
		
//		JScrollPane listScroller = new JScrollPane(list);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		
		JLabel lblJugadores = new JLabel("Jugadores");
		lblJugadores.setBounds(12, 12, 410, 15);
		contentPane.add(lblJugadores);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModel.remove(list.getSelectedIndex());
			}
		});
		btnEliminar.setBounds(12, 161, 117, 25);
		contentPane.add(btnEliminar);
		
		textField = new JTextField();
		textField.setBounds(141, 161, 152, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GamePlayer newGamePlayer = new GamePlayer();
				
				newGamePlayer.setName(textField.getText());
				
				listModel.addElement(newGamePlayer);
				
				textField.setText("");
			}
		});
		btnAgregar.setBounds(305, 161, 117, 25);
		contentPane.add(btnAgregar);
	}
}
