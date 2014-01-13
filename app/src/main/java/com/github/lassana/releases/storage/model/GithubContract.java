package com.github.lassana.releases.storage.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author lassana
 * @since 1/13/14
 */
public final class GithubContract {

    public static final String AUTHORITY = "com.github.lassana.releases";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static interface RepositoriesColumns {
        String OWNER = "user_name";
        String REPOSITORY_NAME = "repository_name";
    }

    public static interface TagsColumns {
        String REPOSITORY_ID = "repository_id";
        String TAG_NAME = "tag_name";
        String ZIPBALL_URL = "zipball_url";
        String TARBALL_URL = "tarball_url";
        String COMMIT_SHA = "commit_sha";
        String COMMIT_URL = "commit_url";
    }

    public static class Repositories implements BaseColumns, RepositoriesColumns {
        public static final String CONTENT_PATH = "repositories";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
    }

    public static class Tags implements BaseColumns, TagsColumns {
        public static final String CONTENT_PATH = "tags";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
    }

}
