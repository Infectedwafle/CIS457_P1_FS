/*
CIS 457
Project 1 part 2
Java client server file transfer
Jonathan Powers, Kevin Anderson, Brett Greenman
 */

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Files;
//Server class for the client to connect to.
public class Server {
	private String method = null;
	private String path = null;
	private String protocol = null;	
	private int clientNum;
	@SuppressWarnings("resource")
	public Server() throws IOException{
		ServerSocket serverSocket = new ServerSocket(9876);
		for (clientNum = 0; true; clientNum++) {
			System.out.println("Active Connections: " + clientNum);
			final Socket socket = serverSocket.accept();
			new Thread(new Runnable() {
				@Override
				public void run() {
				    try {
					handleConnection(socket);
				    } catch (IOException e) {
					System.out.println("Client disconnected unexpectedly.");
					clientNum--;
					System.out.println("Active Connections: " + clientNum);
				    }
				}
			    }).start();
		}	    
	}
    // This method handles the socket connection from the clients
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
    //This method takes the request from the client to find and send the file. 
    public void processRequest(String fileName, Socket socket) throws IOException{
	PrintStream output = new PrintStream(socket.getOutputStream(), true);
	InputStream fileIn = null;
	try{
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
	catch (FileNotFoundException e){
	    output.println("File does not exist");
	    System.out.println("File not found...");	    
	}
    }
}
