package com.example.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.pets.data.petContract;
import com.example.pets.data.petDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import static com.example.pets.data.petContract.petEntry.CONTENT_URI;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int PET_LOADER=0;
    PetCursorAdapter mPetCursorAdapter;
    private petDbHelper mhelper;
    petContract petObject=new petContract();
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        mhelper=new petDbHelper(MainActivity.this);

        //set Adapter here
        ListView petListView=(ListView) findViewById(R.id.text_test_1);


        mPetCursorAdapter =new PetCursorAdapter(this,null);
        petListView.setAdapter(mPetCursorAdapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri= ContentUris.withAppendedId(CONTENT_URI,id);


                intent.putExtra("currentPetId",position);


                intent.setData(currentPetUri);
                startActivity(intent);

            }
        });


        getSupportLoaderManager().initLoader(PET_LOADER, null, this);

    }

     ///   Log.d(TAG, "displayDatabaseInfo: successfully ended");

    private void insertpet(){
        SQLiteDatabase db= mhelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(petContract.petEntry.COLUMN_PET_NAME,"toto");
        values.put(petContract.petEntry.COLUMN_PET_BREED,"toring");
        values.put(petContract.petEntry.COLUMN_PET_GENDER, petContract.petEntry.GENDER_MALE);
        values.put(petContract.petEntry.COLUMN_PET_WEIGHT,"14");

//       long newRowId=db.insert(petContract.petEntry.TABLE_NAME,null,values);
//        Log.v("MainActivity","newRowId"+newRowId);
        Uri newUri = getContentResolver().insert(petContract.petEntry.CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertpet();

                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                getContentResolver().delete(CONTENT_URI,null,null);
                Toast.makeText(this,"all pets deleted",Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection={
                petContract.petEntry._ID,
                petContract.petEntry.COLUMN_PET_NAME,
                petContract.petEntry.COLUMN_PET_BREED,
        };
        return new CursorLoader(this,
                CONTENT_URI,
                projection,
                null,
                null
                ,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPetCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
      mPetCursorAdapter.swapCursor(null);
    }
}