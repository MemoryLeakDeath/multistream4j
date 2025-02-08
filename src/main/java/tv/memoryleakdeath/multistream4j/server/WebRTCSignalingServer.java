package tv.memoryleakdeath.multistream4j.server;

import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.opentelecoms.javax.sdp.NistSdpFactory;
import tv.memoryleakdeath.multistream4j.service.TranscoderService;

import javax.sdp.MediaDescription;
import javax.sdp.SdpFactory;
import javax.sdp.SessionDescription;
import java.net.InetSocketAddress;
import java.util.UUID;

public class WebRTCSignalingServer extends WebSocketServer {
   private StunServer stunServer;
   private WebSocket activeConnection;
   private TranscoderService transcoderService;
   private Agent iceAgent;
   private IceMediaStream mediaStream;
   private boolean isStreamActive;


   public WebRTCSignalingServer(int port, StunServer server) {
      super(new InetSocketAddress(port));
      stunServer = server;
      setReuseAddr(true);
   }

   @Override
   public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
      if(activeConnection != null) {
         webSocket.close(-1, "Server full!");
         return;
      }
      activeConnection = webSocket;
      String sessionId = UUID.randomUUID().toString();
      iceAgent = stunServer.createIceAgent();
      mediaStream = iceAgent.createMediaStream("stream");
      stunServer.start();


      // connection established message
      JSONObject establishedMessage = new JSONObject();
      establishedMessage.put("type", "welcome");
      establishedMessage.put("sessionId", sessionId);
      establishedMessage.put("timestamp", System.currentTimeMillis());
      webSocket.send(establishedMessage.toString());
   }

   @Override
   public void onClose(WebSocket webSocket, int i, String s, boolean b) {
      if(webSocket == activeConnection) {
         // stop stream
         activeConnection = null;
         if (iceAgent != null) {
            iceAgent.free();
            iceAgent = null;
         }
      }
   }

   @Override
   public void onMessage(WebSocket webSocket, String s) {
      JSONObject message = new JSONObject(s);
      String messageType = message.getString("type");

      if(webSocket != activeConnection) {
         throw new RuntimeException("Unknown message received on socket!");
      }

      switch (messageType) {
         case "offer":
            processOffer(message);
            break;
         case "ice-candidate":
            processIceCandidate(message);
            break;
         default:
            throw new RuntimeException("Unknown type parsed: %s".formatted(messageType));
      }
   }

   private void processOffer(JSONObject message) {
      String sessionDescriptionProto = message.getString("sdp");
      // processSdpOffer

      // respond to offer
      JSONObject answer = new JSONObject();
      answer.put("type", "answer");
      answer.put("sdp", ""); // TODO createSdp
      // send answer
   }

   private void doSdpOffer(String sdp) {
      try {
         SdpFactory factory = new NistSdpFactory();
         SessionDescription desc = factory.createSessionDescription(sdp);
         desc.getMediaDescriptions(true).forEach(md -> {
            ((MediaDescription)md).getAttributes(true).forEach(attrib -> {
               if(attrib.toString().startsWith("candidate:")) {
                  // TODO finish
               }
            });
         });
      } catch(Exception e) {
         // TODO do something
      }
   }

   private void processIceCandidate(JSONObject message) {
      JSONObject candidate = message.getJSONObject("candidate");
      // process ICE candidate
   }

   @Override
   public void onError(WebSocket webSocket, Exception e) {

   }

   @Override
   public void onStart() {

   }
}
