import java.io.*;
import java.net.*;

public class RedisServer {

    private static final RedisDatabase database =
            new RedisDatabase();

    public static void main(String[] args) throws IOException {

        database.loadData();

        int port = 6379;

        ServerSocket serverSocket =
                new ServerSocket(port);

        System.out.println("================================");
        System.out.println("       MY REDIS SERVER");
        System.out.println("================================");
        System.out.println("Server started on port " + port);
        System.out.println("Data loaded from disk");
        System.out.println("Waiting for clients...");

        while (true) {

            Socket clientSocket =
                    serverSocket.accept();

            System.out.println("New client connected!");

            Thread clientThread = new Thread(
                    () -> handleClient(clientSocket)
            );

            clientThread.start();
        }
    }


    public static void handleClient(
            Socket socket) {

        try {

            BufferedReader input =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            PrintWriter output =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            String command;

            while ((command = input.readLine())
                    != null) {

                String response =
                        execute(command);

                output.println(response);
            }

            socket.close();

            System.out.println(
                    "Client disconnected.");

        } catch (IOException e) {

            System.out.println(
                    "Client error: "
                    + e.getMessage());
        }
    }


    public static String execute(
            String command) {

        String[] parts =
                command.trim().split("\\s+");

        if (parts.length == 0) {
            return "Invalid command";
        }

        String operation =
                parts[0].toUpperCase();


        // SET
        if (operation.equals("SET")) {

            if (parts.length != 3) {
                return "Usage: SET key value";
            }

            return database.set(
                    parts[1],
                    parts[2]
            );
        }


        // GET
        else if (operation.equals("GET")) {

            if (parts.length != 2) {
                return "Usage: GET key";
            }

            return database.get(
                    parts[1]
            );
        }


        // DELETE
        else if (operation.equals("DELETE")) {

            if (parts.length != 2) {
                return "Usage: DELETE key";
            }

            return database.delete(
                    parts[1]
            );
        }


        // EXPIRE
        else if (operation.equals("EXPIRE")) {

            if (parts.length != 3) {
                return "Usage: EXPIRE key seconds";
            }

            try {

                long seconds =
                        Long.parseLong(parts[2]);

                return database.expire(
                        parts[1],
                        seconds
                );

            } catch (NumberFormatException e) {

                return "Invalid seconds";
            }
        }


        // TTL
        else if (operation.equals("TTL")) {

            if (parts.length != 2) {
                return "Usage: TTL key";
            }

            return database.ttl(
                    parts[1]
            );
        }


        // KEYS
        else if (operation.equals("KEYS")) {

            if (parts.length != 1) {
                return "Usage: KEYS";
            }

            return database.keys();
        }


        // INCR
        else if (operation.equals("INCR")) {

            if (parts.length != 2) {
                return "Usage: INCR key";
            }

            return database.incr(
                    parts[1]
            );
        }


        // DECR
        else if (operation.equals("DECR")) {

            if (parts.length != 2) {
                return "Usage: DECR key";
            }

            return database.decr(
                    parts[1]
            );
        }


        // UNKNOWN COMMAND
        else {

            return "Unknown command";
        }
    }
}
