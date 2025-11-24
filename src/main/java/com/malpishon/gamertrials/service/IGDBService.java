package com.malpishon.gamertrials.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malpishon.gamertrials.dto.IGDBGame;
import com.malpishon.gamertrials.dto.TwitchTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IGDBService {

    @Value("${igdb.client-id}")
    private String clientId;

    @Value("${igdb.client-secret}")
    private String clientSecret;

    @Value("${igdb.token-url}")
    private String tokenUrl;

    @Value("${igdb.api-url}")
    private String apiUrl;

    private String accessToken;
    private Instant tokenExpiration;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IGDBService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private void refreshTokenIfNeeded() {
        if (accessToken == null || tokenExpiration == null || Instant.now().isAfter(tokenExpiration)) {
            obtainAccessToken();
        }
    }

    private void obtainAccessToken() {
        try {
            System.out.println("Obtaining IGDB access token...");
            String url = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("grant_type", "client_credentials")
                    .toUriString();

            ResponseEntity<TwitchTokenResponse> response = restTemplate.postForEntity(
                    url,
                    null,
                    TwitchTokenResponse.class
            );

            if (response.getBody() != null) {
                this.accessToken = response.getBody().getAccessToken();
                this.tokenExpiration = Instant.now().plusSeconds(response.getBody().getExpiresIn() - 300);
                System.out.println("IGDB token obtained successfully");
            }
        } catch (Exception e) {
            System.err.println("Failed to obtain IGDB access token: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to obtain IGDB access token", e);
        }
    }

    public List<IGDBGame> searchGames(String query) {
        return searchGames(query, 10);
    }

    public List<IGDBGame> searchGames(String query, int limit) {
        refreshTokenIfNeeded();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", clientId);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.set("Accept", "application/json");

            String body = String.format(
                    "search \"%s\"; " +
                    "fields name, cover.url, cover.image_id, first_release_date, platforms.name, genres.name, rating_count, category; " +
                    "limit %d;",
                    query.replace("\"", "\\\""),
                    limit * 3
            );

            System.out.println("IGDB Query: " + body);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl + "/games",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getBody() == null || response.getBody().equals("[]")) {
                System.out.println("No games found");
                return Collections.emptyList();
            }

            IGDBGame[] games = objectMapper.readValue(response.getBody(), IGDBGame[].class);
            System.out.println("Found " + games.length + " games before filtering");

            List<IGDBGame> filteredGames = Arrays.stream(games)
                    .filter(game -> {
                        Integer ratingCount = game.getRatingCount();
                        if (ratingCount != null && ratingCount < 10) {
                            System.out.println("Excluding: " + game.getName() + " (rating_count too low: " + ratingCount + ")");
                            return false;
                        }

                        Integer category = game.getCategory();
                        if (category != null && category != 0 && category != 2 && category != 4 && category != 8 && category != 9 && category != 10) {
                            System.out.println("Excluding: " + game.getName() + " (category: " + category + ")");
                            return false;
                        }

                        return true;
                    })
                    .sorted((g1, g2) -> {
                        Integer rc1 = g1.getRatingCount() != null ? g1.getRatingCount() : 0;
                        Integer rc2 = g2.getRatingCount() != null ? g2.getRatingCount() : 0;
                        return rc2.compareTo(rc1);
                    })
                    .limit(limit)
                    .collect(Collectors.toList());

            System.out.println("Returning " + filteredGames.size() + " filtered games");
            return filteredGames;

        } catch (Exception e) {
            System.err.println("Error searching games: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public IGDBGame getGameById(Long gameId) {
        refreshTokenIfNeeded();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", clientId);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.TEXT_PLAIN);

            String body = String.format(
                    "fields name,cover.url,cover.image_id,first_release_date,platforms.name,genres.name; " +
                    "where id = %d & (category = 0 | category = 8 | category = 9);",
                    gameId
            );

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl + "/games",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getBody() != null) {
                IGDBGame[] games = objectMapper.readValue(response.getBody(), IGDBGame[].class);
                if (games.length > 0) {
                    return games[0];
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<IGDBGame> getPopularGames(int limit) {
        refreshTokenIfNeeded();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", clientId);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.TEXT_PLAIN);

            String body = String.format(
                    "fields name,cover.url,cover.image_id,first_release_date,platforms.name,genres.name; " +
                    "where rating_count > 10 & (category = 0 | category = 8 | category = 9); " +
                    "sort rating_count desc; " +
                    "limit %d;",
                    limit
            );

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl + "/games",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getBody() != null) {
                IGDBGame[] games = objectMapper.readValue(response.getBody(), IGDBGame[].class);
                return Arrays.asList(games);
            }

            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

