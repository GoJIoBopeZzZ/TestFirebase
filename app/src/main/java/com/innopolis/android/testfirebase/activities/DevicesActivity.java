package com.innopolis.android.testfirebase.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innopolis.android.testfirebase.R;
import com.innopolis.android.testfirebase.models.FlickrFetcher;
import com.innopolis.android.testfirebase.models.GaleryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "DevicesActivity";
    private RecyclerView recyclerView;
    private List<GaleryItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        recyclerView = (RecyclerView) findViewById(R.id.photo_galery_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this , 3));

        new FetchItemTask().execute();

        setAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setAdapter() {
        recyclerView.setAdapter(new PhotoAdapter(items));
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemImageView = (ImageView) itemView.findViewById(R.id.photos_galery_image_view);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GaleryItem> mGaleryItems;

        public PhotoAdapter(List<GaleryItem> mGaleryItems) {
            this.mGaleryItems = mGaleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(DevicesActivity.this);
            View view = inflater.inflate(R.layout.galery_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GaleryItem galeryItem = mGaleryItems.get(position);
            Picasso.with(DevicesActivity.this).
                    load(galeryItem.getUrl()).
                    into(holder.itemImageView);
        }

        @Override
        public int getItemCount() {
            return mGaleryItems.size();
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<GaleryItem>> {
        @Override
        protected List<GaleryItem> doInBackground(Void... voids) {
            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GaleryItem> galeryItems) {
            items = galeryItems;
            setAdapter();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_basket) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void share() {
        Intent message = new Intent(Intent.ACTION_SEND);
        message.setType("text/plain");
        message.putExtra(Intent.EXTRA_EMAIL, "apple.tyomka@gmail.com");
        message.putExtra(Intent.EXTRA_SUBJECT, "Тестовое письмо");
        message.putExtra(Intent.EXTRA_TEXT, "Рам папапапам папапам пам пам");
        startActivity(message);
    }
}
