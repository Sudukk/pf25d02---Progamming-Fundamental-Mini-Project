public class OptionsManager {
    private static float masterVolume = 0.5f;
    private static float sfxVolume = 0.5f;
    private static float musicVolume = 0.5f;
    private static String bgColor = "black";
    private static String textSize = "medium";
    private static String xIconPath = "";
    private static String oIconPath = "";

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static void setMasterVolume(float v) {
        masterVolume = v;
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(float v) {
        sfxVolume = v;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float v) {
        musicVolume = v;
    }

    public static String getBgColor() {
        return bgColor;
    }

    public static void setBgColor(String c) {
        bgColor = c;
    }

    public static String getTextSize() {
        return textSize;
    }

    public static void setTextSize(String size) {
        textSize = size;
    }

    public static String getXIconPath() {
        return xIconPath;
    }

    public static void setXIconPath(String path) {
        xIconPath = path;
    }

    public static String getOIconPath() {
        return oIconPath;
    }

    public static void setOIconPath(String path) {
        oIconPath = path;
    }
}
