package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JBombServerInetConfigurationFormView extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JBombServerMainView parentWindow;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField ServerInetIPAddressTextField;
	private JTextField ServerInetPortTextField;
	private JLabel lblServerInetIPAddress;
	private JLabel lblServerInetPort;

	/**
	 * Create the dialog.
	 */
	public JBombServerInetConfigurationFormView(JBombServerMainView JBombServerMainView) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.parentWindow = JBombServerMainView;
		
		setTitle("Configuración del Servidor");
		setBounds(100, 100, 450, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblServerInetIPAddress = new JLabel("Dirección IP");
			lblServerInetIPAddress.setBounds(12, 18, 82, 15);
			contentPanel.add(lblServerInetIPAddress);
		}
		{
			ServerInetIPAddressTextField = new JTextField();
			ServerInetIPAddressTextField.setBounds(112, 16, 114, 19);
			lblServerInetIPAddress.setLabelFor(ServerInetIPAddressTextField);
			contentPanel.add(ServerInetIPAddressTextField);
			ServerInetIPAddressTextField.setColumns(10);
			
			ServerInetIPAddressTextField.setText(this.parentWindow.getInetIPAddress());
		}
		{
			lblServerInetPort = new JLabel("Puerto");
			lblServerInetPort.setBounds(244, 18, 48, 15);
			contentPanel.add(lblServerInetPort);
		}
		{
			ServerInetPortTextField = new JTextField();
			lblServerInetPort.setLabelFor(ServerInetPortTextField);
			ServerInetPortTextField.setBounds(310, 16, 114, 19);
			contentPanel.add(ServerInetPortTextField);
			ServerInetPortTextField.setColumns(10);
			
			ServerInetPortTextField.setText(this.parentWindow.getInetPort().toString());
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnServerConfigurationSave = new JButton("Guardar");
				btnServerConfigurationSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if (JBombServerInetConfigurationFormView.this.isFormValid())
						{
							JBombServerInetConfigurationFormView.this.parentWindow.updateInetData(
									JBombServerInetConfigurationFormView.this.ServerInetIPAddressTextField.getText(),
									JBombServerInetConfigurationFormView.this.ServerInetPortTextField.getText()
							);
						
							JBombServerInetConfigurationFormView.this.dispose();
						}						
						
					}
				});
				btnServerConfigurationSave.setActionCommand("OK");
				buttonPane.add(btnServerConfigurationSave);
				getRootPane().setDefaultButton(btnServerConfigurationSave);
			}
			{
				JButton btnServerConfigurationCancel = new JButton("Cancelar");
				btnServerConfigurationCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JBombServerInetConfigurationFormView.this.dispose();
					}
				});
				btnServerConfigurationCancel.setActionCommand("Cancel");
				buttonPane.add(btnServerConfigurationCancel);
			}
		}
	}
	
	private Boolean isFormValid()
	{
		// TODO: Falta validación.
		
		return true;
	}

}
