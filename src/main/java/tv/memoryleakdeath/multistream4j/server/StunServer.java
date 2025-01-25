package tv.memoryleakdeath.multistream4j.server;

import java.net.InetAddress;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.ice.harvest.TurnCandidateHarvester;

public class StunServer implements AutoCloseable {
    private static final int STUN_PORT = 3478;
    private static final int TURN_PORT = 3479;
    private Agent iceAgent;
    private TransportAddress localStunServer = new TransportAddress(InetAddress.getLoopbackAddress(), STUN_PORT,
            Transport.UDP);
    private TransportAddress localTurnServer = new TransportAddress(InetAddress.getLoopbackAddress(), TURN_PORT,
            Transport.UDP);

    public Agent createIceAgent() {
        Agent iceAgent = new Agent();
        iceAgent.addCandidateHarvester(new StunCandidateHarvester(localStunServer));
        iceAgent.addCandidateHarvester(new TurnCandidateHarvester(localTurnServer));
        return iceAgent;
    }

    public void start() {
        iceAgent.startConnectivityEstablishment();
    }

    @Override
    public void close() throws Exception {
        if (iceAgent != null) {
            iceAgent.free();
            iceAgent = null;
        }
    }

    public void printCandidates() {
        iceAgent.getStreams().forEach(s -> {
            System.out.println("Stream: %s".formatted(s.getName()));
            s.getComponents().forEach(c -> {
                System.out.println("Component: %d".formatted(c.getComponentID()));
                c.getLocalCandidates().forEach(lc -> {
                    System.out.println("Candidate: %s".formatted(lc.getTransportAddress()));
                });
            });
        });
    }
}
