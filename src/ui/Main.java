package ui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import TCPConnection.TCPConnection;
import comm.Session;
import model.Words;

public class Main implements Session.OnMessageListener{
	
	public static void main(String[] args) throws IOException {
		TCPConnection tcp = TCPConnection.getInstance();
		tcp.start();
	}

	@Override
	public void onMessage(String line) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(String line) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComparate() {
		// TODO Auto-generated method stub
		
	}

}
