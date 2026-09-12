# My Redis in Java

A lightweight Redis-like in-memory key-value store built from scratch using Java.

This project implements the core concepts behind a Redis-style database, including TCP client-server communication, multithreading, key-value storage, TTL-based expiration, persistence, and command processing.

## Features

* SET and GET key-value operations
* DELETE keys
* EXPIRE for automatic key expiration
* TTL to check remaining expiration time
* KEYS to list stored keys
* INCR and DECR for integer values
* TCP socket-based client-server communication
* Multithreaded client handling
* File-based data persistence
* Command validation and error handling
* Clean separation between server and database logic

## Supported Commands

| Command | Example          | Description                    |
| ------- | ---------------- | ------------------------------ |
| SET     | `SET name Mohit` | Stores a key-value pair        |
| GET     | `GET name`       | Retrieves a value              |
| DELETE  | `DELETE name`    | Removes a key                  |
| EXPIRE  | `EXPIRE name 10` | Expires a key after 10 seconds |
| TTL     | `TTL name`       | Shows remaining TTL            |
| KEYS    | `KEYS`           | Lists available keys           |
| INCR    | `INCR counter`   | Increases an integer value     |
| DECR    | `DECR counter`   | Decreases an integer value     |

## Architecture

```text
                 Client
                   |
                   | TCP Socket
                   v
          +-------------------+
          |   RedisServer     |
          |-------------------|
          | Command Parser    |
          | Client Threads    |
          +---------+---------+
                    |
                    v
          +-------------------+
          |  RedisDatabase    |
          |-------------------|
          | HashMap Storage   |
          | TTL Management    |
          | Persistence       |
          +---------+---------+
                    |
                    v
             redis-data.txt
```

## Project Structure

```text
MyRedisJava/
│
├── README.md
├── .gitignore
│
└── src/
    ├── RedisClient.java
    ├── RedisDatabase.java
    └── RedisServer.java
```

## Technologies Used

* Java
* Java Socket Programming
* TCP/IP
* Multithreading
* HashMap
* Java File I/O
* Object-Oriented Programming

## How It Works

The server listens for client connections on port `6379`.

Each connected client is handled using a separate thread. Commands received from the client are parsed by the server and forwarded to the `RedisDatabase` class.

The database stores key-value pairs in memory using a `HashMap`.

For expiration, the system maintains expiration timestamps and automatically treats expired keys as unavailable.

Data is also written to a local file so that stored values can be loaded again when the server starts.

## Example

```text
SET name Mohit
OK

GET name
Mohit

SET counter 10
OK

INCR counter
11

DECR counter
10

EXPIRE name 10
OK

TTL name
9
```

## Running the Project

Compile the Java source files:

```bash
javac src/RedisDatabase.java src/RedisServer.java src/RedisClient.java
```

Start the server:

```bash
java -cp src RedisServer
```

Then connect using the client or another TCP client.

The server runs on:

```text
localhost:6379
```

## Learning Outcomes

This project was built to understand how a simplified Redis-style system works internally.

Through this project, the following concepts were implemented and practiced:

* Data structures and HashMap-based storage
* Java networking and sockets
* Client-server architecture
* Multithreading
* Command parsing
* TTL and expiration logic
* File persistence
* Exception handling
* Object-oriented design
* Basic database system concepts

## Future Improvements

Possible future improvements include:

* Support for multiple data types
* Improved command parser
* Better persistence format
* Graceful server shutdown
* Performance benchmarking
* Additional Redis-compatible commands
* Improved concurrent data structures
* Automated unit and integration testing

## Author

**Mohit Srivastava**

Computer Science & Engineering (AI/ML)

