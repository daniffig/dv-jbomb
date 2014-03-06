package core;

@Deprecated
public class Player {

	private String PlayerName;

	public String toString() {
		return this.getPlayerName();
	}

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
}
