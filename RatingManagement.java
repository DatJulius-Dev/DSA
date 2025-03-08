import java.util.*;
import java.io.*;

public class RatingManagement {
    private ArrayList<Rating> ratings;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;

    // @Requirement 1
    public RatingManagement(String moviePath, String ratingPath, String userPath) {
        this.movies = loadMovies(moviePath);
        this.users = loadUsers(userPath);
        this.ratings = loadEdgeList(ratingPath);
    }

    private ArrayList<Rating> loadEdgeList(String ratingPath) {
        ArrayList<Rating> result = new ArrayList<>();
        try(Scanner sc = new Scanner (new File(ratingPath))){
            sc.nextLine();
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] parts = line.split(",");
                int userId = Integer.parseInt(parts[0]);
                int movieId = Integer.parseInt(parts[1]);
                int rating = Integer.parseInt(parts[2]);
                long timskip = Long.parseLong(parts[3]);
                Rating rat = new Rating(userId, movieId, rating, timskip);
                result.add(rat);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<Movie> loadMovies(String moviePath) {
        ArrayList<Movie> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(moviePath))){
            sc.nextLine();
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] parts = line.split(",");
                int movieId = Integer.parseInt(parts[0]);
                String title  = parts[1];
                String[] genresArray = parts[2].split("-");
                ArrayList<String> genres = new ArrayList<>();
                for(String genre : genresArray){
                        genres.add(genre);
                }
                Movie movie = new Movie(movieId, title, genres);
                result.add(movie);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<User> loadUsers(String userPath) {
        ArrayList<User> result = new ArrayList<>();
        try (Scanner sc = new Scanner (new File(userPath))){
            sc.nextLine();
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                String[] parts = line.split(",");
                int userId = Integer.parseInt(parts[0]);
                String gender = parts[1];
                int age = Integer.parseInt(parts[2]);
                String occupation = parts[3];
                String zipcode = parts[4];
                User user = new User(userId, gender, age, occupation, zipcode);
                result.add(user);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Rating> getRating() {
        return ratings;
    }

    // @Requirement 2
    public ArrayList<Movie> findMoviesByNameAndMatchRating(int userId, int rating) {
        ArrayList<Movie> result = new ArrayList<>();
        ArrayList<Rating> ratingOfUser = new ArrayList<>();
        for (Rating r : ratings){
            if(r.getUserId() == userId && r.getRating() >= rating){
                ratingOfUser.add(r);
            }
        }
        for (Rating userRating : ratingOfUser){
            for (Movie m : movies){
                if(m.getId() == userRating.getMovieId()){
                    result.add(m);
                }
            }
        }

        Collections.sort(result, Comparator.comparing(Movie::getName));
        return result;
    }

    // Requirement 3
    public ArrayList<User> findUsersHavingSameRatingWithUser(int userId, int movieId) {
        ArrayList<User> result = new ArrayList<>();
        int userRating = -1;
        for (Rating r : ratings){
            if(r.getMovieId() == movieId){
                if (r.getUserId() == userId){
                    userRating = r.getRating();
                } else if (r.getRating() == userRating){
                    User user = getUserFromId(r.getUserId());
                    if(user != null){
                        result.add(user);
                    }
                }
            }
        }
        return result;
    }
    private User getUserFromId(int userId){
        for (User user: users){
            if(user.getId() == userId){
                return user;
            }
        }
        return null;
    }

    // Requirement 4
    public ArrayList<String> findMoviesNameHavingSameReputation() {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<HashSet<Integer>> movieLoves = new ArrayList<>();
        for (int i = 0; i<= getMaxMovieId(); i++){
            movieLoves.add(new HashSet<>());
        }
        for (Rating r: ratings){
            if(r.getRating() > 3){
                movieLoves.get(r.getMovieId()).add(r.getUserId());
            }
        }

        for (Movie m : movies){
            HashSet<Integer> usersLiked = movieLoves.get(m.getId());
            if(usersLiked != null && usersLiked.size() >= 2){
                result.add(m.getName());
            }
        }

        Collections.sort(result);
        return result;
    }

    private int getMaxMovieId(){
        int maxId = -1;
        for (Movie m : movies){
            if (m.getId() > maxId){
                maxId = m.getId();
            }
        }
        return maxId;
    }

    // @Requirement 5
    public ArrayList<String> findMoviesMatchOccupationAndGender(String occupation, String gender, int k, int rating) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> listNameOfFilms = new ArrayList<>();
        
        for(User user:users){
            if(user.getOccupation().equals(occupation) && user.getGender().equals(gender)){
                for(Rating r : ratings){
                    if(r.getRating() == rating && user.getId() == r.getUserId()){
                        int movieId = r.getMovieId();
                        Movie movie = findMovieFromId(movieId);
                        if(movie != null){
                            String movieName = movie.getName();
                            if(!listNameOfFilms.contains(movieName)){
                                listNameOfFilms.add(movieName);
                            }
                        }
                    }
                }
            }
        }

        Collections.sort(listNameOfFilms);

        if (k <= listNameOfFilms.size()){
            result = new ArrayList<>(listNameOfFilms.subList(0,k));
        } else {
            return result;
        }
        return result;
    }

    private Movie findMovieFromId(int movieId){
        for(Movie movie : movies){
            if(movie.getId() == movieId){
                return movie;
            }
        }
        return null;
    }

    // @Requirement 6
    public ArrayList<String> findMoviesByOccupationAndLessThanRating(String occupation, int k, int rating) {
        ArrayList<String> result = new ArrayList<>();
        
        for (User user : users){
            if(user.getOccupation().equals(occupation)){
                for(Rating userRating: ratings){
                    if(userRating.getUserId() == user.getId() && userRating.getRating() < rating){
                        int movieId = userRating.getMovieId();
                        Movie movie = findMovieFromId(movieId);
                        if (movie!= null){
                            String movieName = movie.getName();
                            if (!result.contains(movieName)){
                                result.add(movieName);
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(result);
        if (result.size() > k){
            result = new ArrayList<>(result.subList(0,k));
        }
        return result;
    }

    // @Requirement 7
    public ArrayList<String> findMoviesMatchLatestMovieOf(int userId, int rating, int k) {
        HashSet<String> resultSet = new HashSet<>();
        Movie lastestMovie = findLastRatedMovie(userId);
        if (lastestMovie == null){
            return new ArrayList<>(resultSet);
        }

        String userGender = findGenderOfUserById(userId);
        HashSet<String> lastestMovieGenres = new HashSet<>(lastestMovie.getGenres());
        HashSet<Integer> sameGenderUserIds = new HashSet<>();
        for (User user : users){
            if (user.getGender().equals(userGender)){
                sameGenderUserIds.add(user.getId());
            }
        }
        for (Rating r : ratings){
            if (r.getUserId() != userId && r.getRating() >= rating && sameGenderUserIds.contains(r.getUserId())){
                Movie movie = findMovieFromId(r.getMovieId());
                if (movie != null && hasCommonGenres(lastestMovieGenres, new HashSet<>(movie.getGenres()))){
                    resultSet.add(movie.getName());
                }
            }
        }

        ArrayList<String> result = new ArrayList<>(resultSet);
        Collections.sort(result);
        if (result.size() > k){
            result = new ArrayList<>(result.subList(0,k));
        }
        return result;
    }

    private Movie findLastRatedMovie(int userId){
        Movie lastedRatedMovie = null;
        long lastestRatingTimeSkip = Long.MIN_VALUE;
        for (Rating r : ratings){
            if(r.getUserId() == userId && r.getTimeskip() > lastestRatingTimeSkip){
                lastestRatingTimeSkip = r.getTimeskip();
                lastedRatedMovie = findMovieFromId(r.getMovieId());
            }
        }
        return lastedRatedMovie;
    }
    
    private String findGenderOfUserById(int userId){
        for (User user : users){
            if(user.getId() == userId){
                return user.getGender();
            }
        }
        return "";
    }

    private boolean hasCommonGenres(HashSet<String> set1, HashSet<String> set2){
        return !Collections.disjoint(set1, set2);
    }
}