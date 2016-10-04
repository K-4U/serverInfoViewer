package k4unl.minecraft.siv.lib;

import com.google.gson.internal.LinkedTreeMap;
import k4unl.minecraft.k4lib.network.EnumSIPValues;
import k4unl.minecraft.k4lib.network.SipEndPoint;
import net.minecraft.client.multiplayer.ServerAddress;

import java.io.IOException;

/**
 * @author Koen Beckers (K-4U)
 */
public class QueryGetter {
    private ExtendedServerData extendedServerData;
    private SipEndPoint        sipEndPoint;

    public QueryGetter(ServerAddress serverAddress, ExtendedServerData extendedServerData) {
        sipEndPoint = new SipEndPoint(serverAddress.getIP(), serverAddress.getPort() + 1); //We just assume that the port used is always +1. If not.. well
        this.extendedServerData = extendedServerData;
        this.extendedServerData.setHasData(false);

    }

    public void run(){
        try {
            if(extendedServerData.isRequesting())
                return;
            extendedServerData.setRequesting(true);
            sipEndPoint.requestExtendedInfo(EnumSIPValues.DAYNIGHT, EnumSIPValues.TIME, EnumSIPValues.WEATHER);
            extendedServerData.setHasData(true);

            LinkedTreeMap<String, String> time = (LinkedTreeMap<String, String>) sipEndPoint.getExtendedObject(EnumSIPValues.TIME);
            LinkedTreeMap<String, String> weather = (LinkedTreeMap<String, String>) sipEndPoint.getExtendedObject(EnumSIPValues.WEATHER);
            if(time == null) return;
            if(weather == null) return;
            if(sipEndPoint.getExtendedObject(EnumSIPValues.DAYNIGHT) == null) return;
            extendedServerData.setTime(time.get("0"));
            extendedServerData.setIsDay((Boolean) sipEndPoint.getExtendedObject(EnumSIPValues.DAYNIGHT));
            extendedServerData.setWeather(weather.get("0"));
            extendedServerData.setRequesting(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
