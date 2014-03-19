package gameModes;

import core.Game;
import core.GamePlayer;

public abstract class AbstractGameMode {

	private Game Game;
	
	public abstract String toString();
	
	//Retorna true si cambi� el due�o de la bomba
	public abstract boolean handleNextPlayerRequest(GamePlayer TargetPlayer);
	
	public abstract boolean sendBomb(boolean IsRightAnswer);
	
	public Game getGame() {
		return Game;
	}

	public void setGame(Game game) {
		Game = game;
	}

}
