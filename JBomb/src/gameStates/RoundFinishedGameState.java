package gameStates;

import reference.GameState;

public class RoundFinishedGameState extends AbstractGameState {

	
	public RoundFinishedGameState(){
		this.Type = GameState.ROUND_FINISHED;
	}
	
	@Override
	public String toString() {

		return "Ronda finalizada";
	}

}
