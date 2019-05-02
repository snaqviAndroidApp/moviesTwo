package nanodegree.dfw.perm.moviesTwo.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "moviesfavorite")
public class MovieEntries {

    @PrimaryKey(autoGenerate = true)            //set autoGenerate to true
    private int id_room;
    private boolean bfavorite_room;
    private String review_room;
    private String poster_room;

    @Ignore
    public MovieEntries(boolean bfavorite_room, String review_room, String poster_room) {
        this.bfavorite_room = bfavorite_room;
        this.review_room = review_room;
        this.poster_room = poster_room;
    }

    public MovieEntries(int id_, boolean bfavorite_room, String review_room, String poster_room) {
        this.id_room = id_;
        this.bfavorite_room = bfavorite_room;
        this.review_room = review_room;
        this.poster_room = poster_room;
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

    public String getReview_room() {
        return review_room;
    }

    public void setReview_room(String review_room) {
        this.review_room = review_room;
    }

    public String getPoster_room() {
        return poster_room;
    }

    public void setPoster_room(String poster_room) {
        this.poster_room = poster_room;
    }

    @Override
    public String toString() {
        return "MovieEntries{" +
                "id_room=" + id_room +
                ", bfavorite_room=" + bfavorite_room +
                ", review_room='" + review_room + '\'' +
                ", poster_room='" + poster_room + '\'' +
                '}';
    }


}
