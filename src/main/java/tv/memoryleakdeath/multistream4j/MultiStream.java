package tv.memoryleakdeath.multistream4j;

import tv.memoryleakdeath.multistream4j.server.StunServer;

public class MultiStream {

    public static void main(String[] args) {
        try (StunServer stunServer = new StunServer()) {
            stunServer.start();
            stunServer.printCandidates();
            while (true) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
