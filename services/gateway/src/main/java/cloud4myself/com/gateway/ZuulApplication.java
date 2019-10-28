package cloud4myself.com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@RefreshScope
public class ZuulApplication {
    public static void main(String[] args) {
        ApplicationContext context= SpringApplication.run(ZuulApplication.class,args);
    }
}
