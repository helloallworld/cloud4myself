package cloud4myself.com.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private ProducerFeign producerFeign;
    @GetMapping("test")
    public String test(){
        return "is ok";
    }
    @GetMapping("sayHello")
    public String sayHello(String name){
       return producerFeign.sayHelloUseProducer(name);
    }
}
