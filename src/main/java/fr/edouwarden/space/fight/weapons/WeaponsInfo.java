package fr.edouwarden.space.fight.weapons;

public class WeaponsInfo {
    // TIER, Danage, Range, Cooldown, Area, Accuracy, Type
    public static int tier(String[] meta){      return Integer.valueOf(meta[0]); }
    public static Double damage(String[] meta){
                    return Double.valueOf(meta[1]);
    }
    public static Double range(String[] meta){
                    return Double.valueOf(meta[2]);
    }
    public static Short cooldown(String[] meta){
        return Short.valueOf(meta[3]);
    }
    public static Short area(String[] meta){
        return Short.valueOf(meta[4]);
    }
    public static Double accuracy(String[] meta){
        return Double.valueOf(meta[5]);
    }
    public static String type(String[] meta){ return meta[6]; }
    public static String uniqueName(String[] meta){
        return meta[7];
    }
}
