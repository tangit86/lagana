package my.than.lagana.core.lagana;

import my.than.lagana.core.bake.ILaganaFieldOut;
import my.than.lagana.core.lagana.conf.ConfigurationService;
import my.than.lagana.core.lagana.dashboard.Dashboard;
import my.than.lagana.core.lagana.dashboard.DashboardService;
import my.than.lagana.core.lagana.model.ConfigurationOverview;
import my.than.lagana.core.lagana.model.FetchRequest;
import my.than.lagana.core.lagana.model.FetchResponse;
import my.than.lagana.core.lagana.search.SearchService;

import java.util.List;
import java.util.Set;

public class BaseDeliFacade implements IDeliFacade {

    private ConfigurationService configurationService;
    private SearchService searchService;
    private DashboardService dashboardService;

    public BaseDeliFacade(SearchService searchService, DashboardService dashboardService,ConfigurationService configurationService) {
        this.searchService = searchService;
        this.dashboardService = dashboardService;
        this.configurationService = configurationService;
    }

    @Override
    public FetchResponse fetch(FetchRequest request) {

        return this.searchService.fetch(request);
    }

    @Override
    public List<Dashboard> getDashboards(Set<String> dashboardNames) {
        return this.dashboardService.getDashboards(dashboardNames);
    }

    @Override
    public ConfigurationOverview getConfOverview() {
        return this.configurationService.getOverview();
    }

    @Override
    public void saveConfOverview(ConfigurationOverview configurationOverview) {
        this.configurationService.saveOverview(configurationOverview);
    }

    @Override
    public List<ILaganaFieldOut> fetchSchema() {
        return searchService.getSearchSchema();
    }

    @Override
    public List<Object> fetchFieldSuggestions(String fieldId) {
        return searchService.getFieldSuggestions(fieldId);
    }
}
