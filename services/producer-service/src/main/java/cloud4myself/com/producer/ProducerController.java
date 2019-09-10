package cloud4myself.com.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    @GetMapping("sayhello")
    public String producer(String name){
        return "hello "+name;
    }
}
