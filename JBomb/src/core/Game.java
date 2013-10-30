package core;

import java.util.List;

public class Game {

	private List<GamePlayer> GamePlayers;
	private Integer MaxRounds;
	private Integer CurrentRound;
	private Integer MaxGamePlayersAllowed;

	public List<GamePlayer> getGamePlayers() {
		return GamePlayers;
	}

	public void setGamePlayers(List<GamePlayer> gamePlayers) {
		GamePlayers = gamePlayers;
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

	public Integer getMaxGamePlayersAllowed() {
		return MaxGamePlayersAllowed;
	}

	public void setMaxGamePlayersAllowed(Integer maxGamePlayersAllowed) {
		MaxGamePlayersAllowed = maxGamePlayersAllowed;
	}
	
	public Boolean addGamePlayer(GamePlayer p)
	{
		if (this.getGamePlayers().size() < this.getMaxGamePlayersAllowed())
		{
			this.getGamePlayers().add(p);
			
			return true;
		}
		
		return false;
	}
}
