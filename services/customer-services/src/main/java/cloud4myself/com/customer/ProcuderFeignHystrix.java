//package cloud4myself.com.customer;
//
//import feign.hystrix.FallbackFactory;
//import org.springframework.stereotype.Component;
//
////@Component
////public class ProcuderFeignHystrix implements FallbackFactory<ProducerFeign> {
////
////
////    @Override
////    public ProducerFeign create(Throwable throwable) {
////        return new ProducerFeign(){
////
////            @Override
////            public String sayHelloUseProducer(String name) {
////                return "服务调用失败";
////            }
////        };
////    }
////}
//
//@Component
//public class ProcuderFeignHystrix implements ProducerFeign {
//
//
//    @Override
//    public String sayHelloUseProducer(String name) {
//        return "服务调用失败";
//    }
//}
