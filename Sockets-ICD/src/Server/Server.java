package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public final static int DEFAULT_PORT = 5025;

	public ArrayList<String> alunos;

	public Server() {
		int port = DEFAULT_PORT;
		ServerSocket serverSocket = null;
		alunos = new ArrayList<String>();

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

	public static void main(String args[]) {
		new Server();
	}

	public ArrayList<String> getAlunos() {
		return alunos;
	}
} // end ServidorTCP
