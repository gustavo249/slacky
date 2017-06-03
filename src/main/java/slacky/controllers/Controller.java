package slacky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import slacky.model.Message;
import slacky.model.Webhook;
import slacky.service.HttpUtils;
import slacky.service.JsonReader;
import slacky.service.TokenUrlProvider;
import slacky.service.WebSocketClientEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Created by rcrisan on 5/31/17.
 */
@RestController
public class Controller {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private JsonReader jsonReader;
    @Autowired
    private TokenUrlProvider tokenUrlProvider;
    WebSocketClientEndpoint clientEndpoint;

    @RequestMapping(value = "/getWebhook", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Webhook> getWebhook(@RequestBody String codeAndRedirectUri) throws IOException {
        String code = jsonReader.getValueByKey("code", codeAndRedirectUri);
        String redirectUri = jsonReader.getValueByKey("redirectUri", codeAndRedirectUri);
        String url = tokenUrlProvider.getTokenUri(code, redirectUri);
        System.out.println(url);
        String response = httpUtils.doGet(url);

        if (response.isEmpty() || !Boolean.valueOf(jsonReader.getValueByKey("ok", response))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Webhook webhook = jsonReader.createWebhook(response);
            System.out.println(webhook);
            return new ResponseEntity<>(webhook, OK);
        }
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) throws IOException {
        String response = httpUtils.doPost(message.getWebHookUrl(), message.toString());
        System.out.println(response);
        return new ResponseEntity<>(message, OK);
    }

    @RequestMapping(value = "/connectRealTime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> connectRealTime(@RequestBody String messageInfo) throws URISyntaxException, IOException {
        String code = jsonReader.getValueByKey("code", messageInfo);
        String redirectUri = jsonReader.getValueByKey("redirectUri", messageInfo);

        String requestUrl = getRealTimeConnectionRequestUrl(code, redirectUri);
        String response = httpUtils.doGet(requestUrl);
        clientEndpoint = new WebSocketClientEndpoint(new URI(jsonReader.getValueByKey("url", response)));


        return new ResponseEntity<>(OK);
    }


    @RequestMapping(value = "/sendMessageRealTime", method = RequestMethod.POST)
    public ResponseEntity sendMessageRealTime(@RequestBody String message) {
        if (clientEndpoint == null) {
            return new ResponseEntity(UNAUTHORIZED);
        }

        String text = jsonReader.getValueByKey("text", message);
        StringBuilder messageReturned = new StringBuilder();
        clientEndpoint.setMessageHandler(messageReturned::append);

        clientEndpoint.sendMessage("{\n" +
                "    \"type\": \"message\",\n" +
                "    \"channel\": \"C5KFB96RJ\",\n" +
                "    \"user\": \"U5JSM0GGH\",\n" +
                "    \"text\": \"" + text + "\",\n" +
                "    \"ts\": \"1355517523.000005\"\n" +
                "}");
        System.out.println("Message returned = " + messageReturned.toString());
        return new ResponseEntity(messageReturned.toString() , OK);
    }



    private String getRealTimeConnectionRequestUrl(String code, String redirectUri) throws IOException {
            return "https://slack.com/api/rtm.connect?token=" + getAccessToken(code, redirectUri);
    }

    private String getAccessToken(String code, String redirectUri) throws IOException {
        String url = tokenUrlProvider.getTokenUri(code, redirectUri);
        System.out.println(url);

        return jsonReader.getValueByKey("access_token", httpUtils.doGet(url));
    }
}
