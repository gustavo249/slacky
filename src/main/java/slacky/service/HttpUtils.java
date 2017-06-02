package slacky.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rcrisan on 6/1/17.
 */
@Service
public class HttpUtils {
    @Autowired
    private JsonReader jsonReader;

    public String doGet(String url) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code = " + responseCode);

        String body = getResponseAsString(connection);

        System.out.println(body);
        return body;
    }

    private String getResponseAsString(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        String encoding = connection.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;

        return IOUtils.toString(inputStream, encoding);
    }

    public String doPost(String url, String body) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");


        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes(body);
        dataOutputStream.flush();
        dataOutputStream.close();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code:" + responseCode);

        return getResponseAsString(connection);
    }
}
