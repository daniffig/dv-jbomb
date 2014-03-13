package core;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Observable{

	private Long DetonationMilliseconds = 0L;
	private Long RemainingMilliseconds = 0L;
	
	private Long TimeOnActivation;
	
	private GamePlayer LastPlayer;
	private GamePlayer CurrentPlayer;
	private Boolean IsActive;
	private Timer Timer;
	
	public Bomb()
	{
		super();
	}
	
	public Bomb(Long DetonationMilliseconds, TimerTask DetonationTask)
	{
		this.setDetonationMilliseconds(DetonationMilliseconds);
	}
	
	public Bomb(Long DetonationMilliseconds)
	{
		this.setDetonationMilliseconds(DetonationMilliseconds);
	}
	
	public void activate()
	{	
		this.setIsActive(true);
		
		this.setTimer(new Timer());
		
		this.getTimer().schedule(this.getDetonationTask(), this.getRemainingMilliseconds());
		
		this.TimeOnActivation = System.currentTimeMillis();
		
		System.out.println("Active la bomba con tiempo restante " + this.getRemainingMilliseconds() + "ml");
	}
	
	public void deactivate()
	{
		this.setIsActive(false);
		
		this.getTimer().cancel();
		
		this.RemainingMilliseconds = this.getRemainingMilliseconds() - (System.currentTimeMillis() - this.TimeOnActivation);
	}
	
	public Long getDetonationMilliseconds() {
		return DetonationMilliseconds;
	}

	public void setDetonationMilliseconds(Long detonationMilliseconds) {
		DetonationMilliseconds = detonationMilliseconds;
		
		this.setRemainingMilliseconds(DetonationMilliseconds);
		
		System.out.println("Setee la bomba con un tiempo de detonación de " + this.getDetonationMilliseconds() + "ms");
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
		return (this.getRemainingMilliseconds() <= 0);
	}

	public Long getRemainingMilliseconds() {
		return RemainingMilliseconds;
	}

	public void setRemainingMilliseconds(Long RemainingMilliseconds) {
		this.RemainingMilliseconds = RemainingMilliseconds;
	}

	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}

	public Timer getTimer() {
		return Timer;
	}

	public void setTimer(Timer timer) {
		Timer = timer;
	}

	public TimerTask getDetonationTask() {
		return new TimerTask(){
			public void run(){
				notifyObservers(CurrentPlayer);
			}
		};
	}

}
