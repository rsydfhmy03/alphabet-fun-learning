package com.mitahudev03.alphabet_learning.services;

import com.mitahudev03.alphabet_learning.model.Alphabet;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlphabetService {
    
    private List<Alphabet> alphabets;
    private final String ALPHABET_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public AlphabetService() {
        initializeAlphabets();
    }
    
    private void initializeAlphabets() {
        alphabets = Arrays.asList(
            new Alphabet("A", "Apple", "Apel merah yang manis dan segar", "🍎", "bg-red-100 border-red-300"),
            new Alphabet("B", "Ball", "Bola bundar untuk bermain", "⚽", "bg-blue-100 border-blue-300"),
            new Alphabet("C", "Cat", "Kucing yang lucu dan menggemaskan", "🐱", "bg-yellow-100 border-yellow-300"),
            new Alphabet("D", "Dog", "Anjing yang setia dan ramah", "🐶", "bg-green-100 border-green-300"),
            new Alphabet("E", "Elephant", "Gajah yang besar dan kuat", "🐘", "bg-purple-100 border-purple-300"),
            new Alphabet("F", "Fish", "Ikan yang berenang di air", "🐠", "bg-cyan-100 border-cyan-300"),
            new Alphabet("G", "Giraffe", "Jerapah dengan leher yang panjang", "🦒", "bg-orange-100 border-orange-300"),
            new Alphabet("H", "House", "Rumah tempat tinggal yang nyaman", "🏠", "bg-pink-100 border-pink-300"),
            new Alphabet("I", "Ice Cream", "Es krim yang dingin dan manis", "🍦", "bg-indigo-100 border-indigo-300"),
            new Alphabet("J", "Juice", "Jus buah yang segar", "🧃", "bg-lime-100 border-lime-300"),
            new Alphabet("K", "Kite", "Layang-layang yang terbang tinggi", "🪁", "bg-teal-100 border-teal-300"),
            new Alphabet("L", "Lion", "Singa yang gagah dan kuat", "🦁", "bg-amber-100 border-amber-300"),
            new Alphabet("M", "Moon", "Bulan yang bersinar di malam hari", "🌙", "bg-slate-100 border-slate-300"),
            new Alphabet("N", "Nest", "Sarang burung di atas pohon", "🪺", "bg-stone-100 border-stone-300"),
            new Alphabet("O", "Orange", "Jeruk yang manis dan berair", "🍊", "bg-orange-100 border-orange-300"),
            new Alphabet("P", "Penguin", "Pinguin yang lucu di kutub", "🐧", "bg-gray-100 border-gray-300"),
            new Alphabet("Q", "Queen", "Ratu yang cantik dan bijaksana", "👸", "bg-rose-100 border-rose-300"),
            new Alphabet("R", "Rainbow", "Pelangi yang indah setelah hujan", "🌈", "bg-violet-100 border-violet-300"),
            new Alphabet("S", "Sun", "Matahari yang bersinar terang", "☀️", "bg-yellow-100 border-yellow-300"),
            new Alphabet("T", "Tree", "Pohon yang tinggi dan rindang", "🌳", "bg-green-100 border-green-300"),
            new Alphabet("U", "Umbrella", "Payung untuk melindungi dari hujan", "☂️", "bg-blue-100 border-blue-300"),
            new Alphabet("V", "Violin", "Biola yang menghasilkan musik indah", "🎻", "bg-purple-100 border-purple-300"),
            new Alphabet("W", "Whale", "Paus besar di lautan luas", "🐋", "bg-blue-100 border-blue-300"),
            new Alphabet("X", "Xylophone", "Alat musik dengan bilah kayu", "🎵", "bg-red-100 border-red-300"),
            new Alphabet("Y", "Yacht", "Kapal pesiar yang mewah", "🛥️", "bg-cyan-100 border-cyan-300"),
            new Alphabet("Z", "Zebra", "Zebra dengan garis hitam putih", "🦓", "bg-gray-100 border-gray-300")
        );
    }
    
    public List<Alphabet> getAllAlphabets() {
        return new ArrayList<>(alphabets);
    }
    
    public Alphabet getAlphabetByLetter(String letter) {
        return alphabets.stream()
                .filter(a -> a.getLetter().equalsIgnoreCase(letter))
                .findFirst()
                .orElse(null);
    }
    
    public Alphabet getRandomAlphabet() {
        Random random = new Random();
        return alphabets.get(random.nextInt(alphabets.size()));
    }
    
    public List<String> generateQuizOptions(String correctLetter) {
        List<String> options = new ArrayList<>();
        options.add(correctLetter);
        
        List<String> otherLetters = alphabets.stream()
                .map(Alphabet::getLetter)
                .filter(letter -> !letter.equals(correctLetter))
                .collect(Collectors.toList());
        
        Collections.shuffle(otherLetters);
        
        // Add 3 random wrong options
        for (int i = 0; i < 3 && i < otherLetters.size(); i++) {
            options.add(otherLetters.get(i));
        }
        
        Collections.shuffle(options);
        return options;
    }
    
    public String getNextLetter(String currentLetter) {
        int index = ALPHABET_STRING.indexOf(currentLetter.toUpperCase());
        if (index >= 0 && index < ALPHABET_STRING.length() - 1) {
            return String.valueOf(ALPHABET_STRING.charAt(index + 1));
        }
        return String.valueOf(ALPHABET_STRING.charAt(0)); // Loop back to A
    }
    
    public String getPrevLetter(String currentLetter) {
        int index = ALPHABET_STRING.indexOf(currentLetter.toUpperCase());
        if (index > 0) {
            return String.valueOf(ALPHABET_STRING.charAt(index - 1));
        }
        return String.valueOf(ALPHABET_STRING.charAt(ALPHABET_STRING.length() - 1)); // Loop to Z
    }
    
    public int getTotalLetters() {
        return alphabets.size();
    }
    
    public List<Alphabet> getAlphabetsByCategory(String category) {
        // Future enhancement: categorize alphabets
        return alphabets;
    }
    
    public List<Alphabet> searchAlphabets(String query) {
        return alphabets.stream()
                .filter(a -> a.getLetter().toLowerCase().contains(query.toLowerCase()) ||
                           a.getWord().toLowerCase().contains(query.toLowerCase()) ||
                           a.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}


