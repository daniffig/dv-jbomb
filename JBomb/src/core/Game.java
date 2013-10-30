package core;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private List<GamePlayer> GamePlayers = new ArrayList<GamePlayer>();
	private Integer MaxRounds = 0;
	private Integer CurrentRound;
	private Integer MaxGamePlayersAllowed = 0;
	private Bomb Bomb = new Bomb();

	public List<GamePlayer> getGamePlayers() {
		return GamePlayers;
	}

	public void setGamePlayers(List<GamePlayer> gamePlayers) {
		GamePlayers = gamePlayers;
	}

	public Integer getMaxRounds() {
		return MaxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}

	public Integer getCurrentRound() {
		return CurrentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}

	public Integer getMaxGamePlayersAllowed() {
		return MaxGamePlayersAllowed;
	}

	public void setMaxGamePlayersAllowed(Integer maxGamePlayersAllowed) {
		MaxGamePlayersAllowed = maxGamePlayersAllowed;
	}

	public Boolean addGamePlayer(GamePlayer p) {
		if (this.canAddPlayer()) {
			this.getGamePlayers().add(p);

			return true;
		}

		return false;
	}

	public Boolean canAddPlayer() {
		return this.getGamePlayers().size() < this.getMaxGamePlayersAllowed();
	}

	public Boolean canSendBomb(GamePlayer sourceGamePlayer,
			GamePlayer destinationGamePlayer) {
		// TODO

		return true;
	}

	public void sendBomb(GamePlayer sourceGamePlayer,
			GamePlayer destinationGamePlayer) {
		if (this.canSendBomb(sourceGamePlayer, destinationGamePlayer)) {
			Bomb.setLastPlayer(sourceGamePlayer);
			Bomb.setCurrentPlayer(destinationGamePlayer);
		}
	}

	public void play() {

	}
}
