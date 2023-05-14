package com.cleanup.todoc.db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.db.utils.LiveDataTestUtil;
import com.cleanup.todoc.model.Project;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    @Rule
    // Rule to execute tasks synchronously
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    // Get reference to the ToDoc database
    private TodocDatabase database;
    // Get the list of static initial projects from the model Project class
    private final Project[] projects = Project.getAllProjects();

    @Before
    public void initDatabase() {
        // Create an in-memory blank version of the database
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                        TodocDatabase.class)
        // Allow main thread queries, just for testing
        .allowMainThreadQueries()
        // Build the database
        .build();
    }

    @After
    public void closeDatabase() {
        // Close the database after the tests have been run
        this.database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        // Get the list of projects
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());
        // Check that the list is empty at first
        assertTrue(projects.isEmpty());
        // Insert the projects in the database
        this.database.projectDao().insertProjects(this.projects);
        // Get the list of projects from the database
        projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());

        // Check that the list contains the 3 projects
        assertEquals(projects.get(0).getName(), this.projects[0].getName());
        assertEquals(projects.get(0).getId(), this.projects[0].getId());
        assertEquals(projects.get(0).getColor(), this.projects[0].getColor());

        assertEquals(projects.get(1).getName(), this.projects[1].getName());
        assertEquals(projects.get(1).getId(), this.projects[1].getId());
        assertEquals(projects.get(1).getColor(), this.projects[1].getColor());

        assertEquals(projects.get(2).getName(), this.projects[2].getName());
        assertEquals(projects.get(2).getId(), this.projects[2].getId());
        assertEquals(projects.get(2).getColor(), this.projects[2].getColor());
    }


}
