package k4unl.minecraft.siv.lib;

import com.google.gson.internal.LinkedTreeMap;
import k4unl.minecraft.k4lib.network.EnumQueryValues;
import k4unl.minecraft.k4lib.network.Query;
import net.minecraft.client.multiplayer.ServerAddress;

import java.io.IOException;

/**
 * @author Koen Beckers (K-4U)
 */
public class QueryGetter {
    private ExtendedServerData extendedServerData;
    private Query query;

    public QueryGetter(ServerAddress serverAddress, ExtendedServerData extendedServerData) {
        query = new Query(serverAddress.getIP(), serverAddress.getPort());
        this.extendedServerData = extendedServerData;
        this.extendedServerData.setHasData(false);

    }

    public void run(){
        try {
            if(extendedServerData.isRequesting())
                return;
            extendedServerData.setRequesting(true);
            query.requestExtendedInfo(EnumQueryValues.DAYNIGHT, EnumQueryValues.TIME, EnumQueryValues.WEATHER);
            extendedServerData.setHasData(true);

            LinkedTreeMap<String, String> time = (LinkedTreeMap<String, String>) query.getExtendedObject(EnumQueryValues.TIME);
            LinkedTreeMap<String, String> weather = (LinkedTreeMap<String, String>) query.getExtendedObject(EnumQueryValues.WEATHER);
            if(time == null) return;
            if(weather == null) return;
            if(query.getExtendedObject(EnumQueryValues.DAYNIGHT) == null) return;
            extendedServerData.setTime(time.get("0"));
            extendedServerData.setIsDay((Boolean) query.getExtendedObject(EnumQueryValues.DAYNIGHT));
            extendedServerData.setWeather(weather.get("0"));
            extendedServerData.setRequesting(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
