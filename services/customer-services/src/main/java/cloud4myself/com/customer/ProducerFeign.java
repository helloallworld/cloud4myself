package cloud4myself.com.customer;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "producer-service",fallback = ProcuderFeignHystrix.class)
//@FeignClient(name = "producer-service")
public interface ProducerFeign {

    /**
     * 参数必须用@RequestParam注解，要不然会将name封装在http的body中，传到producer-service时会是POST方法
     * @param name
     * @return
     */
    @RequestMapping(value = "/sayhello",method = RequestMethod.GET)
    public String sayHelloUseProducer(@RequestParam(value = "name") String name);
}
