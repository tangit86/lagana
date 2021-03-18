package my.than.lagana.core.reap;

import my.than.lagana.core.common.StringUtils;
import my.than.lagana.core.reap.conf.FeederSchema;
import my.than.lagana.core.reap.conf.ReapConfiguration;
import my.than.lagana.core.reap.conf.ReapSchema;
import my.than.lagana.core.reap.feeder.FeederState;
import my.than.lagana.core.reap.feeder.IFeeder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseReapFacade implements IReapFacade {

    final static Logger logger = LoggerFactory.getLogger(BaseReapFacade.class);

    private final ReapConfiguration reapConfiguration;

    Map<String, IFeeder> feederMap = new HashMap<>();


    private final Object mutex = new Object();


    public BaseReapFacade(ReapConfiguration reapConfiguration) {
        this.reapConfiguration = reapConfiguration;
    }

    @Override
    public void start(String feeder) {
        synchronized (mutex) {
            if (feederMap.containsKey(feeder)) {
                IFeeder current = feederMap.get(feeder);
                if (current.getFeederState().equals(FeederState.STOPPED)) {
                    current.start();
                } else {
                    logger.warn(StringUtils.format("Feeder {} is already running", feeder));
                }
            } else {
                logger.warn(StringUtils.format("Could not find Feeder: {} to start.", feeder));
            }
        }
    }

    @Override
    public void stop(String feeder) {
        synchronized (mutex) {
            if (feederMap.containsKey(feeder)) {
                feederMap.get(feeder).stop();
            } else {
                logger.warn(StringUtils.format("Could not find Feeder: {} to stop.", feeder));
            }
        }
    }

    @Override
    public void clear(String feeder) {
        
    }

    @Override
    public void register(IFeeder feeder) {
        synchronized (mutex) {
            if (feederMap.containsKey(feeder.getName())) {
                IFeeder current = this.feederMap.get(feeder.getName());
                if (!current.getFeederState().equals(FeederState.STOPPED)) {
                    current.stop();
                }
                this.feederMap.put(feeder.getName(), feeder);
            } else {
                feederMap.put(feeder.getName(), feeder);
            }
        }
    }

    @Override
    public void register(String feederName) {
        IFeeder feederInstance = this.reapConfiguration.loadFeeders().stream().filter(f->f.getName().equals(feederName)).findFirst().orElse(null);
        if(feederInstance!=null){
            this.register(feederInstance);
            setRegisteredInSchema(feederName,true);
        }
        logger.error(StringUtils.format("could not find feeder with name {}",feederName));
    }

    @Override
    public void unregister(String feederName) {
        synchronized (mutex) {
            if (feederMap.containsKey(feederName)) {
                IFeeder current = this.feederMap.get(feederName);
                if (!current.getFeederState().equals(FeederState.STOPPED)) {
                    current.stop();
                }
                this.feederMap.remove(feederName);
                setRegisteredInSchema(feederName,false);
            } else {
                logger.warn(StringUtils.format("Could not find Feeder: {} to delete.", feederName));
            }
        }
    }

    private void setRegisteredInSchema(String feederName, boolean state) {
        ReapSchema schema = this.reapConfiguration.getSchema();

        boolean found = false;

        for(FeederSchema fs : schema.feeders){
            if(fs.name.equals(feederName)){
                found = true;
                fs.isRegistered = state;
            }
        }
        if(!found){
            logger.error(StringUtils.format("Could not find feeder schema for {} , to set isRegistered: {}",feederName,state));
            return;
        }
        this.reapConfiguration.persistSchema(schema);
    }

    @Override
    public List<IFeederOverview> getOverview(List<String> feeders) {
        return this.feederMap.values().stream()
                .filter(obj -> feeders == null || feeders.isEmpty() || feeders.contains(obj.getName()))
                .map(obj -> (IFeederOverview) obj).collect(Collectors.toList());
    }

    @Override
    public ReapSchema getSchema() {
        return this.reapConfiguration.getSchema();
    }

    @Override
    public void saveSchema(ReapSchema reapSchema) {
        this.reapConfiguration.persistSchema(reapSchema);
    }

    public void init() {
        this.reapConfiguration.loadFeeders().stream().forEach(
                feeder -> this.feederMap.put(feeder.getName(), feeder)
        );
    }
}
