package linkageStrategies;

import java.util.List;

import core.GamePlayer;

public class RingLinkageStrategy extends AbstractLinkageStrategy {
	
	public String toString()
	{
		return "Anillo";
	}

	@Override
	public void link(List<GamePlayer> gamePlayers) {
		for (int i = 0; i < gamePlayers.size() - 1; i++)
		{
			gamePlayers.get(i).addNeighbour(gamePlayers.get(i + 1));
		}
	}
		
}
