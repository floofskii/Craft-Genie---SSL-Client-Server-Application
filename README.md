# Craft-Genie---SSL-Client-Server-Application

Craft Genie - SSL Client-Server Application
This repository contains a simple SSL-based client-server application implemented in Java. The project demonstrates secure communication between a client and server using SSL/TLS. The server suggests random craft ideas to the client, and the client can request craft ideas or close the connection.

Key Features:

SSL/TLS Communication: Establishes a secure connection between the client and server using a truststore with SSL/TLS encryption.
Craft Idea Generation: The server generates random craft ideas, which the client can request during the session.
Multiple Client Handling: The server is capable of handling multiple client connections simultaneously, each on a separate thread.
Secure Data Transmission: The application ensures that all communication between the client and server is securely encrypted.
Components:

Client.java: Contains the code for the client-side application that connects to the server, requests craft ideas, and can close the connection.

Server.java: Contains the code for the server-side application that listens for client connections, processes requests, and generates craft ideas.
How to Run:

Compile the Client.java and Server.java files.

Run the server and keep it listening on the specified port.

Run the client to connect to the server and interact with it using predefined commands.
