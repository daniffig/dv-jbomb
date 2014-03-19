package gameModes;

public class NormalGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Normal";
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
