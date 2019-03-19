package nanodegree.dfw.perm.movieapp.data;

public class POJO_MovieData {

    private String poster_path;
    private String original_title;
    private String overview;
    private String release_date;
    private double vote_average;
    private double popularity;
    private String failure_status;
    private String title;                    // to be used later, in case

    public POJO_MovieData() {

    }

    public POJO_MovieData(String poster_path, String original_title, String overview, String release_date, double vote_average, double popularity, String failure_status) {
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.failure_status = failure_status;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "POJO_MovieData{" +
                "poster_path='" + poster_path + '\'' +
                ", original_title='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", vote_average=" + vote_average +
                ", popularity=" + popularity +
                ", failure_status='" + failure_status + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
