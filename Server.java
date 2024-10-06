
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int BUFFER_SIZE = 4096;
    private Socket connection;
    private ServerSocket socket;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private FileInputStream fileIn;
    private String filename;
    private int bytes;
    private byte[] buffer = new byte[BUFFER_SIZE];

    public Server(String args[], int port) {

        int DEBUG = 0;
        float percentageSent = 10;

        if (args.length > 0) {
            for (String arg : args) {
                // Check if the argument starts with "DEBUG="
                if (arg.startsWith("DEBUG=")) {
                    // Extract the value after the "DEBUG="
                    String debugValue = arg.split("=")[1];
                    try {
                        // Convert the value to an integer
                        DEBUG = Integer.parseInt(debugValue);
                       // System.out.println("Debug mode is set to: " + debugMode);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid DEBUG value: " + debugValue);
                    }
                }
            }
        }
            

        try {
            socket = new ServerSocket(port);
            
            // Wait for connection and process it
            while (true) {

                try {
                    
                    connection = socket.accept(); // Block for connection request

                    
                    socketIn = new DataInputStream(connection.getInputStream()); // Read data from client
                    socketOut = new DataOutputStream(connection.getOutputStream()); // Write data to client
                    
                    filename = socketIn.readUTF(); // Read filename from client
                    File file = new File(filename); // Create a File object
                    float fileSize = file.length(); // Get the file size
                    float totalBytesSent = 0; // Keep track of the total bytes sent
                    
                    if(DEBUG == 1){
                    System.out.println("Sending " + file + " to " + socket.getInetAddress().getHostAddress());
                    }

                    fileIn = new FileInputStream(filename);
                    //System.out.println("Sending " + filename + " to " + socket.getInetAddress().getHostAddress() );

                    // Write file contents to client
                    while (true) {
                        bytes = fileIn.read(buffer, 0, BUFFER_SIZE); // Read from file
                        if (bytes <= 0) break; // Check for end of file
                        totalBytesSent += bytes; // Update the total bytes sent

                        // Calculate the percentage of bytes sent
                        //int percentageSent = (int) ((totalBytesSent * 100) / fileSize);
                        percentageSent = (totalBytesSent / fileSize) * 100;
                        // Print progress for each 10% increment, ensuring we do not repeat the 100% message
                       /*if (percentageSent >= 10 && percentageSent % 10 == 0 || totalBytesSent == fileSize) {
                            System.out.println("Sent " + percentageSent + "% of " + filename);
                        }
                        */
                        if(DEBUG == 1){
                        if(bytes != -1 && fileSize <= BUFFER_SIZE && (Math.floor(percentageSent) % 10 == 0 || Math.ceil(percentageSent) % 10 == 0)){
                            percentageSent = 10;
                            for(int i = 0; i <= 9; i++){
                                //System.out.println("filesize < BUFFER_SIZE: " + fileSize + " < " + BUFFER_SIZE);
                                System.out.printf("Sent %.0f%% of %s\n", percentageSent, filename);
                                percentageSent += 10;
                            }
                        } else {

                            if(Math.floor(percentageSent) % 10 == 0){
                                //System.out.println("in mod 10 case");
                                System.out.printf("Sent %.0f%% of %s\n", Math.floor(percentageSent), filename);
                            } else if(Math.ceil(percentageSent) % 10 == 0){  
                                System.out.printf("Sent %.0f%% of %s\n", Math.ceil(percentageSent), filename);
                            }
                        }
                    }
                        // Send the actual bytes read from the buffer to the client
                        socketOut.write(buffer, 0, bytes); // Write bytes to socket
                       
                        }

                       // if(bytes <= 0) break;
                
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                } finally {
                    // Clean up socket and file streams
                    if (connection != null) {
                        connection.close();
                        if(DEBUG == 1){
                        System.out.println("Finished sending " + filename + " to " + socket.getInetAddress().getHostAddress());
                        }
                    }

                    if (fileIn != null) {
                        fileIn.close();
                    }
                }
            }
        } catch (IOException i) {
            System.out.println("Error: " + i);
        }
    }

    public static void main(String[] args) {
       // System.out.println("Starting server...");
        Server server = new Server(args, 5454);
    }
}
