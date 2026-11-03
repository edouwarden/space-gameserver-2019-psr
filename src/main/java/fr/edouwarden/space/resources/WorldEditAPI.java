package fr.edouwarden.space.resources;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditAPI {
    public static Clipboard paste(String name, int angle, Location pos) throws IOException, MaxChangedBlocksException {
        AffineTransform transform = (new AffineTransform()).rotateY(angle);
        EditSession extent = com.sk89q.worldedit.WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(pos.getWorld()), -1);
        File file = new File(General.plugin.getDataFolder() + "/schematics/" + name + ".schem");
        Clipboard clipboard;

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        }
        BlockVector3 loc = BlockVector3.at(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ()).add(clipboard.getOrigin().getX(),-clipboard.getOrigin().getY(),-clipboard.getOrigin().getZ());
        ForwardExtentCopy copy = new ForwardExtentCopy((Extent)clipboard, clipboard.getRegion(),(Extent)extent, extremePoint(angle,clipboard.getOrigin(),pos.toVector())); //clipboard.getOrigin()., (Extent)extent, pos);
        if (!transform.isIdentity())
            copy.setTransform((Transform)transform);
        Operations.completeLegacy((Operation)copy);
        extent.flushSession();

        return clipboard;
    }
    public static BlockVector3 extremePoint(int rotation, BlockVector3 additional, Vector origin){
        int X=additional.getBlockX(),Z = additional.getBlockZ();
        if (rotation == 90) {//WEST
            X = -additional.getBlockZ();
            Z = additional.getBlockX();
        } else if (rotation == 0) {//EAST
            X = -additional.getBlockX();
            Z = -additional.getBlockZ();
        }else if (rotation == 270) {//EAST
            X = additional.getBlockZ();
            Z = -additional.getBlockX();
        }

        return BlockVector3.at(origin.getBlockX()+X,origin.getBlockY()-additional.getBlockY(),origin.getBlockZ()+Z);
    }
}
