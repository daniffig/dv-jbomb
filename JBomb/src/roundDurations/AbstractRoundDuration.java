package roundDurations;

public abstract class AbstractRoundDuration {

	protected Integer duration;
	
	public AbstractRoundDuration()
	{
		this.setDuration(this.getMinDuration() + (int)(Math.random() * ((this.getMaxDuration() - this.getMinDuration()) + 1)));
	}
	
	public abstract String toString();

	public abstract Integer getMinDuration();
	public abstract Integer getMaxDuration();

	public Integer getDuration() {
		
		//return 5;
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
