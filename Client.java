/**
 * Nick Newman
 * 5295926
 * 10/06/24
 * Fall
 * EEL 4781 - Computer Communication Networks
 *  
 * This program is a client for a file server program that when ran attempts to connect to the server
 * which the user determines on port 5454. The base command is as follows:
 * 
 * java Client <server-ip> <filename> 
 * 
 * This will return a file named received-<filename>. You do have options with this program. You can change 
 * the start byte, the end byte, or the start and the end byte. Doing any of these will return a chunk
 * of a document. They can be entered as follows:
 * 
 * java Client <server-ip> <filename> [-s] START_BYTE
 * java Client <server-ip> <filename> [-e] END_BYTE
 * java Client <server-ip> <filename> [-s] START_BYTE [-e] END_BYTE
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class Client {
    private final int BUFFER_SIZE = 4096;
    private Socket connection;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private FileOutputStream fileout;
    private int bytes;
    private byte[] buffer = new byte[BUFFER_SIZE];

    // The below closed client was originally given but altered. This is being kept
    // for posterity
    /*
     * public Client(String host, int port, String filename) {
     * try {
     * connection = new Socket(host, port);
     * 
     * socketIn = new DataInputStream(connection.getInputStream()); // Read data
     * from server
     * socketOut = new DataOutputStream(connection.getOutputStream()); // Write data
     * to server
     * 
     * socketOut.writeUTF(filename); // Write filename to server
     * 
     * FileOutputStream fileOut = new FileOutputStream("received_" + filename);
     * 
     * // Read file contents from server
     * while (true) {
     * bytes = socketIn.read(buffer, 0, BUFFER_SIZE); // Read from socket
     * 
     * if (bytes <= 0) {
     * break; // Check for end of file
     * }
     * 
     * fileOut.write(buffer, 0, bytes); // Write to file
     * System.out.print(new String(buffer, StandardCharsets.UTF_8)); // Write to
     * standard output
     * }
     * 
     * fileOut.close();
     * connection.close();
     * 
     * } catch (Exception ex) {
     * System.out.println("Error: " + ex);
     * }
     * }
     */

    public Client(String args[], int port) {

        // java Client
        if (args.length < 2) {
            System.out.printf("Usage:\n--java Client <server-ip> <file-name>" +
                    "\n--java Client <server-ip> <file-name> [-s] START_BYTE [-e] END_BYTE" +
                    "\n--java Client <server-ip> [-w] <file-name>" +
                    "\n");
            return;

        }

        String host = null;
        String fileName = null;
        boolean writeMode = false;
        int startByte = 0;
        int endByte = 4096;

        // Iterate through the command line arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-w":
                    writeMode = true;
                    break;
                case "-s":
                    if (i + 1 < args.length) {
                        startByte = Integer.parseInt(args[++i]); // Get the start byte
                    }
                    break;
                case "-e":
                    if (i + 1 < args.length) {
                        endByte = Integer.parseInt(args[++i]); // Get the end byte
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length){
                        port = Integer.parseInt(args[++i]); // Allow user to
                    }
                default:
                    // This is either the server name or the file name
                    if (host == null) {
                        host = arg;
                    } else if (fileName == null) {
                        fileName = arg;
                    }
                    break;
            }
        }





        try {

            connection = new Socket(host, port);

            socketIn = new DataInputStream(connection.getInputStream()); // Read data from server
            socketOut = new DataOutputStream(connection.getOutputStream()); // Write data to server

            socketOut.writeUTF(fileName); // Write filename to server

            FileOutputStream fileOut = new FileOutputStream("received_" + fileName);// save new file as

            // Setting the start byte ahead
            if (startByte > 0) {
                socketIn.skipBytes(startByte);
            }

            // Read file contents from server
            while (true) {

                // java Client 127.0.0.1 test.txt
                // This will take 4096 bytes (BUFFER_SIZE) each loop until socketIn.read() returns a -1
                if (startByte == 0 && endByte == 4096) {
                    bytes = socketIn.read(buffer, 0, BUFFER_SIZE); // Read from socket
                }

                // java Client 127.0.0.1 test.txt -s START_BYTE -e END_BYTE
                if (startByte > 0 && endByte < 4096) {
                    int readByte = endByte - startByte;
                    bytes = socketIn.read(buffer, 0, readByte); // Read from socket. readByte can be from 0 to 4096
                    fileOut.write(buffer, 0, bytes); // Write to file
                    break;
                }

                // java Client 127.0.0.1 test.txt -s START_BYTE
                if (startByte > 0 && endByte == 4096) {
                    bytes = socketIn.read(buffer, 0, endByte); // Read from socket

                }

                // java Client 127.0.0.1 test.txt -e END_BYTE
                if (startByte == 0 && endByte < 4096) {
                    int readByte = endByte;
                    bytes = socketIn.read(buffer, 0, readByte); // Read from socket
                    fileOut.write(buffer, 0, bytes); // Write to file
                    break;
                }

                if (bytes < 0) {
                    break;
                }

                fileOut.write(buffer, 0, bytes); // Write to file
                // System.out.print(new String(buffer, StandardCharsets.UTF_8)); // Write to standard output

            }

            fileOut.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Error: " + ex);

        }
    }

    public static void main(String[] args) {
        Client client = new Client(args, 5454);
    }
}
