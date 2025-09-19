import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class PhiServer {

    private final int portNum;
    private final HttpServer server;
    private final static int SYSTEM_SOCKET_BACKLOG_DEFAULT = 0;

    // todo make real exceptions
    public PhiServer(int port) throws Exception{
        this.portNum = port;
        this.server = HttpServer.create(new InetSocketAddress(this.portNum), SYSTEM_SOCKET_BACKLOG_DEFAULT);

    }

    public void listen(){

    }
}