package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DocumentPhotos implements Serializable {

//Ce vine din API, SerializedName = fix numele din api

    public DocumentPhotos() {
    }

    @SerializedName("title")
    public String Title;

    @SerializedName("docStringImage")
    public String DocStringImage;

/*    @SerializedName("docImage")
    public byte[] DocImage;*/

    @SerializedName("clientId")
    public int ClientId;
}