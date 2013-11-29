package network;


import java.io.Serializable;
import java.util.Vector;

public class GameInformation implements Serializable {
	
	private String  Name;
	private Integer CurrentRound;
	private Integer MaxRounds;
	private Integer TotalPlayers;
	private Integer MaxGamePlayersAllowed;
	private Integer RoundDuration;
	private String  GameMode;
	
	private Vector<String> AdjacentPlayers = new Vector<String>();
	
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getCurrentRound() {
		return CurrentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}

	public Integer getMaxRounds() {
		return MaxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}

	public Integer getTotalPlayers() {
		return TotalPlayers;
	}

	public void setTotalPlayers(Integer totalPlayers) {
		TotalPlayers = totalPlayers;
	}

	public Integer getMaxGamePlayersAllowed() {
		return MaxGamePlayersAllowed;
	}

	public void setMaxGamePlayersAllowed(Integer maxGamePlayersAllowed) {
		MaxGamePlayersAllowed = maxGamePlayersAllowed;
	}

	public Integer getRoundDuration() {
		return RoundDuration;
	}

	public void setRoundDuration(Integer roundDuration) {
		RoundDuration = roundDuration;
	}

	public String getGameMode() {
		return GameMode;
	}

	public void setGameMode(String gameMode) {
		GameMode = gameMode;
	}

	public Vector<String> getAdjacentPlayers() {
		return AdjacentPlayers;
	}

	public void setAdjacentPlayers(Vector<String> adjacentPlayers) {
		AdjacentPlayers = adjacentPlayers;
	}
}
