package com.example.hannahpark.flixster;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hannahpark.flixster.models.Config;
import com.example.hannahpark.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    //movie with its detail displayed
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivImage;
    TextView date;

    //instance fields associated with a specific of movie list activity
    AsyncHttpClient client;

    Config config;
    Context context;

    //constants
    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_activity);

        //resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.ratingBar);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        date = (TextView) findViewById(R.id.date);

        //initialize the client
        client = new AsyncHttpClient();

        //Unwrap the movie that is passed in via intent, using its simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        date.setText(movie.getDate());

        //vote average is 0, 1, ...10 so convert to 0...5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage>0 ? voteAverage / 2.0f : voteAverage);

        //steps below in order to load image
        // unwrap the config passed in via intent, using its simple name as a key
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        context = getApplicationContext();

        //get the proper imageurl
        String imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        //get proper placeholder
        int placeholderID = R.drawable.flicks_backdrop_placeholder;


        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(20, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(ivImage);
//
//        getVideos();
    }

//
//    //get the list of currently playing movies from the API
//    private void getVideos() {
//        //create the url that will be accessed by taking the base url and endpoint
//        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos" ;
//        //set the request parameters
//        RequestParams params = new RequestParams();
//        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
//        //execute a GET request expecting a JSON object response
//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                //load the results into the movies list
//                try {
//                    JSONArray results = response.getJSONArray("key");
//                    //iterate through result set and create Movie objects
//                    for(int i = 0; i < results.length(); i++) {
//                        Movie movie = new Movie(results.getJSONObject(i));  //create new Movie
//                        movies.add(movie);  //add to movies list
//                        //notify adapter that a row was added
//                        adapter.notifyItemInserted(movies.size()-1);
//                    }
//                    //indicates number of movies
//                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
//
//                } catch (JSONException e) {
//                    logError("Failed to parse now playing movies", e, true);
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                logError("Failure to get data from now playing endpoint", throwable, true);
//            }
//        });
//    }
//


}
