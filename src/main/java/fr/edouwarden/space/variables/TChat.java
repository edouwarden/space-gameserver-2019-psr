package fr.edouwarden.space.variables;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class TChat {

    private TChat() {
    }

    public static String Head() {
        return ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "[Space]";
    }

    public static ChatColor Error() {
        return ChatColor.RED;
    }

    public static ChatColor Warning() {
        return ChatColor.YELLOW;
    }

    public static ChatColor Info() {
        return ChatColor.BLUE;
    }

    public static String NoShip() {
        return Head() + Warning() + "You do not own a ship yet.";
    }

    public static String TimeLeft(long time) {
        return Head() + Info() + "Wait " + ChatColor.GOLD + time + "s"
                + Info() + " before spawning it again.";
    }

    public static String CannotSpawnShip() {
        return Head() + Error() + "A ship is already parked in this slot.";
    }

    public static String spaceFull() {
        return Head() + Warning() + "The orbital grid is full.";
    }

    public static String kickMysqlError() {
        return Head() + Error() + "Server error: your account could not be initialised. "
                + "Please contact a staff member.";
    }

    public static String usage() {
        return Head() + Info() + "Usage: /space <subcommand> ...";
    }

    public static String done() {
        return Head() + Info() + "Done.";
    }

    public static String cannotWork() {
        return Head() + Error() + "Incomplete action.";
    }

    public static String noReponse() {
        return Head() + Error() + "Error.";
    }

    public static void errorGlobal(Player player, String msg) {
    }

    public static String declineTexturePack() {
        return Head() + Error() + "\nYou must accept the resource pack.\n\n"
                + "If you declined it, open 'Edit' for this server and set 'Resource pack: ON'.\n\n"
                + "If the problem persists, contact a moderator.";
    }

    public static String getItem(String item) {
        return Head() + Info() + "You received: " + ChatColor.GREEN + item;
    }

    public static String notDoorOwner() {
        return Head() + Warning() + "You do not hold the keys for this door.";
    }

    public static String confirmRPK() {
        return Head() + Warning() + "This action is permanent. Confirm with "
                + "/space rpk ROLEPLAYKILL <playerName> yes";
    }

    public static String RPKinfo() {
        return ChatColor.GREEN + "" + ChatColor.BOLD + "Your current character is dead.";
    }

    public static String jobChanged(String jobstr) {
        return Head() + Info() + "You are now a " + jobstr;
    }
}
