package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class QuizQuestionFormView extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizQuestionFormView frame = new QuizQuestionFormView();
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
	public QuizQuestionFormView() {
		setTitle("Respuestas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblListadoDeRespuestas = new JLabel("Listado de Respuestas");
		lblListadoDeRespuestas.setBounds(12, 12, 163, 15);
		contentPane.add(lblListadoDeRespuestas);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 39, 309, 198);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnNewButton = new JButton("Nueva");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(333, 39, 99, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Guardar");
		btnNewButton_1.setBounds(333, 212, 99, 25);
		contentPane.add(btnNewButton_1);
		
		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBounds(333, 76, 99, 25);
		contentPane.add(btnModificar);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(333, 113, 99, 25);
		contentPane.add(btnEliminar);
		
		JButton btnNewButton_2 = new JButton("Correcta");
		btnNewButton_2.setBounds(333, 175, 99, 25);
		contentPane.add(btnNewButton_2);
	}

}
