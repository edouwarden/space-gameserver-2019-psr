package fr.edouwarden.space.resources;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTags {
    public static void setTag(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(player.getName());
        if (team == null)
            team = board.registerNewTeam(player.getName());
        team.addPlayer(player);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        //team.setCanSeeFriendlyInvisibles();
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setScoreboard(board);
        }
    }
}
