package kdrosado.trendyart;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Help from this resource: https://github.com/udacity/ud851-Exercises/blob/student/Lesson09b-ToDo-List-AAC/T09b.04-Exercise-Executors/app/src/main/java/com/example/android/todolist/AppExecutors.java
public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors INSTANCE;
    private final Executor mDiskIO;
    private final Executor mNetworkIO;
    private final Executor mMainThread;

    // Make this class a singleton
    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        mDiskIO = diskIO;
        mNetworkIO = networkIO;
        mMainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(5),
                        new MainThreadExecutor());
            }
        }
        return INSTANCE;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }


    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
