package concurrency;

import java.net.Socket;

public class ClientThread implements Runnable {

	private Socket client_socket;
	
	public ClientThread(Socket s)
	{
		this.client_socket = s;
	}
	@Override
	public void run() {
		System.out.println("Conexi�n establecida! Thread # " + Thread.currentThread().getName() + " creado");

	}

}
