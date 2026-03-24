// Mobile menu toggle functionality
document.addEventListener('DOMContentLoaded', function() {
    const menuBtn = document.getElementById('mobile-menu-btn');
    const nav = document.querySelector('.nav');
    
    // Create overlay if it doesn't exist
    let navOverlay = document.getElementById('nav-overlay');
    if (!navOverlay) {
        navOverlay = document.createElement('div');
        navOverlay.className = 'nav-overlay';
        navOverlay.id = 'nav-overlay';
        document.body.appendChild(navOverlay);
    }

    if (menuBtn) {
        // Toggle menu
        menuBtn.addEventListener('click', function() {
            menuBtn.classList.toggle('active');
            nav.classList.toggle('active');
            navOverlay.classList.toggle('active');
        });

        // Close menu when clicking overlay
        navOverlay.addEventListener('click', function() {
            menuBtn.classList.remove('active');
            nav.classList.remove('active');
            navOverlay.classList.remove('active');
        });

        // Close menu when clicking a nav link
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', function() {
                menuBtn.classList.remove('active');
                nav.classList.remove('active');
                navOverlay.classList.remove('active');
            });
        });
    }
});

