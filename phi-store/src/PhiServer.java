import java.io.IOException;
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
            // exchange wraps both request and reponse

            // todo parametrize this as a sys arg or something
            PopularityRecord pr = PopularityRecord.parse("data/store-0.dat");


            Ollama o = new OllamaBuilder()
                .model(SupportedModels.PHI3)
                .system(pr.toPrompt())
                .build();

            OutputStream os = null;
            try {
                
                // todo pass this in as GET body param
                String preferences = "";
                Response s = o.prompt("Please recommend me products from data from the previous prompt" + preferences);

                // System.out.println(s.getFullResponse());

                // oracles javadoc claims bytes.length is better than just length, but it shouldnt matter
                // for our usecase
                exchange.sendResponseHeaders(200,  s.getFullResponse().getBytes().length);
                os = exchange.getResponseBody();
                os.write(s.getFullResponse().getBytes());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            catch(Exception e) {
                e.printStackTrace();
            }finally {
                os.close();
            }
        }
    
    }
}