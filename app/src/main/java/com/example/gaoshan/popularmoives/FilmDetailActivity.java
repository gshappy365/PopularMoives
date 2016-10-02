package com.example.gaoshan.popularmoives;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends AppCompatActivity {

    TextView filmtitle,release_date,overview,vote_average;
    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        filmtitle = (TextView) findViewById(R.id.filmtitle);
        release_date = (TextView) findViewById(R.id.release_date);
        overview = (TextView) findViewById(R.id.overview);
        vote_average = (TextView) findViewById(R.id.vote_average);

        pic = (ImageView) findViewById(R.id.pic);

        Intent intent = getIntent();
        if(intent != null ){
           Film film = intent.getParcelableExtra("film");
            filmtitle.setText(film.getTitle());
            release_date.setText(film.getRelease_date());
            overview.setText(film.getOverview());
            vote_average.setText(film.getVote_average() + "");
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + film.getPoster_path()).into(pic);

        }
    }
}
