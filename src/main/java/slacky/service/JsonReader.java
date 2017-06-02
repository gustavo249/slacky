package slacky.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Service;
import slacky.model.Webhook;

import java.io.IOException;

/**
 * Created by rcrisan on 6/1/17.
 */
@Service
public class JsonReader {
    private ObjectMapper objectMapper;

    public JsonReader() {
        objectMapper = new ObjectMapper();
    }

    public Webhook createWebhook(String jsonContent) {
        Webhook webhook = new Webhook();

        try {
            JsonNode jsonNode = objectMapper.readTree(jsonContent);

            webhook.setAccessToken(jsonNode.get("access_token").asText());
            webhook.setChannel(jsonNode.get("incoming_webhook").get("channel").asText());
            webhook.setChannelId(jsonNode.get("incoming_webhook").get("channel_id").asText());
            webhook.setConfigurationUrl(jsonNode.get("incoming_webhook").get("configuration_url").asText());
            webhook.setUrl(jsonNode.get("incoming_webhook").get("url").asText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return webhook;
    }

    public String getValueByKey(String key, String content) {
        String value = "";
        try {
            JsonNode node = objectMapper.readTree(content);
            value = node.get(key).asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
