package com.cleanup.todoc.db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.model.database.TodocDatabase;
import com.cleanup.todoc.db.utils.LiveDataTestUtil;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    @Rule
    // Rule to execute tasks synchronously
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    // Get reference to the ToDoc database
    private TodocDatabase database;
    // Get the list of static initial projects from the model Project class
    private Project[] projects = Project.getAllProjects();

    // Create static Task models
    private Task task1 = new Task(projects[0].getId(), "Tache 1", new Date().getTime());
    private Task task2 = new Task(projects[0].getId(), "Tache 2", new Date().getTime());
    private Task task3 = new Task(projects[0].getId(), "Tache 3", new Date().getTime());
    private Task task4 = new Task(projects[2].getId(), "Tache 4", new Date().getTime());
    private Task task5 = new Task(projects[2].getId(), "Tache 5", new Date().getTime());

    @Before
    public void initDatabase() throws InterruptedException {
        // Create an in-memory blank version of the database
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                        TodocDatabase.class)
        // Allow main thread queries, just for testing
        .allowMainThreadQueries()
        // Build the database
        .build();

        // Insert the projects in the database
        this.database.projectDao().insertProjects(this.projects);

        // Get the list of projects from the database
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Check that the list is empty at first
        assertTrue(tasks.isEmpty());
    }

    @After
    public void closeDatabase() {
        // Close the database after the tests have been run
        this.database.close();
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        // Get the list of projects from the database
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());

        // Insert the tasks in the database
        this.database.taskDao().insertTask(this.task1);
        this.database.taskDao().insertTask(this.task2);

        // Get the list of tasks from the database
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Check that the list contains 2 tasks
        assertEquals(2, tasks.size());

        // Add the remaining tasks to the database
        this.database.taskDao().insertTask(this.task3);
        this.database.taskDao().insertTask(this.task4);
        this.database.taskDao().insertTask(this.task5);

        // Get the list of tasks from the database
        tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Check that the list contains 5 tasks
        assertEquals(5, tasks.size());

        // Check that the tasks are associated with the right projects
        assertEquals(projects.get(0).getId(), tasks.get(0).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(1).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(2).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(3).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(4).getProjectId());

        // Check if the tasks attributes from the static array and inside the database are the same
        assertEquals(task1.getName(), tasks.get(0).getName());
        assertEquals(task3.getName(), tasks.get(2).getName());
        assertEquals(task5.getName(), tasks.get(4).getName());

        // Check if the tasks creation timestamps from the static array and inside the database are the same
        assertEquals(task1.getCreationTimestamp(), tasks.get(0).getCreationTimestamp());
        assertEquals(task3.getCreationTimestamp(), tasks.get(2).getCreationTimestamp());
        assertEquals(task5.getCreationTimestamp(), tasks.get(4).getCreationTimestamp());
    }

    @Test
    public void deleteAndGetTask() throws InterruptedException {

        // Insert the tasks in the database
        this.database.taskDao().insertTask(this.task1);
        this.database.taskDao().insertTask(this.task2);
        this.database.taskDao().insertTask(this.task3);
        this.database.taskDao().insertTask(this.task4);
        this.database.taskDao().insertTask(this.task5);

        // Get the list of tasks from the database
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Check that the list contains 5 tasks
        assertEquals(5, tasks.size());

        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());

        // Delete the second task from the database
        this.database.taskDao().deleteTask(tasks.get(1));

        // Get the list of tasks from the database again
        tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Check that the list contains 4 tasks (one task has been deleted)
        assertEquals(4, tasks.size());

        // Check that the second task has been deleted (the name matches the third task)
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }
}
