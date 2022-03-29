package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import model.Words;


public class Session extends Thread {

	private Socket socket;
	private String id;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private OnMessageListener listener;
	
	private ArrayList<Words> words;
	
	public ArrayList<Words> getWords() {
		return words;
	}

	public Session(Socket socket) {
		words = new ArrayList<Words>();
		this.socket = socket;
		id = UUID.randomUUID().toString();

	}
	
	@Override
	public void run() {
		try {
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while(true) {
				String line = reader.readLine();
				System.out.println(line);
				if(line.startsWith(":")) {
					listener.onMessage(line+"/"+id);
					listener.onStop(id);
				}else if(line.startsWith("*")) {
					listener.onMessage(line+"/"+id);
					listener.onComparate();
				}	
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void addWords(String[] msg){
		Words word = new Words(msg[0], msg[1],msg[2], msg[3]);
		words.add(word);
	}
	
	
	
	public void sendMessage(String msg) throws IOException {
		writer.write(msg+"\n");
		writer.flush();
	}
	
	public void setListener(OnMessageListener listener) {
		this.listener = listener;
	}
	
	public String getIdd() {
		return id;
	}

	public interface OnMessageListener{
		void onMessage(String line);
		void onStop(String line);
		void onComparate();
	}

}
