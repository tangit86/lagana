package my.than.lagana.core.reap;

import my.than.lagana.core.reap.feeder.FeederState;

public interface IFeederOverview {
    String getName();

    String description();

    String getTargetSeries();


    FeederState getFeederState();
}
