package ro.ebob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
public class IntegrationApp {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationApp.class);

    public static void main(String[] args) {
        SpringApplication.run(IntegrationApp.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(MsgGtw gtw) {
        return args -> {
            gtw.sendGreeting("TEST");
        };
    }

    @Bean
    ApplicationRunner whatChannels(List<MessageChannel> channels, MsgGtw gtw){
        return args -> {
            channels.forEach(c -> LOG.error("CHANNEL: {}", c));
        };
    }

    @Component
    static class MyMessageSource implements MessageSource<String> {  //poll or wait for events

        @Override
        public Message<String> receive() {
            return MessageBuilder.withPayload(text()).build();
        }

        private static String text() {
            return Math.random() > 0.5 ? "Hello world @" + Instant.now() + "!" : "Hola el mundo @" + Instant.now() + "!";
        }
    }

    @Bean
    MessageChannel atob() {
        return MessageChannels.direct().getObject();
    }

//    @Bean
//    IntegrationFlow flowA(MyMessageSource myMessageSource) {
//        return IntegrationFlow.from(myMessageSource, spec -> spec.poller(pollerFactory -> pollerFactory.fixedRate(Duration.ofSeconds(1))))
//            .filter(String.class, (GenericSelector<String>) source -> source.contains("Hola"))
//            .transform((GenericTransformer<String, String>) String::toUpperCase)
//            //handler(GenericHandler returning null acts like a filter)
//            .channel(atob())
////            .handle((GenericHandler<String>) (payload, headers) -> {
////                LOG.info("The payload is {}", payload);
////                return null;//terminates the pipeline
////            })
//            .get();
//    }

//    @Bean
//    IntegrationFlow flowB() {
//        return IntegrationFlow
//                .from(atob())
//                .transform((GenericTransformer<String, String>) source -> source.toUpperCase())
//                .handle((GenericHandler<String>) (payload, headers) -> {
//                    LOG.info("flowB: {}", payload);
//                    return null;//terminates the pipeline
//                }).get();
//    }
        @ServiceActivator(inputChannel = "atob")//equivalent with the above
        public void exampleHandler(String payload) {
            LOG.warn("Service: {}", payload);
        }
}
//
//@MessagingGateway(defaultRequestChannel = "greetings", defaultReplyChannel = "greetingsResults", defaultRequestTimeout = "1000", defaultReplyTimeout = "1000")
//interface GreetingsClient {
//    String greet(String text);
//}

@MessagingGateway(defaultRequestChannel = "atob")
interface MsgGtw {

    //@Gateway(requestChannel="atob")
    void sendGreeting(String greeting);

}