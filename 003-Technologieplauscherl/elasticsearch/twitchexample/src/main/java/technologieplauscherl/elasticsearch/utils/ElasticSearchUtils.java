package technologieplauscherl.elasticsearch.utils;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by matthias6 on 18.03.2015.
 **
 */
@Component
public class ElasticSearchUtils {

    public Client createElasticsearchTransportClient() {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("client.transport.sniff", true)
                .put("cluster.name", "twitchcluster")
                .build();

        //We need to provide the address of one node. From there on the client detects all other nodes within the cluster
        //because the sniff option was enabled in the settings above.
        return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("172.27.237.20", 9300));
    }

    public Node createElasticsearchNode() {
        return NodeBuilder.nodeBuilder().clusterName("twitchcluster").client(true).node();
    }

    public BulkProcessor getBulkProcessor(Client c, int batchSize) {
        return BulkProcessor.builder(c, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                //System.out.println(new Date().toString() + "+++ STARTING WITH BULK INDEX OF " + request.numberOfActions() + " DOCUMENTS");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                System.out.println("+++ FINISHED INDEXING ANOTHER BULK +++");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                failure.printStackTrace();
            }
        }).setBulkActions(batchSize).build(); //Default bulk size is 1000 if nothing is set. By extending the batch size you have to ensure, that your ES cluster can handle the load!
    }

}
