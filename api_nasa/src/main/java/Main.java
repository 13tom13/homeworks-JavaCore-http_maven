import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Main {

    public static final String API_NASA_URL = "https://api.nasa.gov/planetary/apod?api_key=xhsYxFQADqpSRKZOgCj5vceq0oYQWaNXzXgLe08n";

    public static String nasaResponse;

    public static String nasaFileName;

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        //запрос на получение url
        HttpGet requestUrl = new HttpGet(API_NASA_URL);
        requestUrl.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestUrl)) {
            nasaResponse = mapper.readValue(response.getEntity().getContent(), Nasa.class).getHdurl();
            System.out.println();
            String[] hdurl = nasaResponse.split("/");
            nasaFileName = hdurl[hdurl.length - 1];
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //запрос на получение и скачивание файла
        File file = new File(nasaFileName);
        HttpGet requestFile = new HttpGet(nasaResponse);
        requestFile.setHeader(HttpHeaders.ACCEPT, ContentType.IMAGE_JPEG.getMimeType());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(requestFile);
             BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))){
            bos.write(bis.readAllBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
