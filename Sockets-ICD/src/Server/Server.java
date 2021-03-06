package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{

	public final static int DEFAULT_PORT = 5025;

	private HashMap<String, HandleConnectionThread> alunos;

	public void run() {
		int port = DEFAULT_PORT;
		ServerSocket serverSocket = null;
		alunos = new HashMap<String, HandleConnectionThread>();

		try {
			// Criar socket com uma determinada porta
			serverSocket = new ServerSocket(port);
			System.out.println("Servidor aberto na porta: " + port);
			Socket newSock = null;

			for (;;) {
				// Espera connect do cliente
				newSock = serverSocket.accept();
				HandleConnectionThread a = new HandleConnectionThread(newSock, this);
				Thread th = a;
				th.start();

			}
		} catch (IOException e) {
			System.err.println("Excep��o no servidor: " + e);
		}

	} // end main

	public HashMap<String, HandleConnectionThread> getAlunos() {
		return alunos;
	}
} // end ServidorTCP
