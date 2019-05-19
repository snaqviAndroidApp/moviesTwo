package nanodegree.dfw.perm.moviesTwo.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieEntries.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class FavoritemoviesDb extends RoomDatabase {       // made class Abstract and didn't import the methods that would be the case otherwise

    private static final String LOG_TAG = FavoritemoviesDb.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemovies";                  // different than what was given as default in MoviesEntries.java
    private static FavoritemoviesDb sInstance;

    public static FavoritemoviesDb getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "creating new DB instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoritemoviesDb.class, FavoritemoviesDb.DATABASE_NAME
                )
                        .allowMainThreadQueries()                   // Queries should be done on Separate Thread, it is JUST to validate Room-based-Db
                        .build();
            }
        }
        return sInstance;
    }

    public abstract DbFavoriteMoviesDao dbFavoriteMoviesDao();

}
