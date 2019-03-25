package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stahi on 4/15/2018.
 */

public class GetUserRelationshipResponse implements Serializable {

    @SerializedName("UserRelationship")
    public ArrayList<UserRelationship> UserRelationship;

    public GetUserRelationshipResponse(){}

}
