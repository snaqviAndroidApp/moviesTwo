package nanodegree.dfw.perm.moviesTwo.data.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "favorites")
public class MovieEntries {

    @PrimaryKey(autoGenerate = true)            //set autoGenerate to true
    private int id_room;
    private String bfavorite_room;
    @ColumnInfo(name = "updatedAt")
    private Date updatedAt;


    @Ignore
    public MovieEntries(String bfavorite_room, Date updatedat) {
        this.bfavorite_room = bfavorite_room;
        this.updatedAt = updatedat;
    }

    public MovieEntries(int id_room, String bfavorite_room, Date updatedAt) {
        this.id_room = id_room;
        this.bfavorite_room = bfavorite_room;
        this.updatedAt = updatedAt;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public String getBfavorite_room() {
        return bfavorite_room;
    }

    public void setBfavorite_room(String bfavorite_room) {
        this.bfavorite_room = bfavorite_room;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
