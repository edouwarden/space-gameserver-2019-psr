package fr.edouwarden.space.jobs;

import fr.edouwarden.space.player.JobsDatas;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.logging.Level;

public class JobAction {

    public static boolean hadOne(Player player) {
        if(PlayerEntity.getPlayerEntity(player).getJobDatas() ==null || !PlayerEntity.getPlayerEntity(player).getJobDatas().haveJob())
            return false;
        else return true;
    }
    public static boolean clickOnJobStuffs(Player player, Block block)  {
        if (hadOne(player)){

        }
        return false;
    }
    public static void set(Player player,String jobStr, boolean forced){
        PlayerEntity playerEntity = PlayerEntity.getPlayerEntity(player);
        JobsDatas jobsDatas = playerEntity.getJobDatas();
        Jobs job = Jobs.valueOf(jobStr.toUpperCase());
        boolean isChanged = jobsDatas.setNewJob(Jobs.valueOf(jobStr.toUpperCase()),forced);
        //TODO if job don't work playerEntity.setJobDatas(jobsDatas);
        if (!isChanged) {// && General.powerPlayers.getString(playerUUID) !=null) {
            General.console.log(Level.WARNING, player.getName() + " cannot take the job " + job + "; current job started " + String.valueOf(LocalDate.now().minusDays(jobsDatas.getJobSwitchDate().toEpochDay()).toEpochDay()));//TODO change
        } else {
            player.sendMessage(TChat.jobChanged(jobStr));
        }
    }
    public static void useBlock(PlayerEntity player, Block clickedBlock){
        customJobRunWithBlock(player,clickedBlock,"clickBlock");
    }
    public static void breakBlock(PlayerEntity player, Block block){
        customJobRunWithBlock(player,block,"mineBlock");
    }
    public static void customJobRun(PlayerEntity player, String action){
        Method method = null;

        try {
            method = player.getJobDatas().getJob().Class.getMethod(action, PlayerEntity.class);
            method.invoke(null, player);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static void customJobRunWithBlock(PlayerEntity player,Block block, String action){
        Method method = null;
        try {
            method = player.getJobDatas().getJob().Class.getMethod(action,Block.class, PlayerEntity.class);
            method.invoke(null,block, player);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void remove(Player victime) {
        String playerUUID = victime.getUniqueId().toString();
        PlayerEntity ocVictime = PlayerEntity.getPlayerEntity(victime);
        ocVictime.getJobDatas().setNewJob(null,true);

    }
}
