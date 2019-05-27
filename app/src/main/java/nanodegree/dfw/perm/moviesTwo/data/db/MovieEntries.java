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
    private boolean bfavorite_room;
    @ColumnInfo(name = "updatedat")
    private Date updatedat;


    @Ignore
    public MovieEntries(boolean bfavorite_room, Date updatedat) {
        this.bfavorite_room = bfavorite_room;
        this.updatedat = updatedat;
    }

    public MovieEntries(int id_room, boolean bfavorite_room, Date updatedat) {  // use this to create new table-entry
        this.id_room = id_room;
        this.bfavorite_room = bfavorite_room;
        this.updatedat = updatedat;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public boolean isBfavorite_room() {
        return bfavorite_room;
    }

    public void setBfavorite_room(boolean bfavorite_room) {
        this.bfavorite_room = bfavorite_room;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

}
