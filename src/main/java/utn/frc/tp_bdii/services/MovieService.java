package utn.frc.tp_bdii.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final String apiKey = "f4d2fd8d51e851a46365038378c0a76e";
    private final String baseUrl = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();


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



    public Map<String, Object> getMovieDetailsAsMap(String movieId) {
        String url = baseUrl + "/movie/" + movieId + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, Map.class);
    }

    public List<Map<String, Object>> discoverTopMoviesByGenre(String genreName) {
        // Paso 1: obtener todos los géneros
        Map<String, Object> genreResp = restTemplate.getForObject(
                baseUrl + "/genre/movie/list?api_key=" + apiKey,
                Map.class
        );

        List<Map<String, Object>> genres = (List<Map<String, Object>>) genreResp.get("genres");
        Optional<Integer> genreIdOpt = genres.stream()
                .filter(g -> g.get("name").toString().equalsIgnoreCase(genreName))
                .map(g -> (Integer) g.get("id"))
                .findFirst();

        if (genreIdOpt.isEmpty()) return Collections.emptyList();

        int genreId = genreIdOpt.get();

        // Paso 2: buscar películas de ese género ordenadas por score
        String url = baseUrl + "/discover/movie?api_key=" + apiKey +
                "&with_genres=" + genreId +
                "&sort_by=vote_average.desc" +
                "&vote_count.gte=100";

        Map<String, Object> discoverResp = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) discoverResp.get("results");
    }

}
