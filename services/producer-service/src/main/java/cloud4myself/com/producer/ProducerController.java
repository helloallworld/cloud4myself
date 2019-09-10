package cloud4myself.com.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    @GetMapping("sayhello")
    public String producer(String name){
        return "hello "+name;
    }
    @Value("${config.name}")
    private String name;
    @Value("${producer.privateName}")
    private String privateName;
    @Value("${public.name}")
    private String publicName;
    @GetMapping("fromConfig")
    public String getInfoFromConfig(){
        return "name="+name+",privateName="+privateName+",publicName="+publicName;
    }
}
