package com.cleanup.todoc.db.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// How to test LiveData : https://stackoverflow.com/a/44271247
public class LiveDataTestUtil {
    // This method is used to get the value of a LiveData object.
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        // Create an array of objects to store the data
        final Object[] data = new Object[1];
        // Create a CountDownLatch object to wait for the data to be set
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an Observer object to observe the LiveData object
        Observer<T> observer = new Observer<T>() {
            // This method is called when the LiveData object is changed
            @Override
            public void onChanged(@Nullable T o) {
                // Set the data in the array
                data[0] = o;
                // Count down the latch to 0
                latch.countDown();
                // Remove the observer
                liveData.removeObserver(this);
            }
        };
        // Observe the LiveData object
        liveData.observeForever(observer);
        // Wait for the data to be set
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }
}

