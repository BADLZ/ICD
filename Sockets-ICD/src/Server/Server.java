package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public final static int DEFAULT_PORT = 5025;

	static ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
	public ArrayList<String> alunos = new ArrayList<String>();

	public Server(){
		int port = DEFAULT_PORT;
		ServerSocket serverSocket = null;

		try {
			// Criar socket com uma determinada porta
			serverSocket = new ServerSocket(port);
			System.out.println("Servidor aberto na porta: " + port);
			Socket newSock = null;

			for (;;) {
				// Espera connect do cliente
				newSock = serverSocket.accept();
				if (!addresses.contains(newSock.getInetAddress())) {
					addresses.add(newSock.getInetAddress());
					HandleConnectionThread a = new HandleConnectionThread(newSock, this);
					alunos.add(a.name);
					Thread th = a;
					th.start();
				}
			}
		} catch (IOException e) {
			System.err.println("Excepção no servidor: " + e);
		}

	} // end main
	public static void main (String args[]) {
		Server s = new Server();
	}
} // end ServidorTCP


