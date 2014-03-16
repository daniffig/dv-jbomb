package gameStates;

import reference.GameState;

public class GameOverGameState extends AbstractGameState {


	public GameOverGameState(){
		this.Type = GameState.GAME_OVER;
	}
	
	@Override
	public String toString() {

		return "Terminado";
	}

}
