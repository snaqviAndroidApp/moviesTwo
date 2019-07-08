package nanodegree.dfw.perm.BackingApp.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieEntries.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MoviesDatabase extends RoomDatabase {       // made class Abstract and didn't import the methods that would be the case otherwise

    private static final String LOG_TAG = MoviesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "db_favorites";                  // different than what was given as default in MoviesEntries.java
    private static MoviesDatabase sInstance;
    public static MoviesDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "creating new DB instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class, MoviesDatabase.DATABASE_NAME)
//                        .allowMainThreadQueries()                         // Queries should be done on Separate Thread, it is JUST to validate Room-based-Db
                                                                            // that, then turned-off, after test showed a pass (that db is functioning)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoritesDao dbFavoriteMoviesDao();
}
