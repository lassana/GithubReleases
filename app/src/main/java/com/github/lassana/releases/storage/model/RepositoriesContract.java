package com.github.lassana.releases.storage.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author lassana
 * @since 1/13/14
 */
public final class RepositoriesContract {

    public static final String AUTHORITY = "com.github.lassana.releases";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static interface RepositoriesColumns {
        String USER_NAME = "user_name";
        String REPOSITORY_NAME = "repository_name";
    }

    public static class Repositories implements BaseColumns, RepositoriesColumns {
        public static final String CONTENT_PATH = "repositories";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
    }

}
