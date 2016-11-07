package com.r9software.infinitescrolling.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class AbstractBaseSqliteDao {

    protected interface DbQueryInterface {
        void onCursor(Cursor cursor);

    }
    public interface DbInsertInterface {
        void onInsert(long id);

    }
    protected void insert(String table,ContentValues val, String nullCollumnHack, DbInsertInterface insertInterface){
        SQLiteDatabase database = SQLiteDatabaseHelper.getInstance().open(true);
        if (database != null) {
            try {
                long result = database.insert(table,nullCollumnHack,val);
                if (result != 0) {
                        if (insertInterface != null) {
                            insertInterface.onInsert(result);
                        }
                }
            } catch (Exception e) {
                // most likely SQL syntax error: missing column, etc.
                Log.e(getClass().getSimpleName(), e.getMessage());
            } finally {
                database.close();
            }
        }

    }
    SQLiteDatabase database;
    protected void insertMultiple(String table,ContentValues val,int count, String nullCollumnHack, DbInsertInterface insertInterface){
       if(database==null)
        database = SQLiteDatabaseHelper.getInstance().open(true);
        if (database != null) {
            try {
                long result = database.insert(table,nullCollumnHack,val);
                if (result != 0) {
                    if (insertInterface != null) {
                        insertInterface.onInsert(result);
                    }
                }
            } catch (Exception e) {
                // most likely SQL syntax error: missing column, etc.
                Log.e(getClass().getSimpleName(), e.getMessage());
            } finally {
                if(count==1){
                database.close();
                Log.d(getClass().getSimpleName(),"Closing database");
                }
            }
        }

    }
    protected void update(String table, ContentValues values, String where, String[] args){
        SQLiteDatabase database = SQLiteDatabaseHelper.getInstance().open(true);
        if (database != null) {
            try {
                long result = database.update(table,values,where,args);
            } catch (Exception e) {
                // most likely SQL syntax error: missing column, etc.
                Log.e(getClass().getSimpleName(), e.getMessage());
            } finally {
                database.close();
            }
        }
    }
    protected void query(String sql, String[] args, DbQueryInterface queryInterface) {

        SQLiteDatabase database = SQLiteDatabaseHelper.getInstance().open(false);
        if (database != null) {
            try {
                Cursor result = database.rawQuery(sql, args);
                if (result != null) {
                    try {
                        if (queryInterface != null) {
                            queryInterface.onCursor(result);
                        }
                    } finally {
                        result.close();
                    }
                }
            } catch (Exception e) {

                // most likely SQL syntax error: missing column, etc.
                Log.e(getClass().getSimpleName(), e.getMessage());

            } finally {
                database.close();
            }
        }
    }

    public void query(StringBuffer sql, String[] args, DbQueryInterface queryInterface) {
        query(sql.toString(), args, queryInterface);
    }
    public void insert(StringBuffer table,ContentValues val, String nullCollumnHack, DbInsertInterface insertInterface){
        insert(table,val,nullCollumnHack,insertInterface);
    }


}
