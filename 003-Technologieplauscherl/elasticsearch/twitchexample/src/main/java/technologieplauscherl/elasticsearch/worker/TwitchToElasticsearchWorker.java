package technologieplauscherl.elasticsearch.worker;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technologieplauscherl.elasticsearch.service.ElasticsearchWriterService;
import technologieplauscherl.elasticsearch.service.TwitchReaderService;

import java.util.List;

/**
 * Created by matthias on 13.04.2015.
 */
@Component
public class TwitchToElasticsearchWorker {


    private final ElasticsearchWriterService elasticsearchWriterService;
    private final TwitchReaderService twitchReaderService;

    @Autowired
    public TwitchToElasticsearchWorker(final ElasticsearchWriterService elasticsearchWriterService, final TwitchReaderService twitchReaderService){

        this.elasticsearchWriterService = elasticsearchWriterService;
        this.twitchReaderService = twitchReaderService;
    }

    public void run() {
        while(true){
            List<JSONObject> read = twitchReaderService.read();
            elasticsearchWriterService.save(read);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
