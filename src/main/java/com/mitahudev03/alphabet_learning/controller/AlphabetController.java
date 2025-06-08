package com.mitahudev03.alphabet_learning.controller;


import com.mitahudev03.alphabet_learning.model.Alphabet;
import com.mitahudev03.alphabet_learning.services.AlphabetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@Controller
public class AlphabetController {
    
    @Autowired
    private AlphabetService alphabetService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("alphabets", alphabetService.getAllAlphabets());
        model.addAttribute("totalLetters", alphabetService.getTotalLetters());
        return "index";
    }
    
    @GetMapping("/alphabet/{letter}")
    public String showAlphabet(@PathVariable String letter, Model model) {
        Alphabet alphabet = alphabetService.getAlphabetByLetter(letter);
        if (alphabet != null) {
            model.addAttribute("alphabet", alphabet);
            model.addAttribute("allAlphabets", alphabetService.getAllAlphabets());
            model.addAttribute("nextLetter", alphabetService.getNextLetter(letter));
            model.addAttribute("prevLetter", alphabetService.getPrevLetter(letter));
            return "alphabet-detail";
        }
        return "redirect:/";
    }
    
    @GetMapping("/quiz")
    public String quiz(Model model) {
        Alphabet randomAlphabet = alphabetService.getRandomAlphabet();
        List<String> options = alphabetService.generateQuizOptions(randomAlphabet.getLetter());
        
        model.addAttribute("quiz", randomAlphabet);
        model.addAttribute("options", options);
        model.addAttribute("allAlphabets", alphabetService.getAllAlphabets());
        return "quiz";
    }
    
    @PostMapping("/quiz/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAnswer(
            @RequestParam String userAnswer, 
            @RequestParam String correctAnswer) {
        
        Map<String, Object> response = new HashMap<>();
        boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);
        
        response.put("correct", isCorrect);
        response.put("message", isCorrect ? "Benar! Pintar sekali!" : "Coba lagi ya!");
        response.put("correctAnswer", correctAnswer);
        
        if (isCorrect) {
            response.put("points", 10);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // API Endpoints for AJAX calls
    @GetMapping("/api/alphabet/random")
    @ResponseBody
    public ResponseEntity<Alphabet> getRandomAlphabet() {
        return ResponseEntity.ok(alphabetService.getRandomAlphabet());
    }
    
    @GetMapping("/api/alphabet/all")
    @ResponseBody
    public ResponseEntity<List<Alphabet>> getAllAlphabets() {
        return ResponseEntity.ok(alphabetService.getAllAlphabets());
    }
    
    @GetMapping("/api/quiz/options/{letter}")
    @ResponseBody
    public ResponseEntity<List<String>> getQuizOptions(@PathVariable String letter) {
        List<String> options = alphabetService.generateQuizOptions(letter);
        return ResponseEntity.ok(options);
    }
    
    // Progress tracking endpoints
    @PostMapping("/api/progress/mark-learned")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markLetterLearned(@RequestParam String letter) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Letter " + letter + " marked as learned");
        return ResponseEntity.ok(response);
    }
    
    // Statistics endpoint
    @GetMapping("/stats")
    public String showStats(Model model) {
        model.addAttribute("totalLetters", alphabetService.getTotalLetters());
        model.addAttribute("alphabets", alphabetService.getAllAlphabets());
        return "stats";
    }
    
    // Print-friendly version
    @GetMapping("/print")
    public String printVersion(Model model) {
        model.addAttribute("alphabets", alphabetService.getAllAlphabets());
        return "print";
    }
    
    // About page
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    // Error handling
    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
        return "error";
    }
}
