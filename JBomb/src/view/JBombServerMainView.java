package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

public class JBombServerMainView {

	private JFrame frmJbombV;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombServerMainView window = new JBombServerMainView();
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
	public JBombServerMainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJbombV = new JFrame();
		frmJbombV.setTitle("JBomb v0.2 - Servidor");
		frmJbombV.setResizable(false);
		frmJbombV.setBounds(100, 100, 450, 300);
		frmJbombV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJbombV.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(12, 39, 420, 120);
		frmJbombV.getContentPane().add(table);
		
		JLabel lblJuegosActivos = new JLabel("Últimos Juegos");
		lblJuegosActivos.setBounds(12, 12, 420, 15);
		frmJbombV.getContentPane().add(lblJuegosActivos);
		
		JButton btnNewButton = new JButton("Nuevo Juego");
		btnNewButton.setBounds(282, 171, 150, 25);
		frmJbombV.getContentPane().add(btnNewButton);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(12, 171, 117, 25);
		frmJbombV.getContentPane().add(btnEliminar);
	}
}
