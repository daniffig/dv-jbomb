package roundDurations;

public class LongRoundDuration extends AbstractRoundDuration {

	@Override
	public String toString() {
		return "Largo - 2-4 minutos";
	}

	@Override
	public Integer getMinDuration() {
		return 120;
	}

	@Override
	public Integer getMaxDuration() {
		return 240;
	}

}
