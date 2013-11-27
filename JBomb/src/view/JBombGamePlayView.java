package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
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
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	}

}
