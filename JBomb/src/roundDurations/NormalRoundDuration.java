package roundDurations;

public class NormalRoundDuration extends AbstractRoundDuration {

	@Override
	public String toString() {
		return "Normal - 1-2 minutos";
	}

	@Override
	public Integer getMinDuration() {
		return 60;
	}

	@Override
	public Integer getMaxDuration() {
		return 120;
	}

}
