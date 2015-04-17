package technologieplauscherl.elasticsearch.service;

/**
 * Created by matthias on 13.04.2015.
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Reader for retrieving a json string via twitch rest api.
 */
@Component
public class TwitchReaderService {
    public List<JSONObject> read() {
        List<JSONObject> allGames = new ArrayList<JSONObject>();
        JSONArray gamesBatch = null;
        int offset = 0;
        while(gamesBatch == null || !gamesBatch.isEmpty()) {
            gamesBatch = (JSONArray)getGames(offset).get("top");
            offset += 100;
            allGames.addAll(gamesBatch);
        }
        return allGames;
    }

    private JSONObject getGames(int offset) {
        Client client = Client.create();
        WebResource resource = client.resource("https://api.twitch.tv/kraken/games/top?limit=" + 100 + "&offset=" + offset);
        ClientResponse response = resource.accept("applicaton/json").get(ClientResponse.class);

        if(response.getStatus() != 200){
            System.err.println("Something went wrong when reading from twitch! HTTP Error code: " + response.getStatus());
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject)new JSONParser().parse(response.getEntity(String.class));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
