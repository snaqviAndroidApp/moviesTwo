package nanodegree.dfw.perm.moviesTwo.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DbFavoriteMoviesDao {

    @Query("SELECT * FROM DbFavorite ORDER BY bfavorite_room")
    List<MovieEntries> loadAllDbFavorite();

    @Insert
    void insertDbFavoriteMovies(MovieEntries movieEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDbFavoriteMovies(MovieEntries movieEntries);

    @Delete
    void deleteDbFavoriteMovies(MovieEntries movieEntries);

}
