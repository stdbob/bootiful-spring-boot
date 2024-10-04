package ro.ebob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
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
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@IntegrationComponentScan
public class IntegrationApp {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationApp.class);

    public static void main(String[] args) {
        SpringApplication.run(IntegrationApp.class, args);
    }

    @Bean
    ApplicationRunner whatChannels(List<MessageChannel> channels, GarbageGateway gateway) {
        return args -> {
            channels.forEach(c -> LOG.error("CHANNEL: {}", c));
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter some input: ");

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                System.out.println("You entered: " + input);
                gateway.sendGarbage(input);
                System.out.println("Please enter some input: ");

            }

            scanner.close();
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

//    @Bean
//    CommandLineRunner commandLineRunner(MsgGtw gtw) {
//        return args -> {
//            gtw.sendGarbage(MyMessageSource.text());
//            Thread.sleep(10000);
//        };
//    }

    @Bean
    MessageChannel garbageIn() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    MessageChannel garbageOut() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    IntegrationFlow garbageFlow(MyMessageSource myMessageSource) {
        return IntegrationFlow.from("garbageIn")
//                .filter(String.class, (GenericSelector<String>) source -> source.startsWith("Hola"))
                .transform((GenericTransformer<String, String>) String::toUpperCase)
                //handler(GenericHandler returning null acts like a filter)
            .channel("garbageOut")
//                .handle((GenericHandler<String>) (payload, headers) -> {
//                    LOG.info("The payload is {}", payload);
//                    return null;//terminates the pipeline
//                })
                .get();
    }

    @ServiceActivator(inputChannel = "garbageOut")//equivalent with the above
    public void garbageOutHandler(String payload) {
        System.out.println("garbageOut: " + payload);
    }
}

@MessagingGateway
interface GarbageGateway {

    @Gateway(requestChannel = "garbageIn", replyTimeout = 0)
    void sendGarbage(String message);
}