package fr.edouwarden.space.power.abilities;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.power.Power;

/**
 * Placeholder ability. The behaviour was never implemented in the 2019 build;
 * the class is kept so the power registry and stored ids stay stable.
 */
public class SmokePower implements Power {

    @Override
    public boolean useSkills(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public String getName() {
        return "smoke";
    }
}
