package cloud4myself.com.producer;

import cloud4myself.com.common.exception.GeneralException;
import cloud4myself.com.common.exception.SystemErrorCode;
import cloud4myself.com.common.web.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    @GetMapping("sayhello")
    public ResponseMessage producer(String name){
        return  ResponseMessage.success("hello "+name);
    }
//    @Value("${config.name}")
//    private String name;
//    @Value("${producer.privateName}")
//    private String privateName;
//    @Value("${public.name}")
//    private String publicName;
//    @GetMapping("fromConfig")
//    public String getInfoFromConfig(){
//        return "name="+name+",privateName="+privateName+",publicName="+publicName;
//    }
    @GetMapping("testerror")
    public ResponseMessage testError(){
        System.out.println("in test error");
//        int i=1/0;
        throw new GeneralException(EnumProducerError.DB_ERROR);
    }
}
