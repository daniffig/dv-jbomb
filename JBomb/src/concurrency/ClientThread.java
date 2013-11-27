package concurrency;

import java.net.Socket;

import core.Game;

public class ClientThread implements Runnable {

	private Socket client_socket;
	private Game current_game;
	
	public ClientThread(Socket s, Game g)
	{
		this.client_socket = s;
		this.current_game = g;
	}
	@Override
	public void run() {
		System.out.println("Conexión establecida! Thread # " + Thread.currentThread().getName() + " creado");

	}

}
