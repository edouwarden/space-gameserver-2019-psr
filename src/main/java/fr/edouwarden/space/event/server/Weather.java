package fr.edouwarden.space.event.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {
    @EventHandler
    public static void rain(WeatherChangeEvent event){
        if (event.toWeatherState())event.setCancelled(true);
    }
    @EventHandler
    public static void thunder(ThunderChangeEvent event){
        if (event.toThunderState())event.setCancelled(true);
    }
}
