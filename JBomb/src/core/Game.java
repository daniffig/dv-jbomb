package core;

import java.util.List;

public class Game {

	private List<Player> Players;
	private Integer MaxRounds;
	private Integer CurrentRound;
	private Integer MaxPlayersAllowed;

	public List<Player> getPlayers() {
		return Players;
	}

	public void setPlayers(List<Player> players) {
		Players = players;
	}

	public Integer getMaxRounds() {
		return MaxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}

	public Integer getCurrentRound() {
		return CurrentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}

	public Integer getMaxPlayersAllowed() {
		return MaxPlayersAllowed;
	}

	public void setMaxPlayersAllowed(Integer maxPlayersAllowed) {
		MaxPlayersAllowed = maxPlayersAllowed;
	}
	
	public Boolean addPlayer(Player p)
	{
		if (this.getPlayers().size() < this.getMaxPlayersAllowed())
		{
			this.getPlayers().add(p);
			
			return true;
		}
		
		return false;
	}
}
