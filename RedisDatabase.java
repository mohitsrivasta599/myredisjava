import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RedisDatabase {

    private final Map<String, String> database =
            new HashMap<>();

    private final Map<String, Long> expiryTimes =
            new HashMap<>();

    private final String DATA_FILE =
            "redis-data.txt";


    // SET
    public synchronized String set(
            String key,
            String value) {

        database.put(key, value);
        expiryTimes.remove(key);
        saveData();

        return "OK";
    }


    // GET
    public synchronized String get(
            String key) {

        if (isExpired(key)) {
            return "(nil)";
        }

        String value = database.get(key);

        if (value == null) {
            return "(nil)";
        }

        return value;
    }


    // DELETE
    public synchronized String delete(
            String key) {

        expiryTimes.remove(key);

        if (database.remove(key) != null) {
            saveData();
            return "OK";
        }

        return "(nil)";
    }


    // EXPIRE
    public synchronized String expire(
            String key,
            long seconds) {

        if (!database.containsKey(key)) {
            return "(nil)";
        }

        if (seconds < 0) {
            return "Invalid seconds";
        }

        long expiryTime =
                System.currentTimeMillis()
                + (seconds * 1000);

        expiryTimes.put(key, expiryTime);
        saveData();

        return "OK";
    }


    // TTL
    public synchronized String ttl(
            String key) {

        if (isExpired(key)) {
            return "-2";
        }

        if (!database.containsKey(key)) {
            return "-2";
        }

        Long expiryTime =
                expiryTimes.get(key);

        if (expiryTime == null) {
            return "-1";
        }

        long remaining =
                (expiryTime - System.currentTimeMillis())
                / 1000;

        if (remaining < 0) {
            return "-2";
        }

        return String.valueOf(remaining);
    }


    // KEYS
    public synchronized String keys() {

        if (database.isEmpty()) {
            return "(empty)";
        }

        StringBuilder result =
                new StringBuilder();

        for (String key : database.keySet()) {

            if (!isExpired(key)) {

                result.append(key);
                result.append("\n");
            }
        }

        return result.toString().trim();
    }


    // INCR
    public synchronized String incr(
            String key) {

        if (isExpired(key)) {
            return "(nil)";
        }

        if (!database.containsKey(key)) {

            database.put(key, "1");
            saveData();

            return "1";
        }

        try {

            long value =
                    Long.parseLong(
                            database.get(key));

            value++;

            database.put(
                    key,
                    String.valueOf(value)
            );

            saveData();

            return String.valueOf(value);

        } catch (NumberFormatException e) {

            return "ERR value is not an integer";
        }
    }


    // DECR
    public synchronized String decr(
            String key) {

        if (isExpired(key)) {
            return "(nil)";
        }

        if (!database.containsKey(key)) {

            database.put(key, "-1");
            saveData();

            return "-1";
        }

        try {

            long value =
                    Long.parseLong(
                            database.get(key));

            value--;

            database.put(
                    key,
                    String.valueOf(value)
            );

            saveData();

            return String.valueOf(value);

        } catch (NumberFormatException e) {

            return "ERR value is not an integer";
        }
    }


    // Check expiry
    private boolean isExpired(
            String key) {

        Long expiryTime =
                expiryTimes.get(key);

        if (expiryTime == null) {
            return false;
        }

        if (System.currentTimeMillis()
                >= expiryTime) {

            database.remove(key);
            expiryTimes.remove(key);
            saveData();

            return true;
        }

        return false;
    }


    // Save data
    private void saveData() {

        try {

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(DATA_FILE));

            for (Map.Entry<String, String> entry
                    : database.entrySet()) {

                writer.write(
                        entry.getKey()
                        + "="
                        + entry.getValue()
                );

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving data: "
                    + e.getMessage());
        }
    }


    // Load data
    public synchronized void loadData() {

        File file =
                new File(DATA_FILE);

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(DATA_FILE));

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] parts =
                        line.split("=", 2);

                if (parts.length == 2) {

                    database.put(
                            parts[0],
                            parts[1]
                    );
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error loading data: "
                    + e.getMessage());
        }
    }
}
