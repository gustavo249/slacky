package slacky.model;

/**
 * Created by rcrisan on 6/3/17.
 */
public class TokenUrl {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private String tokenUri;

    public TokenUrl(String clientId, String clientSecret, String code, String redirectUri, String tokenUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    @Override
    public String toString() {
        return tokenUri +
                "?&client_id=" + clientId + "&client_secret=" + clientSecret+ "&code=" + code + "&redirect_uri=" + redirectUri;
    }
}
