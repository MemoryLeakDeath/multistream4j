package tv.memoryleakdeath.multistream4j.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.net.InetAddress;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.testng.annotations.Test;

import tv.memoryleakdeath.multistream4j.server.StunServer;

@Test
public class StunServerTest {

    @Test
    public void testServerConnectivity() {
        try (StunServer server = new StunServer()) {
            server.start();
            Agent testClient = createTestClient();
            assertNotNull(testClient, "Test client should not be null!");
            server.printCandidates();
        } catch (Exception e) {
            fail("Could not test server connectivity!", e);
        }
    }

    private Agent createTestClient() {
        Agent testClient = new Agent();
        TransportAddress address = new TransportAddress(InetAddress.getLoopbackAddress(), 3478, Transport.UDP);
        testClient.addCandidateHarvester(new StunCandidateHarvester(address));
        testClient.createMediaStream("testStream");
        return testClient;
    }

}
