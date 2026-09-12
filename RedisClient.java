
import java.io.*;
import java.net.*;

public class RedisClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("127.0.0.1", 6379);

        BufferedReader input =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

        PrintWriter output =
                new PrintWriter(
                        socket.getOutputStream(), true);

        System.out.println("Connected to My Redis Server!");

        // SET
        output.println("SET name Mohit");
        System.out.println("SET name Mohit");
        System.out.println("Response: " + input.readLine());

        // GET
        output.println("GET name");
        System.out.println("GET name");
        System.out.println("Response: " + input.readLine());

        // EXPIRE
        output.println("EXPIRE name 5");
        System.out.println("EXPIRE name 5");
        System.out.println("Response: " + input.readLine());

        System.out.println();
        System.out.println("Waiting 6 seconds...");

        Thread.sleep(6000);

        // GET after expiry
        output.println("GET name");
        System.out.println("GET name after 6 seconds");
        System.out.println("Response: " + input.readLine());

        socket.close();

        System.out.println();
        System.out.println("Disconnected from server.");
    }
}
