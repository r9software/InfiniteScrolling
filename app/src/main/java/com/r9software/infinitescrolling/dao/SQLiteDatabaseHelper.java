package com.r9software.infinitescrolling.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    // Singleton of the one and only SQLiteDatabaseHelper in the app
    // This is how we make sure app doesn't leave SQLite helper opened,
    // and open another one for the new context
    private static SQLiteDatabaseHelper instance;

    private static final String DATABASE_NAME = "database.db";

    // Database version is always 1 because there is no upgrade process (
    private static final int DATABASE_VERSION = 1;

    // IMPORTANT: this must be Application context, not Activity context which can be leaked/destroyed!
    private static Context appContext;

    public static void init(Context context) {
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.init()");
        appContext = context.getApplicationContext();
    }

    public static String getDatabasePath(boolean includeFileName) {
        String dbFolder = appContext.getApplicationInfo().dataDir + "/databases/";
        return dbFolder + (includeFileName ? DATABASE_NAME : "");
    }

    public static boolean isDatabaseCreated() {
        File f = new File(getDatabasePath(true));
        return f.exists();
    }

    public static void copyFromAssets() throws IOException {
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromAssets()");
        final int BUFFER_SIZE = 32000;
        InputStream in = new BufferedInputStream(appContext.getAssets().open(DATABASE_NAME));
        try {
            // make sure folder exists
            new File(getDatabasePath(false)).mkdirs();

            String outputFileName = getDatabasePath(true);
            Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromAssets: " + outputFileName);

            OutputStream out = new FileOutputStream(outputFileName);
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int totalLength = 0;
                int length;
                do {
                    length = in.read(buffer);
                    if (length > 0) {
                        totalLength += length;
                        out.write(buffer, 0, length);
                    }
                } while (length > 0);
                Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromAssets: " + totalLength + " bytes copied");
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
            }
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }
    }

    public static void copyFromFile(File source) throws IOException {
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromFile()");
        final int BUFFER_SIZE = 32000;
        InputStream in = new BufferedInputStream(new FileInputStream(source));
        try {
            // make sure folder exists
            new File(getDatabasePath(false)).mkdirs();

            String outputFileName = getDatabasePath(true);
            Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromFile: " + outputFileName);

            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFileName));
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int totalLength = 0;
                int length;
                do {
                    length = in.read(buffer);
                    if (length > 0) {
                        totalLength += length;
                        out.write(buffer, 0, length);
                    }
                } while (length > 0);
                Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.copyFromFile: " + totalLength + " bytes copied");
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
            }
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }
    }

    public static SQLiteDatabaseHelper getInstance() {
        if (instance == null) {
            if (appContext == null) {
                Log.e("DATABASE HELPER", "SQLiteDatabaseHelper.getInstance(): app context == NULL");
                return null;
            }
            instance = new SQLiteDatabaseHelper(appContext);
        }
        return instance;
    }

    public SQLiteDatabase open(boolean write) {
        try {
            if (!isDatabaseCreated()) {
                copyFromAssets();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int typeOfDBM;
        if (write)
            typeOfDBM = SQLiteDatabase.OPEN_READWRITE;
        else
            typeOfDBM = SQLiteDatabase.OPEN_READONLY;
        SQLiteDatabase database = SQLiteDatabase.openDatabase(getDatabasePath(true), null, typeOfDBM);

        return database;
    }

    /**
     * Provides an instance of a writable database using parents method checking if a database is
     * already created or copying from assets if not.
     *
     * @return SQLiteDatabase containing the latest update or a copy from assets when not created.
     */
    public SQLiteDatabase openWritable() {
        try {
            if (!isDatabaseCreated()) {
                copyFromAssets();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.getWritableDatabase();
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make call to static factory method getInstance() instead.
     */
    private SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper()");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // we don't support upgrading DB schema
        Log.d("DATABASE HELPER", "SQLiteDatabaseHelper.onUpgrade()");
    }
}