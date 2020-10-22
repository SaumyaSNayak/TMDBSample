package com.saumya.tmdbsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saumya.tmdbsample.datamodels.MovieAPIResponse;
import com.saumya.tmdbsample.datamodels.MovieDetails;
import com.saumya.tmdbsample.network.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private GridLayout categoryGrid;
    LinearLayout filter;
    public static String baseImageURL = "https://image.tmdb.org/t/p/w500";
    public ArrayList<MovieDetails> categoryLists = new ArrayList<>();
    public static HashMap<String, MovieDetails> movies = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryGrid = findViewById(R.id.categoryGrid);
        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(filter, categoryLists);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMovieList();
    }

    public void showPopup(View view, final ArrayList<MovieDetails> movieDetails) {
        final PopupMenu popup = new PopupMenu(MainActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.sorting_option, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.name:
                        Collections.sort(movieDetails, new Comparator<MovieDetails>(){
                            public int compare(MovieDetails obj1, MovieDetails obj2) {
                                return obj1.getTitle().compareToIgnoreCase(obj2.getTitle());
                            }
                        });
                        loadMovies(movieDetails);
                        break;
                    case R.id.date:
                        Collections.sort(movieDetails, new Comparator<MovieDetails>(){
                            public int compare(MovieDetails obj1, MovieDetails obj2) {
                                return obj2.getRelease_date().compareToIgnoreCase(obj1.getRelease_date());
                            }
                        });
                        loadMovies(movieDetails);
                        break;
                    case R.id.rating:
                        Collections.sort(movieDetails, new Comparator<MovieDetails>(){
                            public int compare(MovieDetails obj1, MovieDetails obj2) {
                                return Double.compare(obj2.getVote_average(), obj1.getVote_average());
                            }
                        });
                        loadMovies(movieDetails);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void getMovieList(){
        Call<MovieAPIResponse> checkVersionCall = RestClient.getRestService(getApplicationContext()).getMovieList("c3d6c5ca60b75f82b7817997022f988d");
        checkVersionCall.enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                if(response.isSuccessful()) {
                    MovieAPIResponse category = response.body();
                    movies = new HashMap<>();
                    categoryLists = new ArrayList<>();
                    categoryLists.addAll(category.getResults());
                    for (MovieDetails movie: categoryLists) {
                        movies.put(String.valueOf(movie.getId()), movie);
                    }
                    loadMovies(categoryLists);
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {

            }
        });
    }

    private void loadMovies(final ArrayList<MovieDetails> movieDetails){
        categoryGrid.removeAllViews();
        for (int i = 0; i < movieDetails.size(); i++){
            final LinearLayout categoryItem = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.category_item, null);
            final TextView label = (TextView) categoryItem.findViewById(R.id.label);
            final ImageView icon = (ImageView) categoryItem.findViewById(R.id.icon);
            try {
                Glide.with(MainActivity.this)
                        .load(baseImageURL + movieDetails.get(i).getPoster_path())
                        .asBitmap().placeholder(R.drawable.search_icon).into(icon);
            } catch (Exception e) {
                icon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.search_icon));
            }
            label.setText(String.valueOf(movieDetails.get(i).getId()));
            categoryItem.setTag(false);
            categoryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout item = (LinearLayout) v;
                    TextView label = (TextView) item.findViewById(R.id.label);
                    boolean selected = !(Boolean) v.getTag();
                    item.setTag(selected);
                    Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                    intent.putExtra("filter", label.getText().toString());
                    startActivity(intent);
                }
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setGravity(Gravity.CENTER);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.ALIGN_MARGINS, GridLayout.FILL, 1f);

            categoryItem.setLayoutParams(params);
            categoryGrid.addView(categoryItem);
        }

    }
}