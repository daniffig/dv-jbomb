package core;

public class Bomb {

	private Integer DetonationMilliseconds;
	private Integer CurrentMilliseconds;
	
	public Integer getDetonationMilliseconds() {
		return DetonationMilliseconds;
	}
	public void setDetonationMilliseconds(Integer detonationMilliseconds) {
		DetonationMilliseconds = detonationMilliseconds;
	}
	public Integer getCurrentMilliseconds() {
		return CurrentMilliseconds;
	}
	public void setCurrentMilliseconds(Integer currentMilliseconds) {
		CurrentMilliseconds = currentMilliseconds;
	}
}
