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

    Config config;
    Context context;

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
    }

}
