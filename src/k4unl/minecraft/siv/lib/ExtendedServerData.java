package k4unl.minecraft.siv.lib;

/**
 * @author Koen Beckers (K-4U)
 */
public class ExtendedServerData {
    private boolean hasData;
    private boolean isDay;
    private String time = "";
    private String weather = "";
    private boolean requesting = false;

    public boolean isDay() {
        return isDay;
    }

    public void setIsDay(boolean isDay) {
        this.isDay = isDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public boolean isRequesting() {
        return requesting;
    }

    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
    }
}
