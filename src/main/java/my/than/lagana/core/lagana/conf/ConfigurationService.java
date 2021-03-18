package my.than.lagana.core.lagana.conf;

import my.than.lagana.core.bake.IBakeFacade;
import my.than.lagana.core.common.LaganaConfiguration;
import my.than.lagana.core.lagana.model.ConfigurationOverview;
import my.than.lagana.core.reap.IReapFacade;

public class ConfigurationService {
    IBakeFacade bakeFacade;
    IReapFacade reapFacade;
    LaganaConfiguration laganaConfiguration;

    public ConfigurationService(IBakeFacade bakeFacade, IReapFacade reapFacade, LaganaConfiguration laganaConfiguration) {
        this.bakeFacade = bakeFacade;
        this.reapFacade = reapFacade;
        this.laganaConfiguration = laganaConfiguration;
    }

    public ConfigurationOverview getOverview() {
        ConfigurationOverview overview = new ConfigurationOverview();
        overview.reapSchema = reapFacade.getSchema();
        return overview;
    }

    public void saveOverview(ConfigurationOverview overview){
        //reapFacade.saveSchema(overview.reapSchema);
    }

}
