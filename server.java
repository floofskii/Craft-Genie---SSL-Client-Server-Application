package Craft_Genie;

import javax.net.ssl.*;

public class Server {

    private static final int PORT_NUMBER = 17777;
    private static final List<String> craftIdeas = new ArrayList<>();
    private static String clientName;

    static {
        craftIdeas.addAll(Arrays.asList(
                "Make paper plate animals: Paper plates, markers, googly eyes, glue.",
                "Make sock puppets: Old socks, buttons, felt, glue.",
                "Create nature collage: Leaves, twigs, glue, paper.",
                "Try crayon art: Crayons, canvas, hairdryer.",
                "Craft an Egg Carton Caterpillar: Egg carton, paint, googly eyes, pipe cleaners.",
                "Craft Tissue Paper Flowers: Colored tissue paper, pipe cleaners.",
                "Craft Cardboard Tube Binoculars: Toilet paper tubes, paint, string.",
                "Craft Button Art: Canvas, buttons, glue.",
                "Craft Cereal Box Puzzles: Empty cereal boxes, markers, scissors."
        ));
    }

    public static void main(String[] args) {
        try {
            // Set up SSL context
            SSLContext serverSSLContext = createSSLContext("C:\\nslab\\craft.jks", "craftgenie", "craftgenie");

            // Create SSL server socket
            SSLServerSocketFactory sslServerSocketFactory = serverSSLContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT_NUMBER);

            // Print architecture details when the server starts
            printArchitectureDetails();

            while (true) {
                // Accept client connection
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

                // multiple client connection threads
                Thread clientThread = new Thread(() -> {
                    try (
                            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()))) {
                        
                        // Handle different client interaction
                        handleClientInteraction(in, out, sslSocket);
                    } catch (IOException e) {
                        // Handle the exception appropriately in a production environment
                        System.out.println("Error during client interaction: " + e.getMessage());
                    }
                });

                // Start the thread
                clientThread.start();
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    private static SSLContext createSSLContext(String keystorePath, String keystorePassword, String keyPassword)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

        return sslContext;
    }

    private static void handleClientInteraction(BufferedReader in, PrintWriter out, SSLSocket sslSocket) throws IOException {
        System.out.println("Handshake successful. Client connected from " +
                sslSocket.getInetAddress() + ":" + sslSocket.getPort());

        String receivedMessage;
        while ((receivedMessage = in.readLine()) != null) {
            System.out.println("Crafter: " + receivedMessage);

            String response;
            switch (receivedMessage.toUpperCase()) {
                case "REQUEST_CRAFT_IDEA":
                    // Respond with a random craft idea
                    response = "Craft Genie: " + getRandomCraftIdea();
                    break;
                case "CLOSECONNECTION":
                    // Respond to close the connection
                    response = "Closing the connection...";
                    break;
                default:
                    // Respond with "NOT RECOGNIZED" for unrecognized messages
                    response = "NOT_RECOGNIZED";
                    break;
            }
            out.println(response);
            System.out.println(response);
        }
        System.out.println("Client disconnected.");
    }

    private static String getRandomCraftIdea() {
        Random random = new Random();
        int randomIndex = random.nextInt(craftIdeas.size());
        return craftIdeas.get(randomIndex);
    }

    // Print details
    private static void printArchitectureDetails() {
        System.out.println("Craft Genie");
        System.out.println("-------------------------------");
        System.out.println("[Crafter] <----> [Craft Genie]");
    }

    public static String getClientName() {
        return clientName;
    }
}
