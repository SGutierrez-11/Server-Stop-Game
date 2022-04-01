package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import comm.Session;

public class Playing {
	
	private Session s1;
	private Session s2;
	//private ArrayList<Session> sessions;
	
	public Playing(Session s1, Session s2) {
		this.setS1(s1);
		this.setS2(s2);
		//sessions = new ArrayList<>();
		//sessions.add(s1);
		//sessions.add(s2);
		startPlaying();
	}
	
	public void startPlaying() {
		Random random = new Random();

        char randomizedCharacter = (char) (random.nextInt(26) + 'A');
        System.out.println("Generated Random Character: " + randomizedCharacter);
        
			try {
				s1.sendMessage("start:"+randomizedCharacter);
				s2.sendMessage("start:"+randomizedCharacter);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public Session getS1() {
		return s1;
	}

	public void setS1(Session s1) {
		this.s1 = s1;
	}

	public Session getS2() {
		return s2;
	}

	public void setS2(Session s2) {
		this.s2 = s2;
	}
	
}
