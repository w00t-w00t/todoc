package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Dependency injector to get instances of view models.
 */
public class Injection {

    /**
     * Creates an instance of {@link ProjectDataRepository} based on the database Project DAO.
     * @param context the context
     * @return the instance of {@link ProjectDataRepository}
     */
    private static ProjectDataRepository provideProjectDataSource(Context context) {
        // Gets instance of database via its singleton
        TodocDatabase database = TodocDatabase.getInstance(context);

        // Gets DAO from database
        // Returns new instance of ProjectDataRepository with its DAO
        return new ProjectDataRepository(database.projectDao());
    }

    /**
     * Creates an instance of {@link TaskDataRepository} based on the database Task DAO.
     * @param context the context
     * @return the instance of {@link TaskDataRepository}
     */
    private static TaskDataRepository provideTaskDataSource(Context context) {
        // Gets instance of database via its singleton
        TodocDatabase database = TodocDatabase.getInstance(context);

        // Gets DAO from database
        // Returns new instance of ProjectDataRepository with its DAO
        return new TaskDataRepository(database.taskDao());
    }

    /**
     * Creates an instance of {@link Executor}
     * @return the instance of {@link Executor}
     */
    private static Executor provideExecutor() {
        // Returns new instance of single Thread Executor
        return Executors.newSingleThreadExecutor();
    }

    /**
     * Provides the {@link ViewModelFactory} so that the model can access the data sources.
     * @param context the context
     * @return the {@link ViewModelFactory}
     */
    public static ViewModelFactory provideViewModelFactory(Context context) {
        // Gets instances of ProjectDataRepository and TaskDataRepository
        ProjectDataRepository projectDataSource = provideProjectDataSource(context);
        TaskDataRepository taskDataSource = provideTaskDataSource(context);

        // Gets instance of Executor
        Executor executor = provideExecutor();

        // Returns new instance of ViewModelFactory with ProjectDataRepository, TaskDataRepository and Executor
        return new ViewModelFactory(projectDataSource, taskDataSource, executor);
    }
}
