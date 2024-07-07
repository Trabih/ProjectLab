package com.example.projectlab1;

import android.app.Application;
import androidx.room.Room;

public class ProjectLab1Application extends Application {

    private static ProjectLab1Application instance;
    private AppDatabase database;

    public static ProjectLab1Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "projectlab1_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
