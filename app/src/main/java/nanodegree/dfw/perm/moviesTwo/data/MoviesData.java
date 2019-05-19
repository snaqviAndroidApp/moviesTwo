package nanodegree.dfw.perm.moviesTwo.data;

import java.util.ArrayList;

import nanodegree.dfw.perm.moviesTwo.utilities.NetworkUtils;

public class MoviesData {

    private String poster_path;
    private String backDrop_path;
    private String original_title;
    private String overview;
    private String release_date;
    private double vote_average;
    private double popularity;
    private String failure_status;

    // added for State-2
    private ArrayList<String> movie_reviews;
    private ArrayList<String> movie_id;

    public ArrayList<String> getMovie_reviews() { return movie_reviews; }
    public void setMovie_reviews(ArrayList movie_reviews) { this.movie_reviews = movie_reviews; }

    public ArrayList<String> getMovie_id() { return movie_id; }
    public void setMovie_id(ArrayList<String> movie_id) { this.movie_id = movie_id; }

    public MoviesData() {

    }

    public MoviesData(String poster_path, String backDrop_pathC, String original_title, String overview, String release_date, double vote_average,
                      double popularity, String failure_status, ArrayList movie_reviews, ArrayList movie_id) {
        this.poster_path = poster_path;
        this.backDrop_path = backDrop_pathC;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.failure_status = failure_status;
        this.movie_reviews = movie_reviews;
        this.movie_id = movie_id;
    }

    /**
     * sets the poster location
     *
     */
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public void setBackDrop_path(String backDrop_path) { this.backDrop_path = backDrop_path; }
    public void setOriginal_title(String original_title) { this.original_title = original_title; }
    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    public void setFailure_status(String failure_status) { this.failure_status = failure_status; }

    /**
     * retrieves the posters available for introduction
     * @return
     *
     */
    public String getOriginal_title() {
        return original_title;
    }
    public String getOverview() {
        return overview;
    }
    public double getVote_average() {
        return vote_average;
    }
    public double getPopularity() {
        return popularity;
    }
    public String getFailure_status() { return failure_status; }
    public String getRelease_date() {
        return release_date;
    }

    /**
     * fetches the posters available for introduction
     * @return poster_path for Root Views
     *
     */
    private String getPoster_path() {
        return poster_path;
    }
    public String getBackDrop_path() { return backDrop_path; }

    @Override
    public String toString() {
        return "MoviesData{" +
                "poster_path='" + poster_path + '\'' +
                ", backDrop_path='" + backDrop_path + '\'' +
                ", original_title='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", vote_average=" + vote_average +
                ", popularity=" + popularity +
                ", failure_status='" + failure_status + '\'' +
                ", movie_id=" + movie_id +
                ", movie_reviews='" + movie_reviews + '\'' +
                '}';
    }

    public String getPoster_builtPath() {
        return NetworkUtils.buildPosterUrl(getPoster_path()).toString();
    }

    public String getbackDropImage_bulitPath() {
        return NetworkUtils.buildPosterUrl(getBackDrop_path()).toString();
    }
}
