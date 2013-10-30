package core;

public class Bomb {

	private Integer DetonationMilliseconds;
	private Integer CurrentMilliseconds;
	private GamePlayer LastPlayer;
	private GamePlayer CurrentPlayer;

	public Integer getDetonationMilliseconds() {
		return DetonationMilliseconds;
	}

	public void setDetonationMilliseconds(Integer detonationMilliseconds) {
		DetonationMilliseconds = detonationMilliseconds;
	}

	public Integer getCurrentMilliseconds() {
		return CurrentMilliseconds;
	}

	public void setCurrentMilliseconds(Integer currentMilliseconds) {
		CurrentMilliseconds = currentMilliseconds;
	}

	public GamePlayer getLastPlayer() {
		return LastPlayer;
	}

	public void setLastPlayer(GamePlayer lastPlayer) {
		LastPlayer = lastPlayer;
	}

	public GamePlayer getCurrentPlayer() {
		return CurrentPlayer;
	}

	public void setCurrentPlayer(GamePlayer currentPlayer) {
		CurrentPlayer = currentPlayer;
	}
}
