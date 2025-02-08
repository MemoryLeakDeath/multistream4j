package tv.memoryleakdeath.multistream4j.util;

import org.ice4j.ice.Agent;
import org.ice4j.ice.sdp.IceSdpUtils;
import org.opentelecoms.javax.sdp.NistSdpFactory;

import javax.sdp.SdpFactory;
import javax.sdp.SessionDescription;

public final class SdpUtils {

   private SdpUtils() {}

   public static String createSDPDescription(Agent agent) throws Throwable {
      SdpFactory factory = new NistSdpFactory();
      SessionDescription desc = factory.createSessionDescription();
      IceSdpUtils.initSessionDescription(desc, agent);
      return desc.toString();
   }
}
