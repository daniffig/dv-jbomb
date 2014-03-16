package gameStates;

import reference.GameState;

public class WaitingGameState extends AbstractGameState {

	
	public WaitingGameState(){
		this.Type = GameState.WAITING;
	}
	@Override
	public String toString() {

		return "Esperando jugadores";
	}

}
