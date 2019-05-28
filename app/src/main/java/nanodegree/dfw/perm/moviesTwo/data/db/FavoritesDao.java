package nanodegree.dfw.perm.moviesTwo.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoritesDao {

//    @Query("SELECT * FROM favorites ORDER BY bfavorite_room")
    @Query("SELECT * FROM favorites")
    List<MovieEntries> loadAllDbFavorite();

    @Insert
    void insertFavorites(MovieEntries movieEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorites(MovieEntries movieEntries);

    @Delete
    void deleteFavorites(MovieEntries movieEntries);

}
