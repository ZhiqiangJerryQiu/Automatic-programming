package aad.app.e10.providers;

import java.util.HashMap;

import aad.app.e10.BooksDatabaseHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class BooksContentProvider extends ContentProvider {

    public static final String TAG = BooksContentProvider.class.getSimpleName();

    public static final String AUTHORITY = "aad.app.e11.providers.BooksContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/books");

    private static final int BOOK = 1;
    private static final int BOOK_ID = 2;

    BooksDatabaseHelper mSQLHelper;

    private static final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static HashMap<String, String> tasksProjectionMap;

    static {
        mURIMatcher.addURI(AUTHORITY, "books", BOOK);
        mURIMatcher.addURI(AUTHORITY, "books/#", BOOK_ID);

        tasksProjectionMap = new HashMap<String, String>();
        tasksProjectionMap.put("_id", "_id");
        tasksProjectionMap.put("name", "name");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        int count;
        switch (mURIMatcher.match(uri)) {
        case BOOK:
            count = db.delete("books", selection, selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {

        switch (mURIMatcher.match(uri)) {
        case BOOK:
            return "vnd.android.cursor.dir/vnd.aad.app.e11.books";

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv) {

        if (mURIMatcher.match(uri) != BOOK) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        long rowID = db.insert("books", null, cv);
        if (rowID > 0) {

            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        else {
            Log.e(TAG, "insert() Error inserting task");
        }

        return null;
    }

    @Override
    public boolean onCreate() {

        mSQLHelper = new BooksDatabaseHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (mURIMatcher.match(uri)) {
        case BOOK:
            qb.setTables("books");
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mSQLHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mSQLHelper.getWritableDatabase();

        int count;
        switch (mURIMatcher.match(uri)) {
        case BOOK:
            count = db.update("books", values, selection, selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

}
