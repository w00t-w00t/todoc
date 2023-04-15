package com.cleanup.todoc.database.injections;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import java.util.concurrent.Executor;

/**
 * Factory for ViewModels
 * Essential for the ViewModel to be able to access the data source
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    /**
     * The Project data source
     */
    private final ProjectDataRepository mProjectDataSource;

    /**
     * The Task data source
     */
    private final TaskDataRepository mTaskDataSource;

    /**
     * The Executor
     */
    private final Executor mExecutor;

    /**
     * Constructor
     * @param projectDataSource the project data source
     * @param taskDataSource the task data source
     * @param executor the executor
     */
    public ViewModelFactory(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        mProjectDataSource = projectDataSource;
        mTaskDataSource = taskDataSource;
        mExecutor = executor;
    }

    /**
     * Create a new instance of the given {@code Class}.
     * @param modelClass a {@code Class} whose instance is requested
     * @param <T> The type parameter for the ViewModel.
     * @return a newly created ViewModel
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // Create a ViewModel for the TaskViewModel class
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(mProjectDataSource, mTaskDataSource, mExecutor);
        }

        // If the ViewModel class is unknown, throw an exception
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
