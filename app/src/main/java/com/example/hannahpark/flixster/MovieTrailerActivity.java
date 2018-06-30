package com.example.hannahpark.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hannahpark.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {


    //constants
    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieTrailerActivity";
    Movie movie;

    //instance fields associated with a specific of movie list activity
    AsyncHttpClient client;
    String videoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        //initialize the client
        client = new AsyncHttpClient();
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        getTrailer();

    }

    //get the list of currently playing movies from the API
    private void getTrailer() {
        //create the url that will be accessed by taking the base url and endpoint
        String url
                = API_BASE_URL +
                "/movie/" +
                movie.getId() +
                "/videos";

        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into the movies list
                try {
                    JSONArray results = response.getJSONArray("results");

                    //set default video
                    JSONObject trailer1 = results.getJSONObject(0);
                    videoId = trailer1.getString("key");

                    //iterate through result set and create Movie objects
                    for(int i = 0; i < results.length(); i++) {
                        JSONObject trailer = results.getJSONObject(i);
                        String type = trailer.getString("type");

                        if(type.equals("Trailer")){
                            videoId = trailer.getString("key");
                            break;
                        }

                    }
                    //indicates number of trailers
                    Log.i(TAG, String.format("Loaded %s trailers", results.length()));
                    loadTrailer();

                } catch (JSONException e) {
                    logError("Failed to get Trailer", e, true);
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure to get data from videos endpoint", throwable, true);
            }
        });
    }

    //handle errors, log, and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent error
        if (alertUser) {
            //show a long toast with the error message
            //let the user know something went wrong
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void loadTrailer() {
        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }


        }