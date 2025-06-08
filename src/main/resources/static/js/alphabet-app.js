/**
 * ABC Fun Learning - Main Application Script
 * Handles all interactive functionality including:
 * - Speech synthesis
 * - Quiz logic
 * - Progress tracking
 * - Sound effects
 */

class AlphabetApp {
    constructor() {
        // Initialize properties
        this.score = 0;
        this.learnedLetters = new Set();
        this.soundEnabled = true;
        this.voice = null;
        this.quizAnswered = false;
        
        // Initialize the app
        this.init();
    }
    
    init() {
        // Load saved progress from localStorage
        this.loadProgress();
        
        // Initialize speech synthesis with proper event handling
        this.initSpeech();
        
        // Set up event listeners
        this.setupEventListeners();
        
        // Update UI based on loaded data
        this.updateProgress();
        
        // Auto-speak when alphabet detail page loads
        setTimeout(() => {
            this.autoSpeak();
        }, 500);
    }
    
    initSpeech() {
        // Check if browser supports speech synthesis
        if ('speechSynthesis' in window) {
            // Wait for voices to be loaded
            const setVoice = () => {
                const voices = speechSynthesis.getVoices();
                if (voices.length > 0) {
                    // Prefer Indonesian voice if available
                    const indonesianVoice = voices.find(voice => 
                        voice.lang.includes('id') || voice.lang.includes('ID')
                    );
                    this.voice = indonesianVoice || voices[0];
                    console.log('Voice selected:', this.voice ? this.voice.name : 'None');
                }
            };
            
            // Set voice immediately if available
            setVoice();
            
            // Also set voice when voices change (some browsers load voices asynchronously)
            if (speechSynthesis.onvoiceschanged !== undefined) {
                speechSynthesis.onvoiceschanged = setVoice;
            }
        } else {
            console.warn('Speech synthesis not supported');
        }
    }
    
    setupEventListeners() {
        // Sound toggle
        const soundToggle = document.getElementById('soundToggle');
        if (soundToggle) {
            soundToggle.addEventListener('change', (e) => {
                this.soundEnabled = e.target.checked;
                localStorage.setItem('soundEnabled', this.soundEnabled);
            });
        }
        
        // Letter sound button
        const soundBtn = document.getElementById('letterSoundBtn');
        if (soundBtn) {
            soundBtn.addEventListener('click', () => {
                const letter = document.getElementById('currentLetter')?.value;
                const word = document.getElementById('letterWord')?.textContent;
                if (letter && word) {
                    this.speak(`Huruf ${letter}. ${word}`);
                }
            });
        }
        
        // Emoji click for sound
        const letterEmoji = document.getElementById('letterEmoji');
        if (letterEmoji) {
            letterEmoji.addEventListener('click', () => {
                const letter = document.getElementById('currentLetter')?.value;
                const word = document.getElementById('letterWord')?.textContent;
                if (letter && word) {
                    this.speak(`Huruf ${letter}. ${word}`);
                }
            });
        }
        
        // Quiz answer buttons
        document.querySelectorAll('.quiz-option').forEach(button => {
            button.addEventListener('click', (e) => {
                if (!this.quizAnswered) {
                    this.handleQuizAnswer(e);
                }
            });
        });
        
        // Navigation buttons
        const prevBtn = document.getElementById('prevLetterBtn');
        const nextBtn = document.getElementById('nextLetterBtn');
        
        if (prevBtn && prevBtn.getAttribute('href')) {
            prevBtn.addEventListener('click', (e) => {
                e.preventDefault();
                window.location.href = prevBtn.getAttribute('href');
            });
        }
        
        if (nextBtn && nextBtn.getAttribute('href')) {
            nextBtn.addEventListener('click', (e) => {
                e.preventDefault();
                window.location.href = nextBtn.getAttribute('href');
            });
        }
    }
    
    autoSpeak() {
        // Auto-speak on alphabet detail page
        if (document.getElementById('letterEmoji')) {
            const letter = document.getElementById('currentLetter')?.value;
            const word = document.getElementById('letterWord')?.textContent;
            if (letter && word && this.soundEnabled) {
                setTimeout(() => {
                    this.speak(`Huruf ${letter}. ${word}`);
                }, 1000);
            }
        }
        
        // Auto-speak on quiz page
        if (document.getElementById('quizEmoji')) {
            if (this.soundEnabled) {
                setTimeout(() => {
                    this.speak('Apa huruf untuk gambar ini?');
                }, 1000);
            }
        }
    }
    
    speak(text) {
        if (!this.soundEnabled) return;
        
        // Check if speech synthesis is available
        if (!('speechSynthesis' in window)) {
            console.warn('Speech synthesis not supported');
            return;
        }
        
        // Cancel any ongoing speech
        speechSynthesis.cancel();
        
        // Create new utterance
        const utterance = new SpeechSynthesisUtterance(text);
        
        // Set voice if available
        if (this.voice) {
            utterance.voice = this.voice;
        }
        
        // Set speech parameters
        utterance.rate = 0.8;
        utterance.pitch = 1.1;
        utterance.volume = 1.0;
        
        // Error handling
        utterance.onerror = (event) => {
            console.error('Speech synthesis error:', event.error);
        };
        
        utterance.onstart = () => {
            console.log('Speech started:', text);
        };
        
        utterance.onend = () => {
            console.log('Speech ended');
        };
        
        // Speak the text
        try {
            speechSynthesis.speak(utterance);
        } catch (error) {
            console.error('Error speaking:', error);
        }
    }
    
    handleQuizAnswer(event) {
        if (this.quizAnswered) return;
        
        const button = event.currentTarget;
        const userAnswer = button.textContent.trim();
        const correctAnswerElement = document.getElementById('correctAnswer');
        
        if (!correctAnswerElement) {
            console.error('Correct answer element not found');
            return;
        }
        
        const correctAnswer = correctAnswerElement.value;
        this.quizAnswered = true;
        
        // Disable all buttons to prevent multiple answers
        document.querySelectorAll('.quiz-option').forEach(btn => {
            btn.disabled = true;
            btn.style.pointerEvents = 'none';
        });
        
        // Check if answer is correct
        const isCorrect = userAnswer === correctAnswer;
        
        // Visual feedback with animation
        if (isCorrect) {
            button.classList.remove('bg-gradient-to-r', 'from-gray-50', 'to-gray-100');
            button.classList.add('bg-green-500', 'text-white', 'transform', 'scale-105');
            
            // Add score
            this.addScore(10);
            
            // Mark letter as learned
            this.markLetterLearned(correctAnswer);
            
            // Celebration effect
            this.showCelebration();
        } else {
            button.classList.remove('bg-gradient-to-r', 'from-gray-50', 'to-gray-100');
            button.classList.add('bg-red-500', 'text-white');
            
            // Highlight correct answer
            document.querySelectorAll('.quiz-option').forEach(btn => {
                if (btn.textContent.trim() === correctAnswer) {
                    btn.classList.remove('bg-gradient-to-r', 'from-gray-50', 'to-gray-100');
                    btn.classList.add('bg-green-500', 'text-white', 'transform', 'scale-105');
                }
            });
        }
        
        // Show feedback message
        const feedback = document.getElementById('quizFeedback');
        if (feedback) {
            const message = isCorrect ? 'Benar! Pintar sekali! 🎉' : 'Coba lagi ya! 😊';
            feedback.textContent = message;
            feedback.className = isCorrect ? 
                'text-center mt-6 text-2xl font-bold text-green-500 animate-bounce' : 
                'text-center mt-6 text-2xl font-bold text-red-500';
            feedback.classList.remove('hidden');
        }
        
        // Speak feedback
        const feedbackText = isCorrect ? 'Benar! Pintar sekali!' : 'Coba lagi ya.';
        setTimeout(() => {
            this.speak(feedbackText);
        }, 500);
        
        // Show next button after delay
        setTimeout(() => {
            const nextBtn = document.getElementById('nextQuizBtn');
            if (nextBtn) {
                nextBtn.classList.remove('hidden');
                nextBtn.classList.add('animate-pulse');
            }
        }, 2000);
    }
    
    showCelebration() {
        // Simple celebration effect
        const celebration = document.createElement('div');
        celebration.innerHTML = '🎉✨🎊';
        celebration.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-size: 4rem;
            z-index: 1000;
            animation: celebration 2s ease-out forwards;
            pointer-events: none;
        `;
        
        // Add celebration animation
        const style = document.createElement('style');
        style.textContent = `
            @keyframes celebration {
                0% { opacity: 0; transform: translate(-50%, -50%) scale(0.5); }
                50% { opacity: 1; transform: translate(-50%, -50%) scale(1.2); }
                100% { opacity: 0; transform: translate(-50%, -50%) scale(1.5); }
            }
        `;
        document.head.appendChild(style);
        document.body.appendChild(celebration);
        
        setTimeout(() => {
            document.body.removeChild(celebration);
            document.head.removeChild(style);
        }, 2000);
    }
    
    addScore(points) {
        this.score += points;
        localStorage.setItem('abcScore', this.score);
        
        // Update score display
        const scoreElement = document.getElementById('score');
        if (scoreElement) scoreElement.textContent = this.score;
        
        // Also update in stats if available
        const totalScoreElement = document.getElementById('totalScore');
        if (totalScoreElement) totalScoreElement.textContent = this.score;
    }
    
    markLetterLearned(letter) {
        this.learnedLetters.add(letter.toUpperCase());
        localStorage.setItem('learnedLetters', JSON.stringify(Array.from(this.learnedLetters)));
        this.updateProgress();
    }
    
    loadProgress() {
        // Load score
        const savedScore = localStorage.getItem('abcScore');
        if (savedScore) {
            this.score = parseInt(savedScore, 10) || 0;
        }
        
        // Load learned letters
        const savedLetters = localStorage.getItem('learnedLetters');
        if (savedLetters) {
            try {
                this.learnedLetters = new Set(JSON.parse(savedLetters));
            } catch (e) {
                console.error('Error loading learned letters:', e);
                this.learnedLetters = new Set();
            }
        }
        
        // Load sound preference
        const soundPref = localStorage.getItem('soundEnabled');
        if (soundPref !== null) {
            this.soundEnabled = soundPref === 'true';
            const soundToggle = document.getElementById('soundToggle');
            if (soundToggle) soundToggle.checked = this.soundEnabled;
        }
    }
    
    updateProgress() {
        // Update progress bar
        const progressPercent = (this.learnedLetters.size / 26) * 100;
        const progressBar = document.getElementById('progress');
        const progressText = document.getElementById('progressText');
        
        if (progressBar) {
            progressBar.style.width = `${progressPercent}%`;
        }
        
        if (progressText) {
            progressText.textContent = `${this.learnedLetters.size}/26`;
        }
        
        // Update score display
        const scoreElement = document.getElementById('score');
        if (scoreElement) scoreElement.textContent = this.score;
        
        // Update card styles for learned letters
        document.querySelectorAll('.card-alphabet').forEach(card => {
            const letter = card.getAttribute('data-letter');
            if (this.learnedLetters.has(letter)) {
                card.classList.add('ring-2', 'ring-yellow-400');
            } else {
                card.classList.remove('ring-2', 'ring-yellow-400');
            }
        });
    }
    
    resetProgress() {
        if (confirm('Apakah kamu yakin ingin mereset semua progress?')) {
            this.score = 0;
            this.learnedLetters = new Set();
            localStorage.removeItem('abcScore');
            localStorage.removeItem('learnedLetters');
            
            // Update UI
            this.updateProgress();
            
            // Show confirmation
            alert('Progress telah direset!');
        }
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM loaded, initializing AlphabetApp...');
    window.alphabetApp = new AlphabetApp();
});

// Fallback initialization for browsers that might not support DOMContentLoaded properly
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        if (!window.alphabetApp) {
            console.log('Fallback initialization...');
            window.alphabetApp = new AlphabetApp();
        }
    });
} else {
    // DOM is already ready
    console.log('DOM already loaded, initializing AlphabetApp...');
    window.alphabetApp = new AlphabetApp();
}