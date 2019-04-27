package nanodegree.dfw.perm.moviesTwo.data;

import java.io.Serializable;

//import kotlin.jvm.internal.DoubleCompanionObject;

 class DummyPoints implements Serializable {

    private String mImageUrl;

    public DummyPoints(String mImageUrl, double mRating) {
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
