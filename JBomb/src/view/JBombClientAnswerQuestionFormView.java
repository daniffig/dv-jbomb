package view;

import network.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JButton;

import core.Game;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@Deprecated //Utilizando el cliente android
public class JBombClientAnswerQuestionFormView extends JFrame {
/*
	private JPanel contentPane;
	
	private JBombGamePlayView parentWindow;
	
	private JComboBox<String> QuizAnswerComboBox;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JBombClientAnswerQuestionFormView frame = new JBombClientAnswerQuestionFormView(new JBombGamePlayView(new GameClient()));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JBombClientAnswerQuestionFormView(JBombGamePlayView JBombGamePlayView) {
		
		this.parentWindow = JBombGamePlayView;
		
		setTitle("¡Tienes la bomba!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("00:00");
		lblNewLabel.setForeground(Color.GRAY);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 64));
		lblNewLabel.setBounds(226, 12, 206, 76);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(JBombClientAnswerQuestionFormView.class.getResource("/images/Bomb64.png")));
		lblNewLabel_1.setBounds(12, 12, 64, 64);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("¿De qué color era el caballo blanco de San Martín?");
		lblNewLabel_2.setBounds(12, 100, 420, 15);
		contentPane.add(lblNewLabel_2);
		
		QuizAnswerComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[]{"Respuesta 01", "Respuesta 02", "Respuesta 03"}));
		QuizAnswerComboBox.setBounds(12, 127, 420, 24);
		QuizAnswerComboBox.setSelectedIndex(-1);
		contentPane.add(QuizAnswerComboBox);
		
		JButton btnResponder = new JButton("Responder");
		btnResponder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView JBCAQF = JBombClientAnswerQuestionFormView.this;
				
				if (JBCAQF.isFormValid())
				{
					// TODO: Poner la lógica para responder al servidor.
					JBCAQF.dispose();					
				}

			}
		});
		btnResponder.setBounds(321, 163, 111, 25);
		contentPane.add(btnResponder);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JBombClientAnswerQuestionFormView.this.dispose();
			}
		});
		btnCancelar.setBounds(12, 163, 96, 25);
		contentPane.add(btnCancelar);
	}
	
	public Boolean isFormValid()
	{
		if (this.QuizAnswerComboBox.getSelectedIndex() < 0)
		{
			JOptionPane.showMessageDialog(this, "Debe seleccionar una respuesta.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
			
			return false;
		}
		
		return true;
	}*/
}
