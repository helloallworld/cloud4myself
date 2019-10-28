package cloud4myself.com.gateway.config;

import cloud4myself.com.gateway.route.MyRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicZuulConfig {
    @Autowired
    private ZuulProperties zuulProperties;
    @Autowired
    private ServerProperties serverProperties;
    @Bean
    public MyRouteLocator routeLocator(){
        MyRouteLocator routeLocator=new MyRouteLocator(serverProperties.getServlet().getServletPrefix(),zuulProperties);
        return routeLocator;
    }
}
