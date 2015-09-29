import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;


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
	    
	    String line;
	    
	    line = input.readLine();
	    System.out.println(line);
	    if(line != null){
	      processRequest(line, socket);
	    }	
	    
	    input.close();
	    socket.close();
	    clientNum--;
	}
	
	public void processRequest(String fileName, Socket socket) throws IOException{
		PrintStream output = new PrintStream(socket.getOutputStream(), true);
		InputStream fileIn = null;
		
		File file = new File ("../Data/" + fileName);
		fileIn = new FileInputStream(file);
		
		byte[] buffer = new byte[1024];
        int amountRead = fileIn.read(buffer);
        
        while(amountRead > 0){
			 output.write(buffer, 0, amountRead); // write data back out to an OutputStream
			 amountRead = fileIn.read(buffer);
		}
		
		output.println("");
		fileIn.close();
		output.println("");
	    System.out.println("File transfered.");
	}
}
