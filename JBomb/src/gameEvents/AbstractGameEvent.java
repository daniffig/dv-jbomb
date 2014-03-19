package gameEvents;

import concurrency.ClientThread;

public abstract class AbstractGameEvent {

	public abstract void handle(ClientThread ClientThread);
}
