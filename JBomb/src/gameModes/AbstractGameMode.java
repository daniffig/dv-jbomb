package gameModes;

import core.Game;

public abstract class AbstractGameMode {

	private Game Game;
	
	public abstract String toString();
	
	public abstract boolean handleNextPlayerRequest();
	
	public abstract boolean sendBomb(boolean IsRightAnswer);
	
	public Game getGame() {
		return Game;
	}

	public void setGame(Game game) {
		Game = game;
	}

}
