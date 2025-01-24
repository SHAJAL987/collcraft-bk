package com.middlewareExp.collcraft.servicesImpl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.middlewareExp.collcraft.payload.*;
import com.middlewareExp.collcraft.services.CollCraftService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollCraftServiceImpl implements CollCraftService {

    @Value("${apex.api.url}")
    private String BASE_URL;

   @Override
    public CollectionResponse saveCollection(String serviceId, String timestamp, CollectionRequest request) {
        CollectionResponse response = new CollectionResponse();
        HttpResponse<String> res = null;
        String keystorePassword = "apex123";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("actionType",request.getActionType());
            jsonObject.put("collName",request.getCollName());
            jsonObject.put("coll",request.getColl());
            jsonObject.put("collType",request.getCollType());
            jsonObject.put("name",request.getName());
            jsonObject.put("status",request.getStatus());

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream("cert/keystore.jks");
            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(keystoreStream, keystorePassword.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, keystorePassword.toCharArray());

            // Create SSL context
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Configure Unirest to use custom SSL context
            Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom().setSSLContext(sslContext).build());

            res = Unirest.post(BASE_URL+"/collcraft/v1/collection")
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "PostmanRuntime/7.30.0")
                    .body(jsonObject)
                    .asString();

            JSONObject jsonResponseObject = new JSONObject(res.getBody());
            String resMsg = jsonResponseObject.getString("responseMessage");

            if (res.getStatus()==200 && resMsg.equals("SUCCESS")){
                response.setResponseCode("100");
                response.setResponseMessage("Operation Successful.");
            }else {
                response.setResponseCode("000");
                response.setResponseMessage("Operation not Successful.");
            }

            response.setServiceId(serviceId);
            response.setRequestId(request.getRequestId());
            response.setTimeStamp(timestamp);

        } catch (Exception e) {
            response.setResponseCode("000");
            response.setResponseMessage("Operation not Successful.");
        }
        return response;
    }

    @Override
    public GetAllCollections getAllCollections(String serviceId, String timestamp) {
       GetAllCollections response = new GetAllCollections();
        HttpResponse<String> res = null;
        String keystorePassword = "apex123";
        try {

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream("cert/keystore.jks");
            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(keystoreStream, keystorePassword.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, keystorePassword.toCharArray());

            // Create SSL context
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Configure Unirest to use custom SSL context
            Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom().setSSLContext(sslContext).build());

            res = Unirest.get(BASE_URL+"/collcraft/v1/getAllCollections")
                    .header("User-Agent", "PostmanRuntime/7.30.0")
                    .asString();

            JSONObject jsonResponseObject = new JSONObject(res.getBody());
            JSONArray items = jsonResponseObject.getJSONArray("items");
            List<Collections> collectionsList = new ArrayList<>();

            // Loop through each item in the 'items' array and populate Collections
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Collections collections = new Collections();

                // Set values to Collections object
                collections.setId(item.getInt("id"));
                collections.setType(item.getString("type"));
                collections.setName(item.getString("name"));
                collections.setCreateDate(item.getString("create_date"));
                collections.setUpdateDate(item.isNull("update_date") ? null : item.getString("update_date"));
                collections.setStatus(item.getString("status"));
                collections.setColl(item.getString("coll"));
                collections.setCollName(item.getString("coll_name"));

                // Add the populated object to the list
                collectionsList.add(collections);
            }
            response.setData(collectionsList);
            response.setServiceId(serviceId);
            response.setTimeStamp(timestamp);

        } catch (Exception e) {
            response.setResponseCode("000");
            response.setResponseMessage("Operation not Successful.");
        }
        return response;
    }

    @Override
    public CollectionResponse uploadCollection(String serviceId, String timestamp, MultipartFile file,String actionType,String collType,String name,String status,String correlationId) {
        CollectionResponse response = new CollectionResponse();
        HttpResponse<String> res = null;
        String keystorePassword = "apex123";
        try {

            String jsonContent = new String(file.getBytes());
            String uploadedFileName = file.getOriginalFilename();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("actionType",actionType);
            jsonObject.put("collName",uploadedFileName);
            jsonObject.put("coll",jsonContent);
            jsonObject.put("collType",collType);
            jsonObject.put("name",name);
            jsonObject.put("status",status);

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream("cert/keystore.jks");
            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(keystoreStream, keystorePassword.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, keystorePassword.toCharArray());

            // Create SSL context
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Configure Unirest to use custom SSL context
            Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom().setSSLContext(sslContext).build());

            res = Unirest.post(BASE_URL+"/collcraft/v1/collection")
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "PostmanRuntime/7.30.0")
                    .body(jsonObject)
                    .asString();

            JSONObject jsonResponseObject = new JSONObject(res.getBody());
            String resMsg = jsonResponseObject.getString("responseMessage");

            if (res.getStatus()==200 && resMsg.equals("SUCCESS")){
                response.setResponseCode("100");
                response.setResponseMessage("Operation Successful.");
            }else {
                response.setResponseCode("000");
                response.setResponseMessage("Operation not Successful.");
            }

            response.setServiceId(serviceId);
            response.setRequestId(correlationId);
            response.setTimeStamp(timestamp);

        } catch (Exception e) {
            response.setResponseCode("000");
            response.setResponseMessage("Operation not Successful.");
        }
        return response;
    }

    @Override
    public DownloadColl downloadCollection(String serviceId, String timestamp, String collId, String correlationId) {
        DownloadColl response = new DownloadColl();
        HttpResponse<String> res = null;
        String keystorePassword = "apex123";
        try {

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream("cert/keystore.jks");
            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(keystoreStream, keystorePassword.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, keystorePassword.toCharArray());

            // Create SSL context
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Configure Unirest to use custom SSL context
            Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom().setSSLContext(sslContext).build());

            res = Unirest.get(BASE_URL+"/collcraft/v1/getCollById")
                    .header("User-Agent", "PostmanRuntime/7.30.0")
                    .header("collId", collId)
                    .asString();

            JSONObject jsonResponseObject = new JSONObject(res.getBody());
            JSONArray items = jsonResponseObject.getJSONArray("items");

            // Loop through each item in the 'items' array and populate Collections
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                response.setColl(item.getString("coll"));
                response.setCollName(item.getString("coll_name"));
            }

        } catch (Exception e) {
            response.setColl("not found");
            response.setCollName("coll_name");
        }
        return response;
    }
}
