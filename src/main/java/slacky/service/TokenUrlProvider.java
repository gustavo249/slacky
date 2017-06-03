package slacky.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import slacky.model.TokenUrl;

/**
 * Created by rcrisan on 6/3/17.
 */
@Service
public class TokenUrlProvider {
    @Value(value = "${client.id}")
    private String clientId;

    @Value(value = "${client.secret}")
    private String clientSecret;

    @Value(value = "${token.uri}")
    private String tokenUri;

    public String getTokenUri(String code, String redirectUri) {
        return new TokenUrl(clientId, clientSecret, code, redirectUri, tokenUri).toString();
    }
}
