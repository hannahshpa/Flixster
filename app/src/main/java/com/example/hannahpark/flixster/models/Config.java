package com.example.hannahpark.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
//used when getting the movie images
public class Config {
    //the base url for loading images
    String imageBaseUrl;
    //the poster size to use for fetching images, part of the url
    String posterSize;
    //backdrop size for landscape orientation for fetching images, part of the url
    String backdropSize;


    // no-arg, empty constructor required for Parceler
    public Config() {}

    //constructor
    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        //get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        //get the backdrop size
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        //parse the backdrop sizes and use the option at index 1 or w780 as a fallback
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper method for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    //getter functions
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}

