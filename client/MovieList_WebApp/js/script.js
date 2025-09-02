const apiKey = 'b9883c72e49ac8961800e206e8f281c7';  // Replace with your TMDb API key
    
        // Genre IDs based on TMDb
        const genres = {
            action: 28,
            comedy: 35,
            drama: 18
        };
    
        let allMovies = [];  // Store all movies fetched from the API
    
        // Function to toggle dark mode
        function toggleDarkMode() {
            const body = document.body;
            const toggle = document.getElementById('toggle');
            const dot = document.querySelector('.dot');
    
            if (toggle.checked) {
                body.classList.add('dark');
                localStorage.setItem('darkMode', 'enabled');
                dot.style.transform = 'translateX(100%)';  // Correcting the dot movement
            } else {
                body.classList.remove('dark');
                localStorage.setItem('darkMode', 'disabled');
                dot.style.transform = 'translateX(0)';  // Correcting the dot movement
            }
        }
        // Check the user's preference on page load
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
    
        // Function to fetch movies based on genre or query
        async function fetchMovies(genre = '', query = '') {
            try {
                let apiUrl;
                if (query) {
                    apiUrl = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query)}&language=en-US&page=1`;
                } else {
                    const genreId = genres[genre];
                    apiUrl = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=${genreId}&language=en-US&page=1`;
                }
    
                const response = await fetch(apiUrl);
                const data = await response.json();
                allMovies = data.results; // Store the fetched movies
                displayMovies(allMovies); // Display the fetched movies
                highlightActiveTab(genre);
            } catch (error) {
                console.error('Error fetching movie data:', error);
            }
        }
        // Function to display movies
        function displayMovies(movies) {
    const movieGrid = document.getElementById('movieGrid');
    movieGrid.innerHTML = ''; // Clear existing movies

    movies.forEach(movie => {
        const movieCard = document.createElement('div');
        movieCard.className = 'movie-card bg-white shadow-md rounded-lg overflow-hidden hover:bg-gray-100 dark:bg-gray-800 dark:hover:bg-gray-700 transition-transform duration-300 transform hover:scale-105';
        
        // Add data attributes for search functionality
        movieCard.setAttribute('data-title', movie.title);
        movieCard.setAttribute('data-description', movie.overview);

        movieCard.innerHTML = `
            <img src="https://image.tmdb.org/t/p/w500${movie.poster_path}" alt="${movie.title}" class="w-full h-64 object-cover">
            <div class="p-4">
                <h3 class="text-lg font-semibold text-gray-900 dark:text-gray-100">${movie.title}</h3>
                <p class="text-gray-700 dark:text-gray-300">Release Year: ${new Date(movie.release_date).getFullYear()}</p>
                <p class="text-gray-700 dark:text-gray-300">Rating: ${movie.vote_average} / 10</p>
                <button onclick="openModal('${escapeQuotes(movie.title)}', 'https://image.tmdb.org/t/p/w500${movie.poster_path}', '${escapeQuotes(movie.overview)}')" class="mt-2 py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-600 transition-colors duration-300">
                    View Details
                </button>

                <button class="mt-2 py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-600 transition-colors duration-300" onclick="addToWatchlist('${escapeQuotes(movie.title)}', '${escapeQuotes(movie.overview)}', '${movie.poster_path}', '${movie.release_date}', '${movie.vote_average}')">
                    <span class="plus">+</span> Watchlist
                </button>
            </div>
        `;
        movieGrid.appendChild(movieCard);
    });
}

// Function to escape quotes in movie titles/overviews
function escapeQuotes(text) {
    return text.replace(/'/g, "\\'");
}

// Add movie to watchlist in localStorage
function addToWatchlist(title, overview, posterPath, releaseDate, voteAverage) {
    const movie = {
        title,
        overview,
        posterPath,
        releaseDate,
        voteAverage
    };

    let watchlist = JSON.parse(localStorage.getItem('watchlist')) || [];

    // Check if the movie is already in the watchlist to avoid duplicates
    const movieExists = watchlist.some(item => item.title === movie.title);
    if (!movieExists) {
        watchlist.push(movie);
        localStorage.setItem('watchlist', JSON.stringify(watchlist));
        alert(`${title} added to your watchlist!`);
    } else {
        alert(`${title} is already in your watchlist.`);
    }
}

// View the watchlist in a modal
function viewWatchlist() {
    const watchlistModal = document.getElementById('watchlistModal');
    const watchlistContainer = document.getElementById('watchlistContainer');
    watchlistContainer.innerHTML = ''; // Clear previous watchlist

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
                    <p class="text-sm text-gray-700 dark:text-gray-300">Rating: ${movie.voteAverage} / 10</p>
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

// Close the watchlist modal
function closeWatchlist() {
    const watchlistModal = document.getElementById('watchlistModal');
    watchlistModal.classList.add('hidden');
}
function removeFromWatchlist(title) {
    let watchlist = JSON.parse(localStorage.getItem('watchlist')) || [];
    watchlist = watchlist.filter(movie => movie.title !== title);
    localStorage.setItem('watchlist', JSON.stringify(watchlist));
    viewWatchlist(); // Refresh watchlist view
}


        // Function to open modal with movie details
        function openModal(title, image, description) {
            document.getElementById('modalTitle').textContent = title;
            document.getElementById('modalImage').src = image;
            document.getElementById('modalDescription').textContent = description;
            document.getElementById('movieModal').classList.remove('hidden');
        }
        // Function to close the modal
        function closeModal() {
            document.getElementById('movieModal').classList.add('hidden');
        }
        //login
        document.getElementById('loginButton').addEventListener('click', login);
        //login fucntion
        async function login() {
            const username = prompt('Enter your username:');
            const password = prompt('Enter your password:');

            // You can add your authentication logic here
            // For this example, I'll assume a simple username and password check
            if (username === 'admin' && password === 'password') {
                // Login successful, redirect to a protected page or update the UI
                alert('Login successful!');
                // You can also store the user's login status in local storage or a cookie
                localStorage.setItem('isLoggedIn', true);
            } else {
                alert('Invalid username or password');
            }
         }
        
        // Function to highlight the active tab
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
    
        // Function to escape quotes in movie titles and descriptions
        function escapeQuotes(string) {
            return string.replace(/'/g, "\\'").replace(/"/g, '\\"');
        }
    
        // Function to search movies
        async function searchMovies(query) {
            try {
                const response = await fetch(`https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query)}&language=en-US&page=1`);
                const data = await response.json();
                allMovies = data.results; // Store the fetched movies
                displayMovies(allMovies); // Display the fetched movies
            } catch (error) {
                console.error('Error fetching movie data:', error);
            }
        }
        // Search bar functionality
        document.getElementById('searchBar').addEventListener('input', function() {
            const searchQuery = this.value.toLowerCase();
            searchMovies(searchQuery); // Search movies based on the query
        });