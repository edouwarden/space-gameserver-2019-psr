package fr.edouwarden.space.power;

import fr.edouwarden.space.player.PlayerEntity;

import java.io.Serializable;

public interface Power extends Serializable {
    abstract boolean useSkills(PlayerEntity player);
    abstract boolean isProtected();

    abstract String getName();

     default boolean fallingDamage(){
        return true;
     }


}
