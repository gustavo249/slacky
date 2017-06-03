package slacky.service;

import org.junit.Before;
import org.junit.Test;
import slacky.model.Message;
import slacky.model.Webhook;

import static org.junit.Assert.*;

/**
 * Created by rcrisan on 6/1/17.
 */
public class JsonReaderTest {
    private JsonReader jsonReader;

    @Before
    public void setUp() throws Exception {
        jsonReader = new JsonReader();
    }

    @Test
    public void createWebhook() throws Exception {
        //given
        String jsonContent = "{\n" +
                "  \"ok\": true,\n" +
                "  \"access_token\": \"TOKEN\",\n" +
                "  \"scope\": \"identify,incoming-webhook\",\n" +
                "  \"user_id\": \"USERID\",\n" +
                "  \"team_name\": \"robots\",\n" +
                "  \"team_id\": \"TEAMID\",\n" +
                "  \"incoming_webhook\": {\n" +
                "    \"channel\": \"#random\",\n" +
                "    \"channel_id\": \"CHANNELID\",\n" +
                "    \"configuration_url\": \"CONFIG_URL\",\n" +
                "    \"url\": \"URL\"\n" +
                "  }\n" +
                "}";

        //when
        Webhook webhook = jsonReader.createWebhook(jsonContent);

        //then
        assertEquals("TOKEN", webhook.getAccessToken());
        assertEquals("#random", webhook.getChannel());
        assertEquals("CHANNELID", webhook.getChannelId());
        assertEquals("CONFIG_URL", webhook.getConfigurationUrl());
        assertEquals("URL", webhook.getUrl());
    }

    @Test
    public void testGetValueAsKey() {
        //given
        String jsonContent = "{\n" +
                "  \"ok\": false,\n" +
                "  \"error\": \"code_already_used\"\n" +
                "}";

        //when
        String firstValue = jsonReader.getValueByKey("ok", jsonContent);
        String secondValue = jsonReader.getValueByKey("error", jsonContent);

        //then
        assertEquals("false", firstValue);
        assertEquals("code_already_used", secondValue);
    }

}