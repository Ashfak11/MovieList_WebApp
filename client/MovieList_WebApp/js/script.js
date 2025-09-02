let allMovies = []; // Store all movies fetched from the backend

// Function to toggle dark mode
function toggleDarkMode() {
    const body = document.body;
    const toggle = document.getElementById('toggle');
    const dot = document.querySelector('.dot');

    if (toggle.checked) {
        body.classList.add('dark');
        localStorage.setItem('darkMode', 'enabled');
        dot.style.transform = 'translateX(100%)';
    } else {
        body.classList.remove('dark');
        localStorage.setItem('darkMode', 'disabled');
        dot.style.transform = 'translateX(0)';
    }
}

// Check user's dark mode preference on page load
document.addEventListener('DOMContentLoaded', () => {
    const darkModePreference = localStorage.getItem('darkMode');
    const toggle = document.getElementById('toggle');

    if (darkModePreference === 'enabled') {
        document.body.classList.add('dark');
        toggle.checked = true;
        document.querySelector('.dot').style.transform = 'translateX(100%)';
    }

    // Fetch default category (Action) on load
    fetchMovies('action');
});

// Fetch movies from backend based on genre
async function fetchMovies(genre = '') {
    try {
        let apiUrl = `/api/movies/${genre}`; // Backend endpoint (Spring Boot)

        const response = await fetch(apiUrl);
        const data = await response.json();

        allMovies = data; // Store fetched movies
        displayMovies(allMovies); // Display movies
        highlightActiveTab(genre);
    } catch (error) {
        console.error('Error fetching movies from backend:', error);
    }
}

// Display movies in the grid
function displayMovies(movies) {
    const movieGrid = document.getElementById('movieGrid');
    movieGrid.innerHTML = ''; // Clear existing movies

    movies.forEach(movie => {
        const movieCard = document.createElement('div');
        movieCard.className = 'movie-card bg-white shadow-md rounded-lg overflow-hidden hover:bg-gray-100 dark:bg-gray-800 dark:hover:bg-gray-700 transition-transform duration-300 transform hover:scale-105';
        
        movieCard.setAttribute('data-title', movie.title);
        movieCard.setAttribute('data-description', movie.overview);

        movieCard.innerHTML = `
            <img src="https://image.tmdb.org/t/p/w500${movie.posterPath}" alt="${movie.title}" class="w-full h-64 object-cover">
            <div class="p-4">
                <h3 class="text-lg font-semibold text-gray-900 dark:text-gray-100">${movie.title}</h3>
                <p class="text-gray-700 dark:text-gray-300">Release Year: ${new Date(movie.releaseDate).getFullYear()}</p>
                <p class="text-gray-700 dark:text-gray-300">Rating: ${movie.rating} / 10</p>
                <button onclick="openModal('${escapeQuotes(movie.title)}', 'https://image.tmdb.org/t/p/w500${movie.posterPath}', '${escapeQuotes(movie.overview)}')" class="mt-2 py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-600 transition-colors duration-300">
                    View Details
                </button>
                <button class="mt-2 py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-600 transition-colors duration-300" onclick="addToWatchlist('${escapeQuotes(movie.title)}', '${escapeQuotes(movie.overview)}', '${movie.posterPath}', '${movie.releaseDate}', '${movie.rating}')">
                    <span class="plus">+</span> Watchlist
                </button>
            </div>
        `;
        movieGrid.appendChild(movieCard);
    });
}

// Escape quotes in strings
function escapeQuotes(text) {
    return text.replace(/'/g, "\\'").replace(/"/g, '\\"');
}

// Watchlist functions
function addToWatchlist(title, overview, posterPath, releaseDate, rating) {
    const movie = { title, overview, posterPath, releaseDate, rating };
    let watchlist = JSON.parse(localStorage.getItem('watchlist')) || [];

    if (!watchlist.some(item => item.title === title)) {
        watchlist.push(movie);
        localStorage.setItem('watchlist', JSON.stringify(watchlist));
        alert(`${title} added to your watchlist!`);
    } else {
        alert(`${title} is already in your watchlist.`);
    }
}

function viewWatchlist() {
    const watchlistModal = document.getElementById('watchlistModal');
    const watchlistContainer = document.getElementById('watchlistContainer');
    watchlistContainer.innerHTML = '';

    let watchlist = JSON.parse(localStorage.getItem('watchlist')) || [];

    if (watchlist.length === 0) {
        watchlistContainer.innerHTML = '<p>Your watchlist is empty.</p>';
    } else {
        watchlist.forEach(movie => {
            const movieItem = document.createElement('div');
            movieItem.className = 'watchlist-item p-4 mb-2 bg-gray-100 dark:bg-gray-700 rounded-lg';
            movieItem.innerHTML = `
                <img src="https://image.tmdb.org/t/p/w500${movie.posterPath}" alt="${movie.title}" class="w-16 h-24 object-cover inline-block mr-4">
                <div class="inline-block align-middle">
                    <h3 class="text-lg font-semibold text-gray-900 dark:text-gray-100">${movie.title}</h3>
                    <p class="text-sm text-gray-700 dark:text-gray-300">Release Year: ${new Date(movie.releaseDate).getFullYear()}</p>
                    <p class="text-sm text-gray-700 dark:text-gray-300">Rating: ${movie.rating} / 10</p>
                </div>
                <button onclick="removeFromWatchlist('${escapeQuotes(movie.title)}')" class="py-1 px-3 bg-red-500 text-white rounded hover:bg-red-600 dark:bg-red-700 dark:hover:bg-red-600 transition-colors duration-300">
                    Remove
                </button>
            `;
            watchlistContainer.appendChild(movieItem);
        });
    }
    watchlistModal.classList.remove('hidden');
}

function closeWatchlist() {
    document.getElementById('watchlistModal').classList.add('hidden');
}

function removeFromWatchlist(title) {
    let watchlist = JSON.parse(localStorage.getItem('watchlist')) || [];
    watchlist = watchlist.filter(movie => movie.title !== title);
    localStorage.setItem('watchlist', JSON.stringify(watchlist));
    viewWatchlist(); // Refresh view
}

// Modal functions
function openModal(title, image, description) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalImage').src = image;
    document.getElementById('modalDescription').textContent = description;
    document.getElementById('movieModal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('movieModal').classList.add('hidden');
}

// Login
document.getElementById('loginButton').addEventListener('click', login);
async function login() {
    const username = prompt('Enter your username:');
    const password = prompt('Enter your password:');

    if (username === 'admin' && password === 'password') {
        alert('Login successful!');
        localStorage.setItem('isLoggedIn', true);
    } else {
        alert('Invalid username or password');
    }
}

// Highlight active genre tab
function highlightActiveTab(activeGenre) {
    const buttons = document.querySelectorAll('div.flex.space-x-4.mb-6 button');
    buttons.forEach(button => {
        if (button.textContent.toLowerCase() === activeGenre) {
            button.classList.add('text-blue-500', 'border-b-2', 'border-blue-500', 'dark:text-blue-300', 'dark:border-blue-300');
        } else {
            button.classList.remove('text-blue-500', 'border-b-2', 'border-blue-500', 'dark:text-blue-300', 'dark:border-blue-300');
        }
    });
}

// Search movies (calls backend search if you implement search endpoint)
async function searchMovies(query) {
    try {
        const response = await fetch(`/api/movies/search?query=${encodeURIComponent(query)}`);
        const data = await response.json();
        allMovies = data;
        displayMovies(allMovies);
    } catch (error) {
        console.error('Error searching movies:', error);
    }
}

// Search bar input
document.getElementById('searchBar').addEventListener('input', function() {
    const searchQuery = this.value.toLowerCase();
    searchMovies(searchQuery); // Use backend search
});
