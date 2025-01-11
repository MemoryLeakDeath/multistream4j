package tv.memoryleakdeath.multistream4j.server;

import java.io.InputStream;
import java.util.List;

public interface Server {
    void start();

    void stop();

    void handleStream(int id, InputStream stream);

    void setDestinations(List<Destination> urls);

    List<Destination> getDestinations();
}
