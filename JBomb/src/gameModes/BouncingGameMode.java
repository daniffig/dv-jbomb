package gameModes;

import core.GamePlayer;

public class BouncingGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Rebote";
	}
	
	public boolean handleNextPlayerRequest(GamePlayer TargetPlayer){
		System.out.println("Estoy en Bouncing Mode");
		
		this.getGame().getBomb().sendTo(TargetPlayer);
		
		return true;
	}
	
	public boolean sendBomb(boolean IsRightAnswer){
		if(IsRightAnswer)
		{
			this.getGame().getBomb().bounceBomb();
			
			return true;
		}
		
		return false;
	}

}
