package tv.memoryleakdeath.multistream4j.server;

import org.ice4j.ice.Agent;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebRTCSignalingServer extends WebSocketServer {
   private StunServer stunServer;
   private Map<WebSocket, ClientSession> clientSessions;

   private class ClientSession {
      private String sessionId;
      private Agent iceAgent;
      private WebSocket peerConnection;
      private boolean isOfferSource;
      private String streamId;

      public ClientSession(Agent iceAgent, String sessionId) {
         this.iceAgent = iceAgent;
         this.sessionId = sessionId;
      }
   }

   public WebRTCSignalingServer(int port, StunServer server) {
      super(new InetSocketAddress(port));
      stunServer = server;
      clientSessions = new ConcurrentHashMap<>();
      setReuseAddr(true);
   }

   @Override
   public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
      String sessionId = UUID.randomUUID().toString();
      Agent iceAgent = stunServer.createIceAgent();
      ClientSession session = new ClientSession(iceAgent, sessionId);
      clientSessions.put(webSocket, session);

      // connection established message
      JSONObject establishedMessage = new JSONObject();
      establishedMessage.put("type", "welcome");
      establishedMessage.put("sessionId", sessionId);
      establishedMessage.put("timestamp", System.currentTimeMillis());
      webSocket.send(establishedMessage.toString());
   }

   @Override
   public void onClose(WebSocket webSocket, int i, String s, boolean b) {
      ClientSession session = clientSessions.remove(webSocket);
      if(session != null) {
         session.iceAgent.free();
      }
   }

   @Override
   public void onMessage(WebSocket webSocket, String s) {
      JSONObject message = new JSONObject(s);
      String messageType = message.getString("type");
      ClientSession session = clientSessions.get(webSocket);

      if(session == null) {
         throw new RuntimeException("Unknown message received on socket!");
      }

      switch (messageType) {
         case "offer":
            break;
         case "answer":
            break;
         case "ice-candidate":
            break;
         case "create-stream":
            break;
         default:
            throw new RuntimeException("Unknown type parsed: %s".formatted(messageType));
      }
   }

   private void processOffer(WebSocket webSocket, ClientSession session, JSONObject message) {
      String sessionDescriptionProto = message.getString("sdp");
      session.isOfferSource = false;
   }

   @Override
   public void onError(WebSocket webSocket, Exception e) {

   }

   @Override
   public void onStart() {

   }
}
