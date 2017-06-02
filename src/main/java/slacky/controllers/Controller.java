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

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = {"application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Webhook> authenticate(@RequestBody String code) throws IOException {
        String url = getTokenUrl(code.replace("\"", ""));
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

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Message> sendMessage(@RequestBody  Message message) throws IOException {
        String response = httpUtils.doPost(message.getWebHookUrl(), message.toString());
        System.out.println(response);
        return new ResponseEntity<>(message, OK);
    }

    @RequestMapping(value = "/connectRealTime", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> connectRealTime(@RequestBody String code) throws URISyntaxException, IOException {
        String requestUrl = getRealTimeConnectionRequestUrl(code.replace("\"", ""));
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
    }

    private String getTokenUrl(String code) {
        String client_id = "190277444070.189567610469";
        String client_secret = "31af6ac80e7fb7ac2ade99080f74e3b0";
        String redirect_uri = "http://localhost:8080";

        String url = "https://slack.com/api/oauth.access?" +
                "&client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code + "&redirect_uri=" + redirect_uri;
        return url;
    }

    private String getRealTimeConnectionRequestUrl(String code) throws IOException {
        String url = getTokenUrl(code.replace("\"", ""));
        System.out.println(url);
        String token = jsonReader.getValueByKey("access_token", httpUtils.doGet(getTokenUrl(code)));

        String realTimeConnectionUrl = "https://slack.com/api/rtm.connect?token=" + token;
        return realTimeConnectionUrl;
    }
}
