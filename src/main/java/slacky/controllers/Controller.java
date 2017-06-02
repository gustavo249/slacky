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

import java.io.IOException;

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
    public ResponseEntity<Webhook> authenticate(@RequestBody String url) throws IOException {
        System.out.println(url);
        String response = httpUtils.doGet(url.replace("\"", ""));
        if (response.isEmpty() || !Boolean.valueOf(jsonReader.getValueByKey("ok", response))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Webhook webhook = jsonReader.createWebhook(response);
            return new ResponseEntity<>(webhook, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Message> sendMessage(@RequestBody  Message message) throws IOException {
        String response = httpUtils.doPost(message.getWebHookUrl(), message.toString());
        System.out.println(response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
