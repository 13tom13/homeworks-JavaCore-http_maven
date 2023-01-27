import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;

public class Main {

    public static final String API_NASA_URL = "https://api.nasa.gov/planetary/apod?api_key=xhsYxFQADqpSRKZOgCj5vceq0oYQWaNXzXgLe08n";

    public static String nasa_response;

    public static URL nasa_response_url;

    public static String nasa_file_name;

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        //запрос на получение url
        HttpGet request_url = new HttpGet(API_NASA_URL);
        request_url.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request_url)) {
            nasa_response = mapper.readValue(response.getEntity().getContent(), Nasa.class).getHdurl();
            String[] hdurl = nasa_response.split("/");
            nasa_file_name = hdurl[hdurl.length - 1];
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //запрос на получение и скачивание файла
        File file = new File(nasa_file_name);
        HttpGet request_file = new HttpGet(nasa_response);
        request_file.setHeader(HttpHeaders.ACCEPT, ContentType.IMAGE_JPEG.getMimeType());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request_file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))){
            bufferedOutputStream.write(bufferedInputStream.readAllBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
