package hW1;
import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		if (args.length != 0) {
			System.err.println("Usage: java Server <port number>");
			System.exit(1);
		}
		
		int portNumber = 1025;
		
		try (
			ServerSocket serverSocket = 
				new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = 
					new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader stdIn =
					new BufferedReader(
						new InputStreamReader(System.in));
		){
			String inputLine;
			while((inputLine = in.readLine()) != null)	{
				out.println(inputLine);
				System.out.println(inputLine);
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " 
				+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}	
	}
}