package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetDocumentPhotosRelationshipResponse implements Serializable {

    @SerializedName("DocumentPhotos")
    public ArrayList<DocumentPhotos> MyDocumentPhotos;

    public GetDocumentPhotosRelationshipResponse() {
    }
}