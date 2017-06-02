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
                "  \"access_token\": \"xoxp-190277444070-188905016561-189381155345-5122f1654f24dad34bdfd4d49f03b5ea\",\n" +
                "  \"scope\": \"identify,incoming-webhook\",\n" +
                "  \"user_id\": \"U5JSM0GGH\",\n" +
                "  \"team_name\": \"robots\",\n" +
                "  \"team_id\": \"T5L85D222\",\n" +
                "  \"incoming_webhook\": {\n" +
                "    \"channel\": \"#random\",\n" +
                "    \"channel_id\": \"C5KFB975J\",\n" +
                "    \"configuration_url\": \"https://robotscorp.slack.com/services/B5M3PDDDG\",\n" +
                "    \"url\": \"https://hooks.slack.com/services/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\"\n" +
                "  }\n" +
                "}";

        //when
        Webhook webhook = jsonReader.createWebhook(jsonContent);

        //then
        assertEquals("xoxp-190277444070-188905016561-189381155345-5122f1654f24dad34bdfd4d49f03b5ea", webhook.getAccessToken());
        assertEquals("#random", webhook.getChannel());
        assertEquals("C5KFB975J", webhook.getChannelId());
        assertEquals("https://robotscorp.slack.com/services/B5M3PDDDG", webhook.getConfigurationUrl());
        assertEquals("https://hooks.slack.com/services/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", webhook.getUrl());
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