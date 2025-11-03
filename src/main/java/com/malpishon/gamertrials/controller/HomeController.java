package com.malpishon.gamertrials.controller;

import com.malpishon.gamertrials.model.User;
import com.malpishon.gamertrials.repository.UserRepository;
import com.malpishon.gamertrials.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public HomeController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/challenges")
    public String challenges() {
        return "challenges";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);
        // Countries list for select
        model.addAttribute("countries", getCountryOptions());
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        int xpToNext = userService.getXpToNextLevel(user);
        int xpPct = userService.getXpProgressPercent(user);
        Integer age = userService.getPublicAge(user);
        CountryDisplay cd = resolveCountryDisplay(user.getLocation());
        model.addAttribute("user", user);
        model.addAttribute("xpToNext", xpToNext);
        model.addAttribute("xpPct", xpPct);
        model.addAttribute("age", age);
        model.addAttribute("countryName", cd.name);
        model.addAttribute("countryFlag", cd.flag);
        return "profile";
    }

    @PostMapping("/dashboard/edit")
    public String updateProfile(@ModelAttribute("user") User formUser,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                @RequestParam(value = "profilePrivate", required = false) Boolean profilePrivate,
                                Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setBio(formUser.getBio());
        user.setBirthDate(formUser.getBirthDate());
        user.setProfilePrivate(profilePrivate != null ? profilePrivate : false);
        if (formUser.getLocation() != null && !formUser.getLocation().isBlank()) {
            user.setLocation(formUser.getLocation().trim());
        }

        try {
            if (avatar != null && !avatar.isEmpty()) {
                String baseDir = System.getProperty("user.home") + "/gamer-trials/uploads/";
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(baseDir);
                java.nio.file.Files.createDirectories(uploadPath);

                String original = avatar.getOriginalFilename();
                String ext = "";
                if (original != null && original.contains(".")) {
                    ext = original.substring(original.lastIndexOf('.'));
                }
                String safeName = "avatar-" + user.getId() + "-" + System.currentTimeMillis() + ext;
                java.nio.file.Path target = uploadPath.resolve(safeName);
                try (java.io.InputStream in = avatar.getInputStream()) {
                    java.nio.file.Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                user.setAvatarUrl("/uploads/" + safeName);
            }
        } catch (Exception ex) {
        }

        userRepository.save(user);
        return "redirect:/dashboard?updated=true";
    }

    private static String flagEmoji(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) return "";
        String cc = countryCode.toUpperCase(Locale.ROOT);
        int base = 0x1F1E6 - 'A';
        int c1 = base + cc.charAt(0);
        int c2 = base + cc.charAt(1);
        return new String(Character.toChars(c1)) + new String(Character.toChars(c2));
    }

    private static class CountryOption {
        public final String code; public final String name; public final String flag;
        CountryOption(String code, String name){ this.code=code; this.name=name; this.flag=flagEmoji(code);} }

    private List<CountryOption> getCountryOptions(){
        String[] codes = Locale.getISOCountries();
        List<CountryOption> list = new ArrayList<>();
        for (String code : codes) {
            Locale locale = new Locale("", code);
            list.add(new CountryOption(code, locale.getDisplayCountry(Locale.ENGLISH)));
        }
        list.sort(Comparator.comparing(o -> o.name));
        return list;
    }

    private static class CountryDisplay { public final String name; public final String flag; CountryDisplay(String n,String f){name=n;flag=f;} }

    private CountryDisplay resolveCountryDisplay(String stored){
        if (stored == null || stored.isBlank()) return new CountryDisplay(null, "");
        String s = stored.trim();
        // If looks like ISO code
        if (s.length()==2 && s.equals(s.toUpperCase(Locale.ROOT))){
            Locale loc = new Locale("", s);
            String name = loc.getDisplayCountry(Locale.ENGLISH);
            return new CountryDisplay(name!=null && !name.isEmpty()?name:s, flagEmoji(s));
        }
        // Try to match by name
        for (String code : Locale.getISOCountries()){
            Locale loc = new Locale("", code);
            String name = loc.getDisplayCountry(Locale.ENGLISH);
            if (name.equalsIgnoreCase(s)){
                return new CountryDisplay(name, flagEmoji(code));
            }
        }
        return new CountryDisplay(s, "");
    }
}