package utn.frc.tp_bdii.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final String TMDB_API_KEY = "f4d2fd8d51e851a46365038378c0a76e";

    public String searchMovies(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=" + TMDB_API_KEY + "&query=" + encodedQuery);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                return in.lines().collect(Collectors.joining());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }


    public String getMovieById(String id) {
        try {
            URL url = new URL("https://api.themoviedb.org/3/movie/" + id + "?api_key=" + TMDB_API_KEY);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                return in.lines().collect(Collectors.joining());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
