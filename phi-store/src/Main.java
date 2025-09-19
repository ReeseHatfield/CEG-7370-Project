import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {

        // Ollama o = new OllamaBuilder()
        //     .model(SupportedModels.LLAMA3DOT2)
        //     .system("Recommend me products!")
        //     .build();

        // try {
        //     Response s = o.prompt("I usually like food products, please recommend me some");

        //     System.out.println(s.getFullResponse());
        // } catch (IOException ioe) {
        //     ioe.printStackTrace();
        // }

        // todo document all these ports
        PhiServer server = new PhiServer(3001);
        server.listen();


    }
}
