package tv.memoryleakdeath.multistream4j.service;

import tv.memoryleakdeath.multistream4j.server.Destination;

public class TranscoderService implements AutoCloseable {
    private Destination destination;

    public TranscoderService(Destination dest) {
        this.destination = dest;
    }

    public void start() {
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

    public Destination getDestination() {
        return destination;
    }

}
