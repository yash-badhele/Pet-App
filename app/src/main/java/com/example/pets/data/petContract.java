package com.example.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.net.URI;

public final class petContract implements Serializable {

  static int petId=1;
  public void setPetId(int id){
    this.petId=id;
  }
  public  int getPetId(){
    return petId;
  }


    public static final class petEntry implements BaseColumns{
      public static final   Uri CONTENT_URI=Uri.parse("content://com.example.android.pets/"+petEntry.PATH_PET);

        public static final String TABLE_NAME="pets";
        public static final String CREATE_TABLE="CREATE TABLE ";
        public static final String CONTENT_AUTHORITY="com.example.android.pets";
        public static final String PATH_PET="pets";
        public static final String PATH_PET_ID="pets/#";

        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_PET_NAME="name";
        public static final String COLUMN_PET_BREED="breed";
        public static final String COLUMN_PET_GENDER="gender";
        public static final String COLUMN_PET_WEIGHT="weight";

        public static final int GENDER_MALE=1;
        public static final int GENDER_UNKNOWN=0;
        public static final int GENDER_FEMALE=2;

      public static boolean isValidGender(int gender) {
        if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
          return true;
        }
        return false;
      }
    }
}
