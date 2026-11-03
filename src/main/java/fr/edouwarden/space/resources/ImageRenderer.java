package fr.edouwarden.space.resources;

import fr.edouwarden.space.variables.General;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

/**
 * Draws a bounty poster on a map.
 *
 * <p>The local frame ({@code bounty.png}) is required. The avatar is optional: when
 * {@code rendering.avatar-url} is empty only the frame and the bounty value are drawn.</p>
 */
public class ImageRenderer extends MapRenderer {

    private final SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public ImageRenderer(String avatarUrl, long bounty) throws IOException {
        this.cacheImage = new SoftReference<>(render(avatarUrl, bounty));
    }

    public ImageRenderer(BufferedImage image) {
        this.cacheImage = new SoftReference<>(image);
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if (hasRendered) {
            return;
        }
        BufferedImage image = cacheImage.get();
        if (image == null) {
            player.sendMessage(ChatColor.RED + "The bounty image was released before rendering.");
            hasRendered = true;
            return;
        }
        canvas.drawImage(20, 0, image);
        writeCache(view, image);
        hasRendered = true;
    }

    private void writeCache(MapView view, BufferedImage image) {
        File output = new File(General.mapsFolderPath + view.getId() + ".jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            General.console.log(java.util.logging.Level.FINE,
                    "Cannot write map cache " + output.getName(), e);
        }
    }

    private BufferedImage render(String avatarUrl, long bounty) throws IOException {
        BufferedImage source = ImageIO.read(new File(General.bountyFramePath));
        if (source == null) {
            throw new IOException("Missing bounty frame: " + General.bountyFramePath);
        }
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            BufferedImage avatar = resize(new URL(avatarUrl), new Dimension(75, 75));
            Graphics graphics = source.getGraphics();
            graphics.drawImage(avatar, 10, 27, null);
            graphics.dispose();
        }
        Graphics graphics = source.getGraphics();
        graphics.setColor(java.awt.Color.WHITE);
        graphics.drawString(String.valueOf(bounty), 17, 120);
        graphics.dispose();
        return source;
    }

    private BufferedImage resize(URL url, Dimension size) throws IOException {
        BufferedImage image = ImageIO.read(url);
        if (image == null) {
            throw new IOException("Cannot read avatar URL");
        }
        BufferedImage resized = new BufferedImage(
                size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(image, 0, 0, size.width, size.height, null);
        graphics.dispose();
        return resized;
    }
}
