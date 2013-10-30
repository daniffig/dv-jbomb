package core;

import java.util.Date;

public class Bomb {

	private Long DetonationMilliseconds;
	private Long CurrentMilliseconds;
	private GamePlayer LastPlayer;
	private GamePlayer CurrentPlayer;

	public Long getDetonationMilliseconds() {
		return DetonationMilliseconds;
	}

	public void setDetonationMilliseconds(Long detonationMilliseconds) {
		DetonationMilliseconds = detonationMilliseconds;
	}

	public Long getCurrentMilliseconds() {
		return CurrentMilliseconds;
	}

	public void setCurrentMilliseconds(Long currentMilliseconds) {
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
	
	public Boolean isDetonated()
	{
		//return ((new Date()).getTime() >= this.getDetonationMilliseconds());
		// TODO
		return false;
	}
}
