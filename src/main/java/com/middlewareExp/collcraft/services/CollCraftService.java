package com.middlewareExp.collcraft.services;

import com.middlewareExp.collcraft.payload.CollectionRequest;
import com.middlewareExp.collcraft.payload.CollectionResponse;
import com.middlewareExp.collcraft.payload.GetAllCollections;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface CollCraftService {
    CollectionResponse saveCollection(String serviceId, String timestamp, CollectionRequest request);
    GetAllCollections getAllCollections(String serviceId, String timestamp);
    CollectionResponse uploadCollection(String serviceId, String timestamp, MultipartFile file,String actionType,String collType,String name,String status,String correlationId);
}
