import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class PhiServer {

    private final int portNum;
    private final HttpServer server;
    private static final int SYSTEM_SOCKET_BACKLOG_DEFAULT = 0;

    // todo make real exceptions
    public PhiServer(int port) throws Exception{
        this.portNum = port;
        this.server = HttpServer.create(
            new InetSocketAddress(this.portNum), 
            SYSTEM_SOCKET_BACKLOG_DEFAULT
        );

        this.initEndpoints();
        this.server.setExecutor(null);

    }


    public void listen(){
        System.out.println("PhiSever listening on port " + this.portNum);

        this.server.start();
    }


    private void initEndpoints(){
        // I honestly think for the scope of this project, we should only ever need this endpoint
        this.server.createContext("/rec", new PhiServerHandler());
    }

    static class PhiServerHandler implements HttpHandler {
    @Override

    public void handle(HttpExchange exchange) throws IOException {
        

        // need reader for req body
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        
        // should be a fancy way to do with with streams, I cant get that to work rn
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();

        // "preferences": "something"} is what this is attempting to parse lmao
        String preferences = "";
        int start = body.indexOf("\"preferences\"");


        int colon = body.indexOf(':', start);

        int firstQuote = body.indexOf('"', colon + 1);
        int secondQuote = body.indexOf('"', firstQuote + 1);

        if (firstQuote != -1 && secondQuote != -1){
            preferences = body.substring(firstQuote + 1, secondQuote);
        }

        PopularityRecord pr = PopularityRecord.parse("data/store-0.dat");

        Ollama o = new OllamaBuilder()
            .model(SupportedModels.PHI3)
            .system(pr.toPrompt())
            .build();

        String prompt = "Please recommend me products from data from the previous prompt " + preferences;
        Response s;
        try {
            s = o.prompt(prompt);

            System.out.println("Response was " + s.getFullResponse());
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        byte[] resp = s.getFullResponse().getBytes();
        exchange.sendResponseHeaders(200, resp.length);
        OutputStream os = exchange.getResponseBody();
        os.write(resp);
        os.close();
    }
}

}