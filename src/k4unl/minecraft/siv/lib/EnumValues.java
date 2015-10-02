package k4unl.minecraft.siv.lib;

/**
 * @author Koen Beckers (K-4U)
 */
public enum EnumValues {
    INVALID, MISFORMED, TIME, PLAYERS, DAYNIGHT, DIMENSIONS, UPTIME, DEATHS;

    public static EnumValues fromString(String str) {
        for(EnumValues v : values()){
            if(v.toString().toLowerCase().equals(str.toLowerCase())){
                return v;
            }
        }
        return INVALID;
    }


    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
