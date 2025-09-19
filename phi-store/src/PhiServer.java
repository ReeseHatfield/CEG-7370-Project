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
        this.server.createContext("/test", new PhiServerHandler());
    }

    static class PhiServerHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // exchange wraps both request and reponse
            String wordsToSayBack = "Hello world";

            // oracles javadoc claims bytes.length is better than just length, but it shouldnt matter
            // for our usecase
            exchange.sendResponseHeaders(200,  wordsToSayBack.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(wordsToSayBack.getBytes());
            os.close();

        }
    
        
    }
}