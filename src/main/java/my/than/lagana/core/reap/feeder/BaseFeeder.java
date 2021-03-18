package my.than.lagana.core.reap.feeder;

import my.than.lagana.core.common.StringUtils;
import my.than.lagana.core.common.subpub.IPubSubProvider;
import my.than.lagana.core.reap.matcher.IMatcher;
import my.than.lagana.core.reap.reader.IFeedStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;


public class BaseFeeder implements IFeeder {

    final static Logger logger = LoggerFactory.getLogger(BaseFeeder.class);

    Thread worker;
    final String name;
    String description;
    final String targetSeries;
    final IFeedStreamReader inputStreamReader;
    final IMatcher matcher;
    IPubSubProvider<FeederMessage> pubSubProvider;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String getTargetSeries() {
        return targetSeries;
    }


    public BaseFeeder(String name, String description, String targetSeries, IFeedStreamReader inputStreamReader, IMatcher matcher, IPubSubProvider<FeederMessage> pubSubProvider) {
        this.name = name;
        this.description = description;
        this.targetSeries = targetSeries;
        this.inputStreamReader = inputStreamReader;
        this.matcher = matcher;
        this.pubSubProvider = pubSubProvider;
    }

    @Override
    public void start() {
        if (this.getFeederState().equals(FeederState.STOPPED)) {
            worker = new Thread(() -> this.run());
            worker.start();
            logger.info("feeder started...");
        } else {
            logger.warn("feeder already running...");
        }
    }

    @Override
    public void stop() {
        if (!this.getFeederState().equals(FeederState.STOPPED)) {
            worker.interrupt();
            logger.info("feeder stopped...");
        } else {
            logger.warn("feeder already stopped...");
        }
    }

    private void run() {
        try {
            BufferedReader reader = new BufferedReader(inputStreamReader.get());
            while (inputStreamReader.isAlive()) {
                for(String line = reader.readLine();line!=null;line=reader.readLine()){
                    FeederMessage output = this.matcher.match(line);
                    if (output != null) {
                        pubSubProvider.publish(output);
                    }
                }
            }
            logger.info("Feeder {} completed...",this.name);
        } catch (Exception e) {
            logger.error("Feeder: {} crashed while reading stream {}, reason: \\n {}", this.name, inputStreamReader.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public FeederState getFeederState() {
        if (worker == null || worker.isInterrupted() || worker.getState() == Thread.State.NEW || worker.getState() == Thread.State.TERMINATED) {
            return FeederState.STOPPED;
        }
        return FeederState.ACTIVE;
    }

}
