package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 * DAO for the Project entity
 */
@Dao
public interface ProjectDao {

    /**
     * Get all projects from database
     * @return all projects
     */
    @Query("SELECT * FROM project")
    LiveData<List<Project>> getProjects();

    /**
     * Insert a project in the database
     * @param projects the projects to insert
     */
    @Insert
    void insertProjects(Project... projects);
}