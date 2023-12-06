package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Models.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper
{
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "todo.db";
    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, status INTEGER, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    // ***************************** USERS *****************************

    public boolean checkEmailExists(String email)
    {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkEmailAndPasswordCorrect(String email, String password)
    {
        db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public boolean insertUser(String email, String password)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = db.insert("users", null, contentValues);
        return result != -1;
    }

    public int getUserId(String email)
    {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        else
        {
            return -1;
        }
    }

    public String getUserEmail(int user_id)
    {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(user_id)});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getString(1);
        }
        else
        {
            return "";
        }
    }

    // ***************************** TASKS *****************************

    public boolean insertTask(ToDoModel task)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("status", task.getStatus());
        contentValues.put("user_id", task.getUser_id());
        long result = db.insert("tasks", null, contentValues);
        return result != -1;
    }

    public List<ToDoModel> getAllTasks(int user_id)
    {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> tasks = new ArrayList<>();

        db.beginTransaction();

        try {
            cursor = db.query("tasks", null, "user_id = ?", new String[]{String.valueOf(user_id)}, null, null, null, null);
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        int status = cursor.getInt(2);
                        int user_id_ = cursor.getInt(3);
                        ToDoModel task = new ToDoModel(id, title, status, user_id_);
                        tasks.add(task);
                    }while(cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }

        return tasks;
    }

    public boolean updateTaskTitle(int id, String title)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        long result = db.update("tasks", contentValues, "ID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean updateTaskStatus(int id, int status)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        long result = db.update("tasks", contentValues, "ID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean deleteTask(int id)
    {
        db = this.getWritableDatabase();
        long result = db.delete("tasks", "ID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

}
