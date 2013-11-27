package core;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb {

	private Long DetonationMilliseconds;
	private Long LastActivationMilliseconds;
	private Long CurrentMilliseconds;
	private GamePlayer LastPlayer;
	private GamePlayer CurrentPlayer;
	private Boolean IsActive;
	private Timer Timer;
	private TimerTask DetonationTask;
	
	public static void main(String[] args)
	{
		Bomb B = new Bomb(2000L, new TimerTask(){
			
			public void run()
			{
				System.out.println("KABOOOOOOOOOOOOOOOOOOOM!!!");
			}
		});
		
		B.activate();
	}
	
	public Bomb(Long DetonationMilliseconds, TimerTask DetonationTask)
	{
		this.setDetonationMilliseconds(DetonationMilliseconds);
		this.setCurrentMilliseconds(0L);
		this.setDetonationTask(DetonationTask);
	}
	
	public void activate()
	{		
		this.setTimer(new Timer());
		
		this.getTimer().schedule(this.getDetonationTask(), this.getRemainingMilliseconds());
		
		this.setLastActivationMilliseconds(System.currentTimeMillis());
	}
	
	public void deactivate()
	{
		this.getTimer().cancel();
		
		this.setCurrentMilliseconds(System.currentTimeMillis() - this.getLastActivationMilliseconds());
	}
	
	public void detonate()
	{
		// TODO: ¿Qué hacemos cuando detonamos?
	}
	
	public Long getRemainingMilliseconds()
	{
		return this.getDetonationMilliseconds() - this.getCurrentMilliseconds();
	}

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

	public Long getLastActivationMilliseconds() {
		return LastActivationMilliseconds;
	}

	public void setLastActivationMilliseconds(Long lastActivationMilliseconds) {
		LastActivationMilliseconds = lastActivationMilliseconds;
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
		return DetonationTask;
	}

	public void setDetonationTask(TimerTask detonationTask) {
		DetonationTask = detonationTask;
	}
}
