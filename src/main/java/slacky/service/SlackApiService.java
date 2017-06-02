package slacky.service;

import org.springframework.stereotype.Service;
import slacky.model.Webhook;

/**
 * Created by rcrisan on 6/1/17.
 */
@Service
public class SlackApiService {
    private Webhook webhook;

    public SlackApiService() {
    }

    public SlackApiService(Webhook webhook) {
        this.webhook = webhook;
    }


}
