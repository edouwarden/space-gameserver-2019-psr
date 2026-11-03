package fr.edouwarden.space.sign;

import com.sk89q.worldedit.MaxChangedBlocksException;
import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.ship.ShipEntity;
import fr.edouwarden.space.station.Spot;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

public class ShipSign {
    public static void Clear(Block block){
        Sign lsign = (Sign) block.getState();
        lsign.setLine(1, ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "[Slot]");
        lsign.setLine(2, "");
        lsign.update();
    }
    private static void Setup(Sign sign){
        Station station;
        try {
            station = Station.getFromLoc(sign.getLocation());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (station==null) return;
        short minTier;
        if (sign.getLine(1).isEmpty()) minTier=5;
        else minTier= Short.parseShort(sign.getLine(1));
        Station.addSpot(new Spot(sign.getLocation(),0,minTier, (short) 0,station.letter), station.letter);
        sign.setLine(0,"");
        sign.setLine(1, ChatColor.BOLD+""+ChatColor.DARK_GREEN+"[Slot]");
        sign.update();
    }
    private static void Define(Sign sign,Player player){
        sign.setLine(1,ChatColor.BOLD+""+ChatColor.DARK_RED+"Vessel: " );
        sign.setLine(2,ChatColor.BOLD+""+ player.getName() );
        sign.update();
    }
    private static boolean IsNotSetup(Sign sign){
        return sign.getLine(0).equals("[Dock]");
    }
    private static boolean IsSpawnEmpty(Sign sign){return sign.getLine(1).contains("[Slot]"); }
    private static boolean IsAlreadyTaken(Sign sign){return sign.getLine(1).contains("Vessel: "); }

    private static boolean HaveCooldown(long CooldownLeft){return CooldownLeft> 0L;}

    private static Long CooldownLeft(PlayerEntity player, int cooldownTime){return player.getcooldown() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;}
    private static void SetCooldown(PlayerEntity player){
        player.setcooldown( System.currentTimeMillis());}



    public static void Click(PlayerEntity player, Sign sign) throws IOException, MaxChangedBlocksException {
        if (ShipSign.IsNotSetup(sign)) {
            ShipSign.Setup(sign);
            return;
        }
        else if (ShipSign.IsSpawnEmpty(sign)) {

            int shiptype = 1; // TODO: read the owned ship type from persistence
            if (shiptype == 0) {
                player.getBukkitPL().sendMessage(TChat.NoShip());
            } else {
                if(CooldownCheck(player))return;

                Location loc = sign.getLocation();

                Integer rotation =Rotation(sign.getBlock().getState().getData().toString());
                Station station =Station.getFromLoc(loc);
                if(station ==null) {
                    General.console.log(Level.WARNING,"no station setup");
                    return;
                }
                Spot spot = Station.getSpot(loc,station.letter);

                if(player.haveShip()) player.getOwnedShip().move(spot,rotation);
                else {
                    ShipEntity ship = new ShipEntity(spot,rotation,player.getBukkitPL(),player.getOwnedShipType());
                    player.setOwnedShip(ship);
                }


                SetCooldown(player);
                Define(sign,player.getBukkitPL());
                return;
            }
        }else if (IsAlreadyTaken(sign)){
            player.getBukkitPL().sendMessage(TChat.CannotSpawnShip());
            return;
        }
    }





    public static Integer Rotation (String facing) {
        if (facing.contains("EAST")) return 180;
        else if (facing.contains("WEST")) return 0;
        else if (facing.contains("NORTH")) return 270;
        else return 90;
    }


    public static boolean CooldownCheck(PlayerEntity player){
        if (player.haveCooldowns()) {
            int cooldownTime = 5;//How long cooldown is
            long secondsLeft = CooldownLeft(player,cooldownTime);
            if (HaveCooldown(secondsLeft)) {
                player.getBukkitPL().sendMessage(TChat.TimeLeft(secondsLeft));
                return true;
            }
        }
        return false;
    }





}
