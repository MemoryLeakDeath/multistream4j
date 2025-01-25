package tv.memoryleakdeath.multistream4j.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebRTCSignalingServer extends WebSocketServer {
   private static final int WEBRTC_PORT = 9090;
   private Set<WebSocket> connections;

   public WebRTCSignalingServer() {
      super(new InetSocketAddress(WEBRTC_PORT));
      connections = Collections.synchronizedSet(new HashSet<>());
   }

   @Override
   public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
      connections.add(webSocket);
   }

   @Override
   public void onClose(WebSocket webSocket, int i, String s, boolean b) {
      connections.remove(webSocket);
   }

   @Override
   public void onMessage(WebSocket webSocket, String s) {

   }

   @Override
   public void onError(WebSocket webSocket, Exception e) {

   }

   @Override
   public void onStart() {

   }
}
