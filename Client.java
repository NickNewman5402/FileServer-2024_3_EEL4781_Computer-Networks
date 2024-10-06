
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;
//import java.nio.charset.StandardCharsets;

public class Client {
    private final int BUFFER_SIZE = 4096;
    private Socket connection;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private FileOutputStream fileout;
    private int bytes;
    private byte[] buffer = new byte[BUFFER_SIZE];
    

    //The below closed client was originally given but altered. This is being kept for posterity
    /*public Client(String host, int port, String filename) {
        try {
            connection = new Socket(host, port);

            socketIn = new DataInputStream(connection.getInputStream()); // Read data from server
            socketOut = new DataOutputStream(connection.getOutputStream()); // Write data to server

            socketOut.writeUTF(filename); // Write filename to server

            FileOutputStream fileOut = new FileOutputStream("received_" + filename);

            // Read file contents from server
            while (true) {
                bytes = socketIn.read(buffer, 0, BUFFER_SIZE); // Read from socket
               
                if (bytes <= 0) {
                    break; // Check for end of file
                }

                fileOut.write(buffer, 0, bytes); // Write to file
                System.out.print(new String(buffer, StandardCharsets.UTF_8)); // Write to standard output
            }

            fileOut.close();
            connection.close();
            
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }*/

    public Client(String args[], int port) {

        if(args.length < 2){
            System.out.printf("Usage:\njava Client <server-ip> <file-name>" +
            "\njava Client <server-ip> <file-name> [-s] START_BYTE [-e] END_BYTE" +
            "\njava Client <server-ip> [-w] <file-name>" +
            "\n");

        }
        
        //        a[0]          a[1]        a[2]        a[3]       a[4]      a[5]    
        // Client <server-name> <file-name>                                            ***********regular operation
        // Client <server-name> <file-name> [-s]        START_BYTE [-e]      END_BYTER ***********client tells server to only send a chunk
        // Client <server-name> [-w]        <file-name>                                ***********allows client to write to server 
            
            String host = args[0];
            String filename = args[1];
            

            try{
                                
                connection = new Socket(host, port);
                
                socketIn = new DataInputStream(connection.getInputStream()); // Read data from server
                socketOut = new DataOutputStream(connection.getOutputStream()); // Write data to server

                socketOut.writeUTF(filename); // Write filename to server

                FileOutputStream fileOut = new FileOutputStream("received_" + filename);

                    // Read file contents from server
                    while (true) {
                        bytes = socketIn.read(buffer, 0, BUFFER_SIZE); // Read from socket
                        
                        // to send chunks I will make 2nd arg START_BYTE and I will make BUFFER_SIZE END_BYTE - START_BYTE
                        if (bytes <= 0) {
                            break; // Check for end of file
                        }

                        fileOut.write(buffer, 0, bytes); // Write to file
                        //System.out.print(new String(buffer, StandardCharsets.UTF_8)); // Write to standard output
                    }

                fileOut.close();
                connection.close();
                
                
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
           
            }
    }





    public static void main(String[] args) {
        //Client client = new Client("127.0.0.1"/*args[0]*/, 5454, "test.txt"/*args[1]*/);
        Client client = new Client(args, 5454);
    }
}

