package com.example.gaoshan.popularmoives;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.gaoshan.popularmoives.databinding.FilmdetailBinding;
import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FilmdetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.filmdetail);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent != null ){
           Film film = intent.getParcelableExtra("film");
            binding.filmtitle.setText(film.getTitle());
            binding.releaseDate.setText(film.getRelease_date());
            binding.overview.setText(film.getOverview());
            binding.voteAverage.setText(film.getVote_average() + "");
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + film.getPoster_path()).into(binding.pic);

        }
    }
}
