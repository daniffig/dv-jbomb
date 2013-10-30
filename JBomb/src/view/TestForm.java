package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

public class TestForm extends JPanel {

	private BindingGroup m_bindingGroup;
	private core.Player player = new core.Player();
	private JTextField playerNameJTextField;

	public TestForm(core.Player newPlayer) {
		this();
		setPlayer(newPlayer);
	}

	public TestForm() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0E-4 };
		setLayout(gridBagLayout);

		JLabel playerNameLabel = new JLabel("PlayerName:");
		GridBagConstraints labelGbc_0 = new GridBagConstraints();
		labelGbc_0.insets = new Insets(5, 5, 5, 5);
		labelGbc_0.gridx = 0;
		labelGbc_0.gridy = 0;
		add(playerNameLabel, labelGbc_0);

		playerNameJTextField = new JTextField();
		GridBagConstraints componentGbc_0 = new GridBagConstraints();
		componentGbc_0.insets = new Insets(5, 0, 5, 5);
		componentGbc_0.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_0.gridx = 1;
		componentGbc_0.gridy = 0;
		add(playerNameJTextField, componentGbc_0);

		if (player != null) {
			m_bindingGroup = initDataBindings();
		}
	}

	protected BindingGroup initDataBindings() {
		BeanProperty<core.Player, java.lang.String> playerNameProperty = BeanProperty
				.create("playerName");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty = BeanProperty
				.create("text");
		AutoBinding<core.Player, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, player,
						playerNameProperty, playerNameJTextField, textProperty);
		autoBinding.bind();
		//
		BindingGroup bindingGroup = new BindingGroup();
		bindingGroup.addBinding(autoBinding);
		//
		return bindingGroup;
	}

	public core.Player getPlayer() {
		return player;
	}

	public void setPlayer(core.Player newPlayer) {
		setPlayer(newPlayer, true);
	}

	public void setPlayer(core.Player newPlayer, boolean update) {
		player = newPlayer;
		if (update) {
			if (m_bindingGroup != null) {
				m_bindingGroup.unbind();
				m_bindingGroup = null;
			}
			if (player != null) {
				m_bindingGroup = initDataBindings();
			}
		}
	}

}
