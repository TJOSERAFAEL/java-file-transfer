package filetransfer;

import java.util.Map;

public class Configuration {

    Map<String, String> server;
    Map<String, String> client;
    Map<String, String> network;
    
    public Configuration() {}

    public void setServer(Map<String, String> server) {
        this.server = server;
    }

    public void setClient(Map<String, String> client) {
        this.client = client;
    }

    public void setNetwork(Map<String, String> network) {
        this.network = network;
    }

    public Map<String, String> getServer() {
        return this.server;
    }

    public Map<String, String> getClient() {
        return this.client;
    }

    public Map<String, String> getNetwork() {
        return this.network;
    }
}
