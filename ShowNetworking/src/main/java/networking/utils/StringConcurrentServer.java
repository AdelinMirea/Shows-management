package networking.utils;

import show.services.IShowServer;

import java.net.Socket;

public class StringConcurrentServer extends AbstractConcurrentServer {

    private IShowServer showServer;

    public StringConcurrentServer(int port, IShowServer showServer) {
        super(port);
        this.showServer = showServer;
        System.out.println("--- ShowStringConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ShowClientRpcWorker worker=new ShowClientRpcWorker(showServer, client);
        return new Thread(worker);
    }

}
