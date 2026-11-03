package fr.edouwarden.space.jobs;

import java.io.Serializable;

public enum Jobs implements Serializable {
    FABRICATOR(Blacksmith.class),
    NUTRIENT(fr.edouwarden.space.jobs.Cook.class),
    SENTINEL(fr.edouwarden.space.jobs.Patrol.class),
    ASTROGATOR(fr.edouwarden.space.jobs.Navigator.class),
    STATIONMASTER(fr.edouwarden.space.jobs.StationOwner.class);


    public final java.lang.Class Class;

    Jobs(Class Class) {
        this.Class = Class;
    }
}
