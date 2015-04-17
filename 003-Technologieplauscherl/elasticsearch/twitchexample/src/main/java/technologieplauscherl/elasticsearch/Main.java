package technologieplauscherl.elasticsearch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import technologieplauscherl.elasticsearch.worker.TwitchToElasticsearchWorker;

/**
 * Created by matthias on 13.04.2015.
 */
@ComponentScan
@Configuration
public class Main {
    public static void main(String [] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        TwitchToElasticsearchWorker worker = context.getBean(TwitchToElasticsearchWorker.class);
        worker.run(); //run the worker in the main thread forever
    }
}
