package my.than.lagana.core.lagana.dashboard;

import my.than.lagana.core.common.subpub.IPubSubProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DashboardService {

    private IPubSubProvider pubSubProvider;

    public DashboardService(IPubSubProvider pubSubProvider) {
        this.pubSubProvider = pubSubProvider;
    }

    public List<Dashboard> getDashboards(Set<String> dashboardNames) {
        return new ArrayList<>();
    }
}
