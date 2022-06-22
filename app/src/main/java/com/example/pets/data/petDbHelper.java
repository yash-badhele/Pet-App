package com.example.pets.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.pets.EditorActivity;
import com.example.pets.R;

public class petDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="shelter.db";
    public petDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_Create_pets_Table= petContract.petEntry.CREATE_TABLE + petContract.petEntry.TABLE_NAME+"("
                + petContract.petEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + petContract.petEntry.COLUMN_PET_NAME +" TEXT NOT NULL,"
                + petContract.petEntry.COLUMN_PET_BREED +" TEXT,"
                + petContract.petEntry.COLUMN_PET_GENDER +" INTEGER NOT NULL,"
                + petContract.petEntry.COLUMN_PET_WEIGHT +" INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_Create_pets_Table);

    }
    public void EditTextSetter(int a){
        SQLiteDatabase db=this.getReadableDatabase();
        String select="SELECT * FROM "+ petContract.petEntry.TABLE_NAME;
        Cursor cursor=db.rawQuery(select,null);
        cursor.moveToPosition(a);
        String s=cursor.getString(1);
        EditorActivity.mNameEditText.setText(s);
        EditorActivity.mBreedEditText.setText(cursor.getString(2));
        EditorActivity.mWeightEditText.setText(cursor.getString(4));
        if((Integer.parseInt( cursor.getString(3)))==1) {
            EditorActivity.mGender = petContract.petEntry.GENDER_MALE;
        }else if((Integer.parseInt( cursor.getString(3)))==2){
            EditorActivity.mGender= petContract.petEntry.GENDER_FEMALE;
        }



        Log.d("cursorString",s);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
