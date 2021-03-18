package my.than.lagana.core.lagana;

import my.than.lagana.core.bake.ILaganaFieldOut;
import my.than.lagana.core.lagana.dashboard.Dashboard;
import my.than.lagana.core.lagana.model.ConfigurationOverview;
import my.than.lagana.core.lagana.model.FetchRequest;
import my.than.lagana.core.lagana.model.FetchResponse;

import java.util.List;
import java.util.Set;

public interface IDeliFacade {

    FetchResponse fetch(FetchRequest request);

    List<Dashboard> getDashboards(Set<String> dashboardNames);

    ConfigurationOverview getConfOverview();

    void saveConfOverview(ConfigurationOverview configurationOverview);

    List<ILaganaFieldOut> fetchSchema();

    List<Object> fetchFieldSuggestions(String fieldId);
}
