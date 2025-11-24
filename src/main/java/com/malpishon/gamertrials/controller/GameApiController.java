package com.malpishon.gamertrials.controller;

import com.malpishon.gamertrials.dto.IGDBGame;
import com.malpishon.gamertrials.service.IGDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameApiController {

    private final IGDBService igdbService;

    @Autowired
    public GameApiController(IGDBService igdbService) {
        this.igdbService = igdbService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<IGDBGame>> searchGames(@RequestParam("q") String query) {
        List<IGDBGame> games = igdbService.searchGames(query, 20);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IGDBGame> getGameById(@PathVariable Long id) {
        IGDBGame game = igdbService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        }
        return ResponseEntity.notFound().build();
    }
}

