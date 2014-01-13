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

import com.github.lassana.releases.storage.model.GithubContract;

/**
 * @author lassana
 * @since 1/13/14
 */
@SuppressWarnings("ConstantConditions")
public class GithubProvider extends ContentProvider {

    private static final String TAG = "GithubProvider";

    private static final String TABLE_REPOSITORIES = "repositories";
    private static final String TABLE_TAGS = "tags";

    private static final String DB_NAME = "github.db";
    private static final int DB_VERSION = 1;

    private static final int PATH_ROOT = 0;
    private static final int PATH_REPOSITORIES = 1;
    private static final int PATH_TAGS = 2;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(PATH_ROOT);
        sUriMatcher.addURI(GithubContract.AUTHORITY, GithubContract.Repositories.CONTENT_PATH, PATH_REPOSITORIES);
        sUriMatcher.addURI(GithubContract.AUTHORITY, GithubContract.Tags.CONTENT_PATH, PATH_TAGS);
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
            String createRepositories =
                    "create table " + TABLE_REPOSITORIES + " (" +
                            GithubContract.Repositories._ID + " integer primary key autoincrement, " +
                            GithubContract.Repositories.USER_NAME + " text, " +
                            GithubContract.Repositories.REPOSITORY_NAME + " text" +
                            ")";
            String createTags = "create table " + TABLE_TAGS + " (" +
                    GithubContract.Tags._ID + " integer primary key autoincrement, " +
                    GithubContract.Tags.REPOSITORY_ID + " integer, " +
                    GithubContract.Tags.TAG_NAME + " text, " +
                    GithubContract.Tags.ZIPBALL_URL + " text, " +
                    GithubContract.Tags.TARBALL_URL + " text" +
                    ")";
            String[] sqls = {createRepositories, createTags};
            for(String sql : sqls ) {
                db.execSQL(sql);
            }
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
                cursor.setNotificationUri(getContext().getContentResolver(), GithubContract.Repositories.CONTENT_URI);
                return cursor;
            }
            case PATH_TAGS: {
                Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_TAGS, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), GithubContract.Tags.CONTENT_URI);
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
                return GithubContract.Repositories.CONTENT_TYPE;
            case PATH_TAGS:
                return GithubContract.Tags.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES: {
                long id = mDatabaseHelper.getWritableDatabase().insert(TABLE_REPOSITORIES, null, values);
                getContext().getContentResolver().notifyChange(GithubContract.Repositories.CONTENT_URI, null);
                return ContentUris.withAppendedId(uri, id);
            }
            case PATH_TAGS: {
                long id = mDatabaseHelper.getWritableDatabase().insert(TABLE_TAGS, null, values);
                getContext().getContentResolver().notifyChange(GithubContract.Tags.CONTENT_URI, null);
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
            case PATH_TAGS:
                return mDatabaseHelper.getWritableDatabase().delete(TABLE_TAGS, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PATH_REPOSITORIES:
                return mDatabaseHelper.getWritableDatabase().update(TABLE_REPOSITORIES, values, selection, selectionArgs);
            case PATH_TAGS:
                return mDatabaseHelper.getWritableDatabase().update(TABLE_TAGS, values, selection, selectionArgs);
            default:
                return 0;
        }
    }

}
