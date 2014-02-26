package linkageStrategies;

import java.util.List;

import core.GamePlayer;

public class ConexantLinkageStrategy extends AbstractLinkageStrategy {
	
	public String toString()
	{
		return "Conexa";
	}

	@Override
	public void link(List<GamePlayer> gamePlayers) {
		for (int i = 0; i < gamePlayers.size(); i++)
		{
			gamePlayers.get(i).addNeighbour(gamePlayers.get((i+1)%gamePlayers.size()));
			gamePlayers.get(i).addNeighbour(gamePlayers.get((i+2)%gamePlayers.size()));
		}
	}
		
}
