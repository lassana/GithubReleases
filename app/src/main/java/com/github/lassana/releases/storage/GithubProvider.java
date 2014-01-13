package com.github.lassana.releases.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.github.lassana.releases.storage.model.RepositoriesContract;

/**
 * @author lassana
 * @since 1/13/14
 */
@SuppressWarnings("ConstantConditions")
public class GithubProvider extends ContentProvider {

    private static final String TAG = "GithubProvider";

    private static final String TABLE_REPOSITORIES = "repositories";

    private static final String DB_NAME = "github.db";
    private static final int DB_VERSION = 1;

    private static final int PATH_ROOT = 0;
    private static final int PATH_REPOSITORIES = 1;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(PATH_ROOT);
        sUriMatcher.addURI(RepositoriesContract.AUTHORITY, RepositoriesContract.Repositories.CONTENT_PATH, PATH_REPOSITORIES);
    }

    private DatabaseHelper mDatabaseHelper;

    /**
     * @author lassana
     * @since 1/13/14
     */
    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql =
                    "create table " + TABLE_REPOSITORIES + " (" +
                            RepositoriesContract.Repositories._ID + " integer primary key autoincrement, " +
                            RepositoriesContract.Repositories.USER_NAME + " text, " +
                            RepositoriesContract.Repositories.REPOSITORY_NAME + " text" +
                            ")";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext(), DB_NAME, null, DB_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES: {
                Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_REPOSITORIES, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), RepositoriesContract.Repositories.CONTENT_URI);
                return cursor;
            }
            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES:
                return RepositoriesContract.Repositories.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES: {
                long id = mDatabaseHelper.getWritableDatabase().insert(TABLE_REPOSITORIES, null, values);
                getContext().getContentResolver().notifyChange(RepositoriesContract.Repositories.CONTENT_URI, null);
                return ContentUris.withAppendedId(uri, id);
            }
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES:
                return mDatabaseHelper.getWritableDatabase().delete(TABLE_REPOSITORIES, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES:
                return mDatabaseHelper.getWritableDatabase().update(TABLE_REPOSITORIES, values, selection, selectionArgs);
            default:
                return 0;
        }
    }

}
