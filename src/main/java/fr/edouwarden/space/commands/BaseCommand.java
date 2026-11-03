package fr.edouwarden.space.commands;

import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.commands.ArgumentCompletion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class BaseCommand implements TabExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(fr.edouwarden.space.variables.TChat.usage());
            return true;
        }
        if (args.length > 0) {
            try {
                Class className = Class.forName("fr.edouwarden.space.commands."+args[0].toUpperCase(Locale.ROOT)+"Command");
                Method method = className.getDeclaredMethod("onCommand", CommandSender.class, Command.class, String.class, String[].class);
                method.invoke(null, sender,command,label,args);
                return true;
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                     | IllegalAccessException e) {
                General.console.log(java.util.logging.Level.FINE, "Cannot invoke subcommand", e);
            }
        }
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return ArgumentCompletion.base1;
        }else {
            try {
                List<String> b = (List<String>) ArgumentCompletion.class.getDeclaredField(args[0].toLowerCase().toString()+String.valueOf(args.length - 1)).get(List.class);
                if(b !=null)
                    return b;
            } catch (Exception e) {
            }

        }

       return null;
    }

}
