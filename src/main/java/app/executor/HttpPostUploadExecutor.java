package app.executor;

import app.entity.UploadOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.zeroturnaround.exec.ProcessResult;

public class HttpPostUploadExecutor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static HttpPostUploadExecutor instance;
    private  ObjectMapper mapper = new ObjectMapper();


    public HttpPostUploadExecutor() {
        this.instance = this;
    }

    public static HttpPostUploadExecutor getInstance() {
        return instance;
    }

    public int httpFormPost(String urlString, Map<String, String> formDataMap, JTextArea outputTextArea) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = getHttpPost(urlString, formDataMap);
        CloseableHttpResponse response = null;

        StringJoiner stringJoiner = new StringJoiner(", ");
        formDataMap.forEach((key, value) -> stringJoiner.add(key + ":" + value));

        String auth = Base64.getEncoder().encodeToString((UploadOption.getInstance().getId() + ":" + UploadOption.getInstance().getPassword()).getBytes());
        request.addHeader("Authorization", "Basic " + auth);


        try {
            response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() > 299) {
                outputTextArea.append("***File Upload Fail. Error Code : " + response.getStatusLine().getStatusCode() + " ***\n");
                outputTextArea.append("[uploadInfo] " + stringJoiner + "\n");
                if (response.getEntity() != null) {
                    HttpEntity entity = response.getEntity();
                    if (entity.getContent() != null) {
                        InputStream content = entity.getContent();
                        String responseContent = new String(IOUtils.toByteArray(content), StandardCharsets.UTF_8);
                        logger.info("Post Result : " + responseContent);
                        outputTextArea.append("[Result Info] " + responseContent);
                    }
                }
                return -1;

            } else {
                logger.debug("***File Upload Success. Upload Info : " + stringJoiner);
                return 1;
            }


        } catch (IOException e) {
            logger.error("Post Error.", e);
            logger.error("[uploadInfo] " + stringJoiner);

            outputTextArea.append("***File Upload Fail Error : " + e.getMessage() + "***\n");
            outputTextArea.append("[uploadInfo] " + stringJoiner);
            return -1;
        }


    }

    public int httpGet(String urlString, JTextArea outputTextArea) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(urlString);

        CloseableHttpResponse response = null;

        String auth = Base64.getEncoder().encodeToString((UploadOption.getInstance().getId() + ":" + UploadOption.getInstance().getPassword()).getBytes());
        request.addHeader("Authorization", "Basic " + auth);
        try {
            response = httpClient.execute(request);
            return  response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            logger.error("http get error : {}", urlString, e);
            outputTextArea.append("Error Checking Status Of : {}" + urlString + "\n");
            return -1;
        }
    }


    private HttpPost getHttpPost(String urlString, Map<String, String> formDataMap) {
        HttpPost request = new HttpPost(urlString);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (Map.Entry<String, String> entry : formDataMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.contains("-File")) {
                String fileKey = key.replace("-File", "");
                builder.addBinaryBody(fileKey, new File(value));
            } else {
                builder.addTextBody(key ,value, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));
            }
        }

        HttpEntity multipart = builder.build();
        request.setEntity(multipart);
        return request;
    }


}
