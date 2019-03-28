package nanodegree.dfw.perm.movieapp.data;

import android.os.Parcel;
import android.os.Parcelable;

//public final class DetailsData implements Parcelable {

public class DetailsData implements Parcelable {

    private String detailAct_original_title;        //    1.    original title
    private String detailAct_backdrop_path;         //    2.    movie image thumbnail --> moving it to actual Image, and Poster to thumbnail
    private String detailAct_overview;              //    3.    A plot synopsis (called overview in the api)
    private double detailAct_vote_average;          //    4.    user rating (called vote_average in the api)
    private String detailAct_release_date;          //    5.    release date

    public String getDetailAct_original_title() { return detailAct_original_title; }
    public String getDetailAct_backdrop_path() { return detailAct_backdrop_path; }
    public String getDetailAct_overview() { return detailAct_overview; }
    public double getDetailAct_vote_average() { return detailAct_vote_average; }
    public String getDetailAct_release_date() { return detailAct_release_date; }

    public DetailsData(String original_title, String backDrop_path, String overview, double vote_average, String release_date) {
        this.detailAct_original_title = original_title;
        this.detailAct_backdrop_path = backDrop_path;
        this.detailAct_overview = overview;
        this.detailAct_vote_average = vote_average;
        this.detailAct_release_date = release_date;
    }


    public DetailsData(Parcel in) {
        detailAct_original_title = in.readString();
        detailAct_backdrop_path = in.readString();
        detailAct_overview = in.readString();
        detailAct_vote_average = in.readDouble();
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
        dest.writeString(detailAct_release_date);
    }
}
