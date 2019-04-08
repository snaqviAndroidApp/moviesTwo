package nanodegree.dfw.perm.movieapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public final class DetailsData implements Parcelable {

    private String detailAct_original_title;
    private String detailAct_backdrop_path;
    private String detailAct_overview;
    private double detailAct_vote_average;
    private double detailAct_popularity;
    private String detailAct_release_date;


    protected DetailsData(Parcel in) {
        detailAct_original_title = in.readString();
        detailAct_backdrop_path = in.readString();
        detailAct_overview = in.readString();
        detailAct_vote_average = in.readDouble();
        detailAct_popularity = in.readDouble();
        detailAct_release_date = in.readString();
    }

    public static final Creator<DetailsData> CREATOR = new Creator<DetailsData>() {
        @Override
        public DetailsData createFromParcel(Parcel in) {
            return new DetailsData(in);
        }

        @Override
        public DetailsData[] newArray(int size) {
            return new DetailsData[size];
        }
    };

    public String getDetailAct_original_title() {
        return detailAct_original_title;
    }
    public String getDetailAct_backdrop_path() {
        return detailAct_backdrop_path;
    }
    public String getDetailAct_overview() {
        return detailAct_overview;
    }
    public double getDetailAct_vote_average() {
        return detailAct_vote_average;
    }
    public double getDetailAct_popularity() {
        return detailAct_popularity;
    }
    public String getDetailAct_release_date() {
        return detailAct_release_date;
    }

    public DetailsData(String detailAct_original_title, String detailAct_backdrop_path, String detailAct_overview, double detailAct_vote_average, double detailAct_popularity, String detailAct_release_date) {
        this.detailAct_original_title = detailAct_original_title;
        this.detailAct_backdrop_path = detailAct_backdrop_path;
        this.detailAct_overview = detailAct_overview;
        this.detailAct_vote_average = detailAct_vote_average;
        this.detailAct_popularity = detailAct_popularity;
        this.detailAct_release_date = detailAct_release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(detailAct_original_title);
        dest.writeString(detailAct_backdrop_path);
        dest.writeString(detailAct_overview);
        dest.writeDouble(detailAct_vote_average);
        dest.writeDouble(detailAct_popularity);
        dest.writeString(detailAct_release_date);
    }
}
