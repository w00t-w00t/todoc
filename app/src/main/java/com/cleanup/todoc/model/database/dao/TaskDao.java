package com.cleanup.todoc.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * DAO for the Task entity
 */
@Dao
public interface TaskDao {

    /**
     * Get all tasks from database
     * @return all tasks
     */
    @Query("SELECT * FROM task")
    LiveData<List<Task>> getTasks();

    /**
     * Insert a task in the database
     * @param task the task to insert
     */
    @Insert
    void insertTask(Task task);

    /**
     * Delete a task from the database
     * @param task the task to delete
     */
    @Delete
    void deleteTask(Task task);
}
