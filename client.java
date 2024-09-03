package Craft_Genie;

import javax.net.ssl.*;

public class Client {

    public static void main(String[] args) {
        int portNumber = 17777;
        String hostName = "127.0.0.1";
        String trustStorePath = "C:\\nslab\\craft-truststore.jks";
        char[] truststorePassword = "craftgenie".toCharArray();

        SSLContext sslContext;
        try {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(truststorePath), truststorePassword);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Connecting to the server...");

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(hostName, portNumber);
             PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()))) {

            System.out.println("Handshake successful. Connected to the server.");

            // User prompt for command
            System.out.println("Enter a command: REQUEST_CRAFT_IDEA or CLOSECONNECTION");

            String message;
            while (true) {
                System.out.print("Crafter: ");
                message = new BufferedReader(new InputStreamReader(System.in)).readLine();
                out.println(message);

                if (message.equalsIgnoreCase("CLOSECONNECTION")) {
                    System.out.println("Closing the connection...");
                    break;
                } else if (message.equalsIgnoreCase("REQUEST_CRAFT_IDEA")) {
                    String receivedCraftIdeaResponse = in.readLine();
                    System.out.println("" + receivedCraftIdeaResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
