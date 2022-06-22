package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.pets.data.petContract;
import com.example.pets.data.petDbHelper;

public class EditorActivity extends AppCompatActivity {
    private petDbHelper editDb=new petDbHelper(EditorActivity.this);
    /** EditText field to enter the pet's name */
    public static EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    public static EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    public static EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;
    petContract petObject=new petContract();

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    public static int mGender = 0;
    public  int currentId=-2;
    Uri currentPetUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        Intent intent=getIntent();
         currentPetUri=intent.getData();
        if(currentPetUri==null){
            setTitle(R.string.add_pet_title);
        }else{
            setTitle(R.string.edit_pet_title);
            currentId=(Integer) getIntent().getSerializableExtra("currentPetId");
           editDb.EditTextSetter(currentId );
           Log.d("currentId", String.valueOf(currentId));
        }





        setupSpinner();
    }
    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = petContract.petEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = petContract.petEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = petContract.petEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    private void addPet(){

            ContentValues values = new ContentValues();
            values.put(petContract.petEntry.COLUMN_PET_NAME, mNameEditText.getText().toString().trim());
            values.put(petContract.petEntry.COLUMN_PET_BREED, mBreedEditText.getText().toString().trim());
            values.put(petContract.petEntry.COLUMN_PET_GENDER, mGender);
            values.put(petContract.petEntry.COLUMN_PET_WEIGHT, Integer.parseInt(mWeightEditText.getText().toString().trim()));

            if(currentId<0) {
                Uri newUri = getContentResolver().insert(petContract.petEntry.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                getContentResolver().update( getIntent().getData(),values,null,null);
            }

           //
    }


    public void deletePet(){
        getContentResolver().delete(getIntent().getData(),null,null);
        Toast.makeText(this,"pet deleted",Toast.LENGTH_SHORT).show();
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                addPet();
              //  Toast.makeText(this,"your pet is added to data base",Toast.LENGTH_LONG).show();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                deletePet();
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}