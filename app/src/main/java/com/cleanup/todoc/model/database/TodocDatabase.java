package com.cleanup.todoc.model.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.database.dao.ProjectDao;
import com.cleanup.todoc.model.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Room database that contains the Task and Project tables
 */
@Database(entities={Project.class, Task.class}, version=1, exportSchema=false)
public abstract class TodocDatabase extends RoomDatabase {

    /**
     * Singleton instance of the database
     */
    public static volatile TodocDatabase INSTANCE;

    /**
     * Detect if instrumentation test is running
     */
    private static AtomicBoolean isRunningTest;
    public static synchronized boolean isRunningTest() {
        // do some caching to avoid checking every time
        if (null == isRunningTest) {
            boolean istest;

            try {
                // androidx only
                Class.forName ("androidx.test.espresso.Espresso");
                istest = true;
            } catch (ClassNotFoundException e) {
                istest = false;
            }

            isRunningTest = new AtomicBoolean(istest);
        }

        return isRunningTest.get();
    }

    /**
     * Get the singleton instance of the database for a given context
     *
     * @param context the context
     * @return the singleton instance of the database for a given context
     */
    public static TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {

                    // Is instrumentation test running?
                    if( isRunningTest() ) {
                        // Use an in-memory database
                        INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                                        TodocDatabase.class)
                                // Allow main thread queries, just for testing
                                .allowMainThreadQueries()
                                // Do callback to pre-populate the database
                                .addCallback(prepopulateDatabase())
                                // Build the database
                                .build();
                    } else {
                        // Use a real database
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TodocDatabase.class,
                                // SQLite database file name stored in the app's private data directory
                                "TodocDatabase.db")

                        // Do callback to pre-populate the database
                        .addCallback(prepopulateDatabase())
                        .build();
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Pre-populate the database with the list of projects
     * @return the callback to pre-populate the database
     */
    private static Callback prepopulateDatabase() {
        return new Callback() {

            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                // Insert the list of projects in the database
                Project[] projects = Project.getAllProjects();
                for (Project project : projects) {
                    // Create a ContentValues object where column names are the keys,
                    // and Project's attributes are the values.
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", project.getId());
                    contentValues.put("name", project.getName());
                    contentValues.put("color", project.getColor());

                    // Do insert
                    // Conflict strategy is set to IGNORE: if the value is already in the database, nothing happens
                    db.insert("project", OnConflictStrategy.IGNORE, contentValues);
                }
            }
        };
    }

    /**
     * Get the DAO for the Project table
     * @return the DAO for the Project table
     */
    public abstract ProjectDao projectDao();

    /**
     * Get the DAO for the Task table
     * @return the DAO for the Task table
     */
    public abstract TaskDao taskDao();
}

