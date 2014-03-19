package gameModes;

public class BouncingGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Rebote";
	}
	
	public boolean handleNextPlayerRequest(){
		return true;
	}
	
	public boolean sendBomb(boolean IsRightAnswer){
		if(IsRightAnswer)
		{
			this.getGame().getBomb().sendToTarget();
			
			return true;
		}
		
		return false;
	}

}
