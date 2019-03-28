package nanodegree.dfw.perm.movieapp.data;

import java.io.Serializable;
import java.io.SerializablePermission;

//import kotlin.jvm.internal.DoubleCompanionObject;

 class Showables implements Serializable {

    private String mImageUrl;

    public Showables(String mImageUrl, double mRating) {
        this.mImageUrl = mImageUrl;
        double mRating1 = mRating;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
