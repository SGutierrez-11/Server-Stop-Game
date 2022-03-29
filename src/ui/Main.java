package ui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import comm.Session;
import model.Words;

public class Main implements Session.OnMessageListener{

	public static void main(String[] args) throws IOException {
		new Main();
	}

	private ArrayList<Session> sessions;

	public Main() throws IOException {
		sessions = new ArrayList<>();
		ServerSocket server = new ServerSocket(6000);
		while (true) {
			System.out.println("Esperando cliente...");
			Socket socket = server.accept();
			System.out.println("Nuevo cliente conectado!");
			System.out.println("Entró en el puerto: " + socket.getPort());
			Session session = new Session(socket);
			session.setListener(this);
			session.start();
			sessions.add(session);
			words = new ArrayList<Words>();
		}
	}
	
	
	
	private ArrayList<Words> words;
	
	
	
	public void addWords(String name, String animal, String country, String thing) {
		Words word = new Words(name, animal, country, thing);
		words.add(word);
	}
	
	public ArrayList<Words> getWords() {
		return words;
	}

	@Override
	public void onMessage(String line) {
		if(line.startsWith("ALL::")) {
			try {
				sendBroadCast(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendBroadCast(String line) throws IOException {
		for(Session s : sessions) {
			s.sendMessage(line);
		}
	}

}
