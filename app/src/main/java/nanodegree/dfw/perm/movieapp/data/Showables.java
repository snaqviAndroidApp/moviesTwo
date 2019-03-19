package nanodegree.dfw.perm.movieapp.data;

import java.io.Serializable;
import java.io.SerializablePermission;

//import kotlin.jvm.internal.DoubleCompanionObject;

public class Showables implements Serializable {

    private String mImageUrl;
    private double mRating;

    public Showables(String mImageUrl, double mRating) {
        this.mImageUrl = mImageUrl;
        this.mRating = mRating;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
