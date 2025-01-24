package com.middlewareExp.collcraft.controller;

import com.middlewareExp.collcraft.payload.CollectionRequest;
import com.middlewareExp.collcraft.payload.CollectionResponse;
import com.middlewareExp.collcraft.payload.DownloadColl;
import com.middlewareExp.collcraft.payload.GetAllCollections;
import com.middlewareExp.collcraft.services.CollCraftService;
import com.middlewareExp.collcraft.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/middlewareExp/collcraft/v1")
public class CollCraftController {
    @Autowired
    private CollCraftService collCraftService;
    @Autowired
    private Utilities utilities;
    @PostMapping("/saveColl")
    public ResponseEntity saveColl(
            @RequestBody CollectionRequest request
    ) {
        ResponseEntity response;

        try {
            CollectionResponse objResponse = collCraftService.saveCollection(utilities.getServiceID(), utilities.getTimeStamp(), request);
            if (objResponse != null)
                response = ResponseEntity.ok(objResponse);
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
            return response;
        } catch (Exception e) {
            return utilities.handleException(e);
        }
    }

    @GetMapping("/getColl")
    public ResponseEntity getColl() {
        ResponseEntity response;

        try {
            GetAllCollections objResponse = collCraftService.getAllCollections(utilities.getServiceID(), utilities.getTimeStamp());
            if (objResponse != null)
                response = ResponseEntity.ok(objResponse);
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
            return response;
        } catch (Exception e) {
            return utilities.handleException(e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity saveColl(
            @RequestParam("file")MultipartFile file,
            @RequestParam("actionType") String actionType,
            @RequestParam("collType") String collType,
            @RequestParam("name") String name,
            @RequestParam("status") String status,
            @RequestParam("correlationId") String correlationId
    )
    {
        ResponseEntity response;
        try {
            CollectionResponse objResponse = collCraftService.uploadCollection(
                    utilities.getServiceID(),
                    utilities.getTimeStamp(),
                    file,
                    actionType,
                    collType,
                    name,
                    status,
                    correlationId
            );
            if (objResponse != null)
                response = ResponseEntity.ok(objResponse);
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
            return response;
        } catch (Exception e) {
            return utilities.handleException(e);
        }
    }

    @GetMapping("/download")
    public ResponseEntity downloadColl(
            @RequestHeader("id") String collId,
            @RequestHeader("correlationId") String correlationId
    ){
        try {
            DownloadColl objResponse = collCraftService.downloadCollection(
                    utilities.getServiceID(),
                    utilities.getTimeStamp(),
                    collId,
                    correlationId
            );
            byte[] collectionBytes = objResponse.getColl().getBytes(StandardCharsets.UTF_8);
            ByteArrayResource resource = new ByteArrayResource(collectionBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objResponse.getCollName() + "\"")
                    .contentLength(collectionBytes.length)
                    .body(resource);

        } catch (Exception e) {
            return utilities.handleException(e);
        }
    }
}
