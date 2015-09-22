import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private String method = null;
	private String path = null;
	private String protocol = null;	
	private int clientNum;
	
	@SuppressWarnings("resource")
	public Server() throws IOException{
		ServerSocket serverSocket = new ServerSocket(9876);
		
		for (clientNum = 0; true; clientNum++) {
			System.out.println("Connection Started: " + clientNum);
	    	final Socket socket = serverSocket.accept();
	    	new Thread(new Runnable() {
	    		@Override
	    		public void run() {
	    			try {
	    				handleConnection(socket);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}).start();
	    }	    
	}
	
	public void handleConnection(Socket socket) throws IOException{
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    PrintStream output = new PrintStream(socket.getOutputStream(), true);
	    
	    String line;
	    
	    line = input.readLine();
	    System.out.println(line);
	    if(line != null){
	      processRequest(line);
	    }	
	    
	    input.close();
	    output.close();
	    socket.close();
	    clientNum--;
	}
	
	public void processRequest(String fileName) throws IOException{
		
	}
}
