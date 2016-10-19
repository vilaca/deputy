
package eu.vilaca.remote.server;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class BenchServer implements Runnable {

    private static String HOST;
    private static Integer PORT;

    private final Handler handler = new RequestHandler();

    public void start() {

        final ContextHandler root = new ContextHandler();
        root.setContextPath("/");
        root.setHandler(handler);

        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { root });

        final Server listener = new Server(
                new InetSocketAddress(HOST != null ? HOST : "0.0.0.0", PORT != null ? PORT : 8089));

        listener.setHandler(contexts);

        try {

            // start server

            listener.start();
            listener.join();
            // listener.destroy();

        } catch (final Exception ex) {

            // TODO log
        }
    }

    @Override
    public void run() {
        start();
    }

    public RequestHandler getHandler() {
        return (RequestHandler) handler;
    }

    static void setHost(String host) {
        HOST = host;
    }

    static void setPort(Integer port) {
        PORT = port;
    }
}
