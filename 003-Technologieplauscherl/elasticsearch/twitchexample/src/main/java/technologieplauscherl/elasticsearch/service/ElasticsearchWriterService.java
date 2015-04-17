package technologieplauscherl.elasticsearch.service;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technologieplauscherl.elasticsearch.utils.ElasticSearchUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by matthias on 13.04.2015.
 */
@Component
public class ElasticsearchWriterService {

    private final ElasticSearchUtils elasticSearchUtils;

    @Autowired
    public ElasticsearchWriterService(final ElasticSearchUtils elasticSearchUtils){
        this.elasticSearchUtils = elasticSearchUtils;
    }

    public void save(List<JSONObject> games) {
        Node elasticsearchNode = elasticSearchUtils.createElasticsearchNode();
        Client client = elasticsearchNode.client();
        BulkProcessor bulkProcessor = elasticSearchUtils.getBulkProcessor(client, 1000);
        Date requestTime = new Date();
        for(JSONObject game : games){
            game.put("requestTime", requestTime);
            String documentId = UUID.randomUUID().toString();
            IndexRequest gameIndexRequest = new IndexRequest("twitch", "game");
            gameIndexRequest.id(documentId);
            gameIndexRequest.source(game);
            bulkProcessor.add(gameIndexRequest);
        }

        bulkProcessor.close();
        elasticsearchNode.close();
    }
}
