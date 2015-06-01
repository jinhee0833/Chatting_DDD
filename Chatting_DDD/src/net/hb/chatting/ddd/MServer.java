package net.hb.chatting.ddd;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.net.ssl.SSLContext;

public class MServer extends Thread implements Runnable{
	Vector vc = new Vector();
	BufferedReader in;
	OutputStream out;
	Socket s;
	
	public MServer() {
		
	}
	public void messageAll(String msg){
		for(int i=0;i<vc.size();i++){
			try{
			 MServer sv=(MServer)vc.elementAt(i);
			 sv.message(msg);
			}catch(Exception ex){
				vc.removeElementAt(i--);
			}
		}
	}//messageAll end
	
	public void message(String msg) throws Exception {
		out.write((msg+"\n").getBytes());
		
	}//message end
	public void run(){
		ServerSocket ss = null;
		try{
			ss = new ServerSocket(8888);
			
		}catch(Exception ex){
			System.out.println("오류:"+ex);
			return;
		}
		while(true){
			try{
				System.out.println("클라이언트 접속대기중...");
				s = ss.accept();
			}catch(Exception ex){
				ex.printStackTrace(); 
				return;
			}
		}
	}//run end
	
	public static void main(String[] args) {
		MServer sv = new MServer();
		Thread tr = new Thread(sv);
	}//main end
}//Class END
