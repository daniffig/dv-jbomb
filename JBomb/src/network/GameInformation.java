package network;


import java.io.Serializable;
import java.util.Vector;

public class GameInformation implements Serializable {
	
	private String  Name;
	private Integer CurrentRound;
	private Integer MaxRounds;
	private String GamePlayersOverMaxGamePlayers;	
	private Integer RoundDuration;
	private String  GameMode;
	private String	GameState;
	
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
	
	public String getGamePlayersOverMaxGamePlayers() {
		return GamePlayersOverMaxGamePlayers;
	}

	public void setGamePlayersOverMaxGamePlayers(
			String gamePlayersOverMaxGamePlayers) {
		GamePlayersOverMaxGamePlayers = gamePlayersOverMaxGamePlayers;
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
	
	public Vector<Object> toVector()
	{
		Vector<Object> v = new Vector<Object>();
		
		v.add(this.getName());
		v.add(this.getGameMode());
		v.add(this.getGamePlayersOverMaxGamePlayers());
		v.add(this.getGameState());
		
		return v;
	}

	public String getGameState() {
		return GameState;
	}

	public void setGameState(String gameState) {
		GameState = gameState;
	}
}
