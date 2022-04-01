package TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import comm.Session;
import model.Playing;
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
					System.out.println("Esperando cliente...");
					Socket socket = server.accept();
					System.out.println("Nuevo cliente conectado!");
					System.out.println("Entró en el puerto: " + socket.getPort());
					Session session = new Session(socket);
					session.setListener(this);
					session.start();
					sessions.add(session);
					
					if(sessions.size()%2==0) {
						new Thread(()->{
							Playing playing = new Playing(sessions.get(sessions.size()-1), sessions.get(sessions.size()-2));
						}).start();
					}
				}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void onMessage(String line) {
		System.out.println("OnMessage: "+line);
		
		String[] word = new String[5];
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
		boolean pos2 = false;
		boolean pos3 = false;
		int k=0;
		for(int i=0;i<word.length && !pos3;i++) {
			pos2 = false;
			for(int j=k;j<line.length() && !pos2 && !pos3;j++) {
				k++;
				if(line.charAt(j)!=':' && line.charAt(j)!='/') {
					word[i]+=line.charAt(j);
				}else if(line.charAt(j)=='/' || line.charAt(j)=='*'){
					pos3=true;
				}else {
					pos2=true;
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
		System.out.println("Sending: "+id);
		for(Session s : sessions) {
			if(s.getIdd().equals(id)) {
				s.addWords(line);
			}
		}
	}

	@Override
	public void onStop(int id) {
		try {
			sendBroadCast2(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendBroadCast2(int id) throws IOException {
		for(Session s : sessions) {
			if(s.getSocket().getPort()!=id) {
				s.sendMessage("finish");	
			}
		}
	}

	@Override
	public void onComparate() {
		Session p1 = sessions.get(0);
		Session p2 = sessions.get(1);
		
		String[] n = {"100","100","100","100"};
		String[] m = {"100","100","100","100"};
		
		if(p1.getWords().get(0).getName().equals(p2.getWords().get(0).getName())) {
			n[0]="50";
			m[0]="50";
		}
		if(p1.getWords().get(0).getAnimal().equals(p2.getWords().get(0).getAnimal())) {
			n[1]="50";
			m[1]="50";
		}
		if(p1.getWords().get(0).getCountry().equals(p2.getWords().get(0).getCountry())) {
			n[2]="50";
			m[2]="50";
		}
		if(p1.getWords().get(0).getThing().equals(p2.getWords().get(0).getThing())) {
			n[3]="50";
			m[3]="50";
		}
		
		if(p1.getWords().get(0).getName().equals("") || p1.getWords().get(0).getName().equals(null)) {
			n[0]="0";
		}
		
		if(p2.getWords().get(0).getName().equals("") || p2.getWords().get(0).getName().equals(null)) {
			m[0]="0";
		}
		
		if(p1.getWords().get(0).getAnimal().equals("") || p1.getWords().get(0).getAnimal().equals(null)) {
			n[1]="0";
		}
		
		if(p2.getWords().get(0).getAnimal().equals("") || p2.getWords().get(0).getAnimal().equals(null)) {
			m[1]="0";
		}
		
		if(p1.getWords().get(0).getCountry().equals("") || p1.getWords().get(0).getCountry().equals(null)) {
			n[2]="0";
		}
		if(p2.getWords().get(0).getCountry().equals("") || p2.getWords().get(0).getCountry().equals(null)) {
			m[2]="0";
		}
		
		if(p1.getWords().get(0).getThing().equals("") || p1.getWords().get(0).getThing().equals(null)) {
			n[3]="0";
		}
		if(p2.getWords().get(0).getThing().equals("") || p2.getWords().get(0).getThing().equals(null)) {
			m[3]="0";
		}
	}
	
	public void sendBroadCast3(String[] n, String[] m) throws IOException {
		Session p1 = sessions.get(0);
		Session p2 = sessions.get(1);
		
		String x = ":"+p1.getWords().get(0).getName()+";"+n[0]+
				":"+p1.getWords().get(0).getAnimal()+";"+n[1]+
				":"+p1.getWords().get(0).getCountry()+";"+n[2]+
				":"+p1.getWords().get(0).getThing()+";"+n[3];
		String y=":"+p2.getWords().get(0).getName()+";"+n[0]+
				":"+p2.getWords().get(0).getAnimal()+";"+n[1]+
				":"+p2.getWords().get(0).getCountry()+";"+n[2]+
				":"+p2.getWords().get(0).getThing()+";"+n[3];
		
		p1.sendMessage(x+y);
		p2.sendMessage(y+x);
		
	}
	

}
