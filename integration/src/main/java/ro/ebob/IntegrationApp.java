package ro.ebob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

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
        };
    }

    @Bean
    ApplicationRunner sendGreetings(){
        return args -> {
            for(int i =0;i<10;i++) {
                greetings().send(MessageBuilder.withPayload(text()).build());
            }
        };
    }

    private String text() {
        return Math.random() > 0.5 ? "Hello world @" + Instant.now() + "!" : "Hola el mundo @" + Instant.now() + "!";
    }
    @Bean
    IntegrationFlow flow() {
        return IntegrationFlow
                .from(greetings())
                .filter(String.class, (GenericSelector<String>) source -> source.contains("Hola"))
                .transform((GenericTransformer<String, String>) String::toUpperCase)
                //.channel(atob())
                .handle((GenericHandler<String>) (payload, headers) -> {
                    LOG.info("The payload is {}", payload);
                    return null;//terminates the pipeline
                })
                .get();
    }

    @Bean
    MessageChannel greetings() {
        return MessageChannels.direct().getObject();
    }
}