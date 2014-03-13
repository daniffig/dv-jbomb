package roundDurations;

public class ShortRoundDuration extends AbstractRoundDuration {

	@Override
	public String toString() {
		return "Corto - 30-60 segundos";
	}

	@Override
	public Integer getMinDuration() {
		return 30;
	}

	@Override
	public Integer getMaxDuration() {
		return 60;
	}

}
