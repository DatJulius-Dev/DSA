import java.util.Date;

public class Rating {
    private int userId;
    private int movieId;
    private int rating;
    private long timeskip;

    public Rating (int userId, int movieId, int rating, long timeskip){
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timeskip = timeskip;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getTimeskip() {
        return timeskip;
    }

    public void setTimeskip(long timeskip) {
        this.timeskip = timeskip;
    }

    public String toString(){
        return "Rating[" + "userId: " + userId + ", movieId: " + movieId + ", rating: " + rating + ", timeskip: " + new Date(timeskip) + "]";
    }
}
