package slacky.model;

/**
 * Created by rcrisan on 6/1/17.
 */
public class Message {
    private String text;
    private String webHookUrl;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWebHookUrl() {
        return webHookUrl;
    }

    public void setWebHookUrl(String webHookUrl) {
        this.webHookUrl = webHookUrl;
    }

    @Override
    public String toString() {
        return "{\"text\": \"" + text + "\"}";
    }
}
