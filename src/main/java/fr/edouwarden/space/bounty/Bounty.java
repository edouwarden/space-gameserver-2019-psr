package fr.edouwarden.space.bounty;

import fr.edouwarden.space.player.PlayerEntity;

import java.util.UUID;

public class Bounty {
    public static void set(PlayerEntity player, Long bounty, UUID logUser){//TODO loguser
        if(bounty <= 0L) bounty = 0L;
        else if(bounty >= 10000000L) bounty = 10000000L;
        player.setbounty(bounty);
        if(player.haveLinkedMap())
            BountyPoster.update(player.getBukkitPL(), player.getLinkedMap(), player.getLinkedMap());
        else
            BountyPoster.generate(player,player.getbounty());
    }




    public static void add(PlayerEntity player,Long bounty, UUID logUser){
        if(player.hasBounty())
            set(player,player.getbounty()+bounty,logUser);
        else
            set(player,bounty,logUser);
    }
    public static void remove(PlayerEntity player, UUID logUser){
        if(player.hasBounty())
            set(player, 0L,logUser);
    }
    public static void take(PlayerEntity player,Long bounty, UUID logUser){
        if(player.hasBounty()) {
            set(player, player.getbounty() - bounty, logUser);
        }
    }

}
