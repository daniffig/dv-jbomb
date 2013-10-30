package linkageStrategies;

import java.util.List;

import core.GamePlayer;

public abstract class AbstractLinkageStrategy {

	public abstract String toString();
	public abstract void link(List<GamePlayer> gamePlayers);
}
