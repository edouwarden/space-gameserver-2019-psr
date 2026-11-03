package fr.edouwarden.space.resources;

import fr.edouwarden.space.variables.General;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getName;
import static org.bukkit.Bukkit.getServer;

public final class EconomySetup {

    private EconomySetup() {
    }

    public static void setup() {
        if (!setupEconomy()) {
            General.console.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getName()));
            getServer().getPluginManager().disablePlugin(General.plugin);
        }
    }

    private static boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        General.econ = rsp.getProvider();
        return General.econ != null;
    }
}
