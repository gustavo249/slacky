package slacky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Message> sendMessage(@RequestBody  Message message) throws IOException {
        String response = httpUtils.doPost(message.getWebHookUrl(), message.toString());
        System.out.println(response);
        return new ResponseEntity<>(message, OK);
    }

    /*@RequestMapping(value = "/connectRealTime/{code}/{redirectUri}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> connectRealTime() throws URISyntaxException, IOException {
        String requestUrl = getRealTimeConnectionRequestUrl(code, redirectUri);
        String response = httpUtils.doGet(requestUrl);
        final WebSocketClientEndpoint clientEndpoint = new WebSocketClientEndpoint(new URI(jsonReader.getValueByKey("url", response)));

        StringBuilder messageReturned = new StringBuilder();
        clientEndpoint.setMessageHandler(messageReturned::append);

        clientEndpoint.sendMessage("{\n" +
                "    \"type\": \"message\",\n" +
                "    \"channel\": \"C5KFB96RJ\",\n" +
                "    \"user\": \"U5JSM0GGH\",\n" +
                "    \"text\": \"Hello world motherfuckers\",\n" +
                "    \"ts\": \"1355517523.000005\"\n" +
                "}");
        System.out.println("Message returned = " + messageReturned.toString());

        return new ResponseEntity<>(messageReturned.toString(), OK);
    }*/

//    private String getRealTimeConnectionRequestUrl(String code, String redirectUri) throws IOException {
//        String url = getTokenUrl(code.replace("\"", ""), redirectUri);
//        System.out.println(url);
//        String token = jsonReader.getValueByKey("access_token", httpUtils.doGet(getTokenUrl(code, redirectUri)));
//
//        String realTimeConnectionUrl = "https://slack.com/api/rtm.connect?token=" + token;
//        return realTimeConnectionUrl;
//    }
}
