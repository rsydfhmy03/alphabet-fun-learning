package com.mitahudev03.alphabet_learning.model;

public class Alphabet {
    private String letter;
    private String word;
    private String description;
    private String emoji;
    private String colorClass;
    private String pronunciation;
    private String category;
    
    // Default constructor
    public Alphabet() {}
    
    // Constructor dengan parameter utama
    public Alphabet(String letter, String word, String description, String emoji, String colorClass) {
        this.letter = letter;
        this.word = word;
        this.description = description;
        this.emoji = emoji;
        this.colorClass = colorClass;
        this.pronunciation = generatePronunciation(letter);
        this.category = determineCategory(word);
    }
    
    // Constructor lengkap
    public Alphabet(String letter, String word, String description, String emoji, 
                   String colorClass, String pronunciation, String category) {
        this.letter = letter;
        this.word = word;
        this.description = description;
        this.emoji = emoji;
        this.colorClass = colorClass;
        this.pronunciation = pronunciation;
        this.category = category;
    }
    
    private String generatePronunciation(String letter) {
        // Pronunciation guide dalam bahasa Indonesia
        switch (letter.toUpperCase()) {
            case "A": return "ah";
            case "B": return "beh";
            case "C": return "ceh";
            case "D": return "deh";
            case "E": return "eh";
            case "F": return "ef";
            case "G": return "geh";
            case "H": return "hah";
            case "I": return "ih";
            case "J": return "jeh";
            case "K": return "kah";
            case "L": return "el";
            case "M": return "em";
            case "N": return "en";
            case "O": return "oh";
            case "P": return "peh";
            case "Q": return "kiu";
            case "R": return "er";
            case "S": return "es";
            case "T": return "teh";
            case "U": return "uh";
            case "V": return "feh";
            case "W": return "weh";
            case "X": return "eks";
            case "Y": return "yeh";
            case "Z": return "zet";
            default: return letter.toLowerCase();
        }
    }
    
    private String determineCategory(String word) {
        String lowerWord = word.toLowerCase();
        if (lowerWord.matches(".*(apple|orange|banana|grape).*")) return "Buah";
        if (lowerWord.matches(".*(cat|dog|elephant|lion|tiger).*")) return "Hewan";
        if (lowerWord.matches(".*(house|car|ball|book).*")) return "Benda";
        if (lowerWord.matches(".*(sun|moon|star|rainbow).*")) return "Alam";
        return "Umum";
    }
    
    // Getters dan Setters
    public String getLetter() { return letter; }
    public void setLetter(String letter) { this.letter = letter; }
    
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    
    public String getColorClass() { return colorClass; }
    public void setColorClass(String colorClass) { this.colorClass = colorClass; }
    
    public String getPronunciation() { return pronunciation; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return "Alphabet{" +
                "letter='" + letter + '\'' +
                ", word='" + word + '\'' +
                ", description='" + description + '\'' +
                ", emoji='" + emoji + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}