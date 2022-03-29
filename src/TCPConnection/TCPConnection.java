package TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import comm.Session;
import model.Words;

public class TCPConnection extends Thread implements Session.OnMessageListener{
	
	private static TCPConnection instance = null;
	
	public static synchronized TCPConnection getInstance() throws IOException {
		if(instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}
	
	private ArrayList<Session> sessions;

	public TCPConnection() throws IOException {
		sessions = new ArrayList<>();
	}
	
	@Override
	public void run() {
		ServerSocket server;
		try {
			server = new ServerSocket(6000);
			while(true) {
				while (sessions.size()<=2) {
					System.out.println("Esperando cliente...");
					Socket socket = server.accept();
					System.out.println("Nuevo cliente conectado!");
					System.out.println("Entró en el puerto: " + socket.getPort());
					Session session = new Session(socket);
					session.setListener(this);
					session.start();
					sessions.add(session);
					
					if(sessions.size()==2) {
						for(Session s: sessions) {
							s.sendMessage("start");
						}
					}
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

	@Override
	public void onMessage(String line) {
		String[] word = new String[3];
		String id = new String();
		boolean pos = false;
		for(int i=0;i<line.length() && !pos;i++) {
			if(line.charAt(i)=='/') {
				for(int j=i+1;j<line.length();j++) {
					id += String.valueOf(line.charAt(j));			
				}
				pos=true;
			}
		}
		for(int i=0;i<word.length;i++) {
			for(int j=0;j<line.length();i++) {
				if(line.charAt(j)!=':') {
					word[i]+=line.charAt(j);
				}
			}
		}
		try {
			sendBroadCast(word, id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendBroadCast(String[] line, String id) throws IOException {
		for(Session s : sessions) {
			if(s.getIdd()==id) {
				s.addWords(line);
			}
		}
	}

	@Override
	public void onStop(String id) {
		try {
			sendBroadCast2(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendBroadCast2(String id) throws IOException {
		for(Session s : sessions) {
			if(s.getIdd()!=id) {
				s.sendMessage("finish");

			}
		}
	}

}
