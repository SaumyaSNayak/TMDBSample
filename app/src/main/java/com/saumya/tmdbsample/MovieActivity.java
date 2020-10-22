package com.saumya.tmdbsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.saumya.tmdbsample.datamodels.MovieDetails;

import static com.saumya.tmdbsample.MainActivity.baseImageURL;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable= getResources().getDrawable(R.drawable.back_button);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), bitmap);
        newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(newdrawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = findViewById(R.id.title);
        TextView overview = findViewById(R.id.overview);
        TextView rating = findViewById(R.id.rating);
        TextView date = findViewById(R.id.date);
        ImageView icon = findViewById(R.id.icon);

        MovieDetails movie = MainActivity.movies.get(getIntent().getExtras().getString("filter"));

        toolbar.setTitle(movie.getTitle());

        try {
            Glide.with(MovieActivity.this)
                    .load(baseImageURL + movie.getBackdrop_path())
                    .asBitmap().placeholder(R.drawable.search_icon).into(icon);
        } catch (Exception e) {
            icon.setImageDrawable(ContextCompat.getDrawable(MovieActivity.this, R.drawable.search_icon));
        }

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        rating.setText("TMDb " + movie.getVote_average());
        date.setText(movie.getRelease_date());
    }
}