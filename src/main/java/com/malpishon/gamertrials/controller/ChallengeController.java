package com.malpishon.gamertrials.controller;

import com.malpishon.gamertrials.model.Challenge;
import com.malpishon.gamertrials.model.User;
import com.malpishon.gamertrials.repository.UserRepository;
import com.malpishon.gamertrials.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserRepository userRepository;

    @Autowired
    public ChallengeController(ChallengeService challengeService, UserRepository userRepository) {
        this.challengeService = challengeService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("userRole", user.getRole());
            }
        }
    }

    @GetMapping
    public String listChallenges(Model model) {
        List<Challenge> challenges = challengeService.getAllChallenges();
        model.addAttribute("challenges", challenges);
        return "challenges";
    }

    @GetMapping("/{id}")
    public String viewChallenge(@PathVariable Long id, Model model) {
        Challenge challenge = challengeService.getById(id);
        if (challenge == null) {
            return "redirect:/challenges";
        }
        model.addAttribute("challenge", challenge);
        if (challenge.getCreator() != null) {
            model.addAttribute("creator", challenge.getCreator());
        }
        return "challenge-detail";
    }

    @GetMapping("/new")
    public String newChallengeForm(Model model) {
        model.addAttribute("challenge", new Challenge());
        return "challenge-create";
    }

    @PostMapping("/new")
    public String createChallenge(@ModelAttribute Challenge challenge, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User creator = userRepository.findByUsername(principal.getName()).orElse(null);
        if (creator == null) {
            return "redirect:/login";
        }
        challenge.setCreator(creator);
        challenge.setDateOfCreation(LocalDate.now());
        if (challenge.getStatus() == null || challenge.getStatus().isEmpty()) {
            challenge.setStatus("ACTIVE");
        }
        if (challenge.getVisibility() == null || challenge.getVisibility().isEmpty()) {
            challenge.setVisibility("PUBLIC");
        }
        challenge.setPublic("PUBLIC".equals(challenge.getVisibility()));
        if (challenge.getRewardXp() == null) {
            challenge.setRewardXp(0);
        }
        challengeService.createChallenge(challenge);
        return "redirect:/challenges";
    }

    @GetMapping("/{id}/edit")
    public String editChallengeForm(@PathVariable Long id, Model model) {
        Challenge challenge = challengeService.getById(id);
        if (challenge == null) return "redirect:/challenges";
        model.addAttribute("challenge", challenge);
        model.addAttribute("games", new ArrayList<>());
        return "challenge-edit";
    }

    @PostMapping("/{id}/edit")
    public String submitEdit(@PathVariable Long id, @ModelAttribute Challenge updated) {
        Challenge existing = challengeService.getById(id);
        if (existing == null) return "redirect:/challenges";
        // only admins can access this path (configured in SecurityConfig)
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setGame(updated.getGame());
        existing.setPlatform(updated.getPlatform());
        existing.setCategory(updated.getCategory());
        existing.setType(updated.getType());
        existing.setDifficulty(updated.getDifficulty());
        existing.setVisibility(updated.getVisibility());
        existing.setPublic(updated.isPublic());
        existing.setStatus(updated.getStatus());
        existing.setDateOfClosing(updated.getDateOfClosing());
        // rewardXp left for admins only if desired
        challengeService.updateChallenge(existing);
        return "redirect:/challenges/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteChallenge(@PathVariable Long id) {
        // admins only (SecurityConfig)
        challengeService.deleteById(id);
        return "redirect:/challenges";
    }
}
