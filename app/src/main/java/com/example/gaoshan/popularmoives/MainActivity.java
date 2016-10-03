package com.example.gaoshan.popularmoives;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String LOG_Main = MainActivity.class.getSimpleName();

    private String[] mFilmspic = new String[20];

    private ArrayList<Film> mFilmList = new ArrayList<Film>();



    private ImageAdapter mSampleGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GridView gridview = (GridView) findViewById(R.id.grid_view);
        mSampleGridViewAdapter = new ImageAdapter(this);
        gridview.setAdapter(mSampleGridViewAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FilmDetailActivity.class);
                intent.putExtra("film",mFilmList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        upDateData();
    }

    private void upDateData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String unitType = sharedPrefs.getString(getString(R.string.pref_type_key),
                getString(R.string.pref_units_popular));
        FilmsTask filmsTask = new FilmsTask();
        filmsTask.execute(unitType);
    }

    public class FilmsTask extends AsyncTask<String,Void,String[]> {

        private final String LOG_TAG = FilmsTask.class.getSimpleName();



        @Override
        protected void onPostExecute(String[] result) {


            mSampleGridViewAdapter.notifyDataSetChanged();

        }

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String filmJsonStr = null;

            String format = "json";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast




                final String FORECAST_POPULAR_URL =
                        "http://api.themoviedb.org/3/movie/popular?";

                final String FORECAST_RATED_URL =
                        "http://api.themoviedb.org/3/movie/top_rated?";

                final String APPID_PARAM = "api_key";

                String FORECAST_URL;

                if(params[0].equals("popular")){
                     FORECAST_URL = FORECAST_POPULAR_URL;
                }else{
                     FORECAST_URL = FORECAST_RATED_URL;
                }

                Uri builtUri = Uri.parse(FORECAST_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Films_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    filmJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    filmJsonStr = null;
                }
                filmJsonStr = buffer.toString();

                Log.d(LOG_Main, "" + filmJsonStr);

            } catch (Exception e) {
                Log.e("FilmsTask", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                filmJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FilmsTask", "Error closing stream", e);
                    }
                }

                try {
                    return getFilmsDataFromJson(filmJsonStr);
                } catch (Exception e) {
                    Log.e(LOG_Main, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }

        }

        private String[] getFilmsDataFromJson(String filmJsonStr) throws JSONException  {

            final String RESULTS = "results";



                JSONObject filmsJson = new JSONObject(filmJsonStr);
                JSONArray filmsArray = filmsJson.getJSONArray(RESULTS);
                String [] filmspic = new String[filmsArray.length()];

            for (int i = 0; i < filmsArray.length(); i++) {

                JSONObject filmObject = filmsArray.getJSONObject(i);
                Film film = new Film();
                film.setOverview(filmObject.getString("overview"));
                film.setPoster_path(filmObject.getString("poster_path"));
                film.setRelease_date(filmObject.getString("release_date"));
                film.setTitle(filmObject.getString("title"));
                film.setVote_average(filmObject.getInt("vote_average"));

                mFilmList.add(film);

                String pic = filmObject.getString("poster_path");
                filmspic[i] = pic;

            }

            mFilmspic = filmspic.clone();

            Log.d(LOG_Main,"Filmspic[0] =" + mFilmspic[0]);


            return filmspic;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    our custom adapter
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mFilmspic.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ImageView imageView;
//            check to see if we have a view
            if (convertView == null) {
//                no view - so create a new one
                imageView = new ImageView(mContext);
            } else {
//                use the recycled view object
                imageView = (ImageView) convertView;
            }

//            Picasso.with(MainActivity.this).setDebugging(true);
            Picasso.with(MainActivity.this)
                    .load("http://image.tmdb.org/t/p/w185/" + mFilmspic[position])
                 // .load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                    .placeholder(R.drawable.ic_launcher)
                    .noFade().resize(800, 1300)
                    .centerCrop()
                    .into(imageView);
            return imageView;
        }
    }




}
