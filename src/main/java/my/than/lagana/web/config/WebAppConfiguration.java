package my.than.lagana.web.config;

import my.than.lagana.core.bake.BaseBakeFacade;
import my.than.lagana.core.bake.IBakeFacade;
import my.than.lagana.core.bake.LaganaRecord;
import my.than.lagana.core.bake.conf.BakeConfiguration;
import my.than.lagana.core.common.LaganaConfiguration;
import my.than.lagana.core.common.subpub.IPubSubProvider;
import my.than.lagana.core.lagana.BaseDeliFacade;
import my.than.lagana.core.lagana.conf.ConfigurationService;
import my.than.lagana.core.lagana.IDeliFacade;
import my.than.lagana.core.lagana.dashboard.DashboardService;
import my.than.lagana.core.lagana.search.SearchService;
import my.than.lagana.core.reap.BaseReapFacade;
import my.than.lagana.core.reap.IReapFacade;
import my.than.lagana.core.reap.conf.ReapConfiguration;
import my.than.lagana.core.reap.feeder.FeederMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebAppConfiguration {

    @Value("${path}")
    private String appConfPath;

    @Bean
    public LaganaConfiguration getLaganaConfForWebController() throws IOException {
        return new LaganaConfiguration(appConfPath);
    }

    @Bean
    public IReapFacade getReapFacade(IPubSubProvider<FeederMessage> pubSubProvider) throws IOException {
        BaseReapFacade fc = new BaseReapFacade(new ReapConfiguration(appConfPath, pubSubProvider));
        fc.init();
        return fc;
    }

    @Bean
    public IBakeFacade getBakeFacade(IPubSubProvider<LaganaRecord> pubsubProvider) throws IOException {
        return new BaseBakeFacade(new BakeConfiguration(appConfPath), pubsubProvider);
    }

    @Bean
    public IDeliFacade getDeliFacade(IBakeFacade bakeFacade, IReapFacade reapFacade, DummyBakeDeliPubSubProvider pubsubProvider, LaganaConfiguration laganaConfiguration) {
        SearchService searchService = new SearchService(bakeFacade);
        DashboardService dasboardService = new DashboardService(pubsubProvider);
        ConfigurationService configurationService = new ConfigurationService(bakeFacade, reapFacade, laganaConfiguration);
        return new BaseDeliFacade(searchService, dasboardService, configurationService);
    }

    @Bean
    public IPubSubProvider<FeederMessage> forReapBakeComm(IBakeFacade bakeFacade) {
        return new DummyReapBakePubSubProvider(bakeFacade);
    }

    @Bean
    public IPubSubProvider<LaganaRecord> forBakeDeliDashboards() {
        return new DummyBakeDeliPubSubProvider();
    }

    // for dev purposes
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("http://localhost:3000");
            }
        };
    }


}
