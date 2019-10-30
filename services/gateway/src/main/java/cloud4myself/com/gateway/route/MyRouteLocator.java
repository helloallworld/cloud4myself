package cloud4myself.com.gateway.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    @Autowired
    private ZuulProperties properties;
    public MyRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    public void refresh() {
        doRefresh();
    }
    @Override
    protected Map<String,ZuulProperties.ZuulRoute> locateRoutes(){
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        //从application.properties中加载路由信息
//        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
//        routesMap.putAll(locateRoutesFromDB());
        routesMap.putAll(staticRoutes());
        //优化一下配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                //加前缀，
                //spring.cloud.config.server.jdbd.sql（cloud可从数据库中读取ZuulProperties，还未深入研究，再config-service中再研究）
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }else {
                //假装zuulProperties中有配置prefix
//                path="/api/v1"+path;
                if(!path.startsWith("/")){
                    path="/"+path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }
    //假装从数据库取动态的路由
    private Map<String, ZuulProperties.ZuulRoute> staticRoutes(){
        Map<String,ZuulProperties.ZuulRoute> routeMap=new LinkedHashMap<>();
        ZuulProperties.ZuulRoute zuulRoute=new ZuulProperties.ZuulRoute();
        //服务名，即spring.application.name
        zuulRoute.setServiceId("producer-service");
        //id
        zuulRoute.setId("producer");
        //匹配规则
        zuulRoute.setPath("/producer/**");
        /**
         * zuulRoute.setStripPrefix()
         * true:请求/producer/**  ==> 转为 /**
         * false:请求/producer/** ==> 转为 /producer/**   (如果还想请求到可在controller上增加@RequestMapping("/producer"))
         *
         * 与/api/v1的前缀无关，仅仅表示ZuulRoute中的转换
         * /api/v1仅仅是在  protected Map<String,ZuulProperties.ZuulRoute> locateRoutes()方法中给请求对应的地址加前缀
         * 即：如果在locateRoutes()方法中给路由加上了/api/v1
         *     只有请求/api/v1/producer/** 的地址才有路由
         *          此时zuulRoute.setStripPrefix()为true时  /api/v1/producer/** ===》 /**
         *              zuulRoute.setStripPrefix()为false时  /api/v1/producer/** ===》 /producer/**
         *     请求/producer/** 的地址是找不到对应路由的
         *
         * 总结：zuul.prefix的值设置的是：前端请求与网关的联系映射
         *       zuulRoute.setStripPrefix 是 gateway 与 各个服务间的映射是否去掉path中的/producer
         */
//        zuulRoute.setStripPrefix(false);
//        zuulRoute.setRetryable(false);
//        routeMap.put("client01",zuulRoute);
//        zuulRoute.setUrl("www.baidu.com");
        zuulRoute.setUrl(null);
        //以path值为key,
        routeMap.put("/producer/**",zuulRoute);
//        ZuulProperties.ZuulRoute zuulRoute1=new ZuulProperties.ZuulRoute();
//        zuulRoute1.setServiceId("zuul-service");
//        zuulRoute1.setId("zuul");
//        zuulRoute1.setPath("/api/v1/zuul/**");
//        zuulRoute1.setStripPrefix(false);
//        routeMap.put("/zuul/**",zuulRoute1);

        return routeMap;
    }
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        List<ZuulRoutePo> results=new ArrayList<>();
        //从数据库中读取路由配置
//        results=zuulRoutePoRepository.selectAll();
        for (ZuulRoutePo result : results) {
            if((result.getPath()==null || result.getPath() =="") || (result.getUrl()==null ||result.getUrl()=="") ){
                continue;
            }
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            //设置zuulRoute的值得
            try {
                org.springframework.beans.BeanUtils.copyProperties(result,zuulRoute);
            } catch (Exception e) {

            }
            routes.put(zuulRoute.getPath(),zuulRoute);
        }
        return routes;
    }
}
