package my.than.lagana.web;

import my.than.lagana.core.bake.ILaganaFieldOut;
import my.than.lagana.core.lagana.dashboard.Dashboard;
import my.than.lagana.core.lagana.IDeliFacade;
import my.than.lagana.core.lagana.model.ConfigurationOverview;
import my.than.lagana.core.reap.IFeederOverview;
import my.than.lagana.core.reap.IReapFacade;
import my.than.lagana.core.lagana.model.FetchRequest;
import my.than.lagana.core.lagana.model.FetchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class ApiController {

    IDeliFacade deliFacade;
    IReapFacade reapFacade;

    public ApiController(@Autowired IDeliFacade deliFacade,
                         @Autowired IReapFacade reapFacade) {
        this.deliFacade = deliFacade;
        this.reapFacade = reapFacade;
    }

    @PostMapping("/api/fetch")
    public FetchResponse fetch(@RequestBody(required = true) FetchRequest request) {
        return deliFacade.fetch(request);
    }

    @GetMapping("/api/fetch/schema")
    public List<ILaganaFieldOut> fetchSchema() {
        return deliFacade.fetchSchema();
    }

    @GetMapping("/api/fetch/suggestions/{fieldId}")
    public List<Object> fetchFieldSuggestions(@PathVariable(name = "fieldId") String fieldId) {
        return deliFacade.fetchFieldSuggestions(fieldId);
    }

    @GetMapping("/api/dashboards")
    public List<Dashboard> getDashboards(@RequestBody  Set<String> dashboardNames) {
        return deliFacade.getDashboards(dashboardNames);
    }

    @GetMapping("/api/feeder/{feeder}/start")
    public void start(@PathVariable(value = "feeder") String feeder) {
        reapFacade.start(feeder);
    }


    @GetMapping("/api/feeder/{feeder}/stop")
    public void stop(@PathVariable(value = "feeder") String feeder) {
        reapFacade.stop(feeder);
    }

    @GetMapping("/api/feeder/{feeder}/clear")
    public void clear(@PathVariable(value = "feeder") String feeder) {
        reapFacade.clear(feeder);
    }

    @GetMapping("/api/feeder/{feeder}/register")
    public void register(@PathVariable(value = "feeder") String feeder) {
        reapFacade.register(feeder);
    }

    @GetMapping("/api/feeder/{feeder}/unregister")
    public void unregister(@PathVariable(value = "feeder") String feeder) {
        reapFacade.unregister(feeder);
    }

    @GetMapping("/api/feeder")
    public List<IFeederOverview> getOverview(@RequestBody(required = false) List<String> feeders) {
        return this.reapFacade.getOverview(feeders);
    }

    @GetMapping("/api/conf")
    public ConfigurationOverview get() {
        return deliFacade.getConfOverview();
    }

    @PostMapping("/api/conf")
    public void setConf(@RequestBody ConfigurationOverview configurationOverview) {
        this.deliFacade.saveConfOverview(configurationOverview);
    }
}
