package ro.ebob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
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
    ApplicationRunner whatChannels(List<MessageChannel> channels){
        return args -> {
            channels.forEach(c -> LOG.info(c.toString()));
            this.greetingsResults().subscribe((payload) -> {
                LOG.info("SUBSCRIBED:: {}", payload);
            });
        };
    }

    @Component
    static class MyMessageSource implements MessageSource<String> {  //poll or wait for events

        @Override
        public Message<String> receive() {
            return MessageBuilder.withPayload(text()).build();
        }

        public static String text() {
            return Math.random() > 0.5 ? "Hello world @" + Instant.now() + "!" : "Hola el mundo @" + Instant.now() + "!";
        }
    }

    @Bean
    MessageChannel greetings() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    DirectChannel greetingsResults() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    IntegrationFlow flow(MyMessageSource myMessageSource) {
        return IntegrationFlow
                .from("greetings")
                .filter(String.class, (GenericSelector<String>) source -> source.contains("Hola"))
                .transform((GenericTransformer<String, String>) String::toUpperCase)
                //handler(GenericHandler returning null acts like a filter)
                .channel("greetingsResults")
//                .handle((GenericHandler<String>) (payload, headers) -> {
//                    LOG.info("RESULT:: {}", payload);
//                    return null;//terminates the pipeline
//                })
                .get();
    }
    //@Bean
    IntegrationFlow fromResults() {
        return IntegrationFlow.from(greetingsResults()).handle((GenericHandler<String>) (payload, headers) -> {
                    LOG.info("fromResults:: {}", payload);
                    return null;//terminates the pipeline
                }).get();
    }
}

@Component
class Runner implements ApplicationRunner {

    final GreetingsClient gateway;

    Runner(GreetingsClient gateway) {
        this.gateway = gateway;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 10; i++) {
            gateway.greet(IntegrationApp.MyMessageSource.text());
        }
    }
}

@MessagingGateway(defaultRequestChannel = "greetings", defaultReplyChannel = "greetingsResults", defaultRequestTimeout = "1000", defaultReplyTimeout = "1000")
interface GreetingsClient {
    String greet(String text);
}