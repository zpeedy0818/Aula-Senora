// Global App Logic for Aula Señora
document.addEventListener('DOMContentLoaded', () => {

    // Initialize AOS (Animate On Scroll)
    if (typeof AOS !== 'undefined') {
        AOS.init({
            duration: 800,
            easing: 'ease-out-cubic',
            once: true,
            offset: 50
        });
    }

    // Mobile Navigation Toggle
    const mobileMenuBtn = document.getElementById('mobile-menu-btn');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuBtn && mobileMenu) {
        mobileMenuBtn.addEventListener('click', () => {
            const isExpanded = mobileMenuBtn.getAttribute('aria-expanded') === 'true';
            mobileMenuBtn.setAttribute('aria-expanded', !isExpanded);
            mobileMenu.classList.toggle('hidden');
        });
    }

    // Accessible Dropdowns (if needed)
    const dropdowns = document.querySelectorAll('.dropdown');
    dropdowns.forEach(dropdown => {
        const toggle = dropdown.querySelector('.dropdown-toggle');
        const menu = dropdown.querySelector('.dropdown-menu');

        if (toggle && menu) {
            toggle.addEventListener('click', (e) => {
                e.preventDefault();
                const isExpanded = toggle.getAttribute('aria-expanded') === 'true';
                toggle.setAttribute('aria-expanded', !isExpanded);
                menu.classList.toggle('hidden');
            });

            // Close when clicking outside
            document.addEventListener('click', (e) => {
                if (!dropdown.contains(e.target)) {
                    toggle.setAttribute('aria-expanded', 'false');
                    menu.classList.add('hidden');
                }
            });
        }
    });
});

// Global Toastify Utility Functions
window.showToast = function(message, type = 'info') {
    if (typeof Toastify !== 'undefined') {
        let className = 'modern-toast modern-toast-info';
        if (type === 'success') className = 'modern-toast modern-toast-success';
        if (type === 'error') className = 'modern-toast modern-toast-error';

        Toastify({
            text: message,
            duration: 4000,
            close: true,
            gravity: "bottom", // `top` or `bottom`
            position: "right", // `left`, `center` or `right`
            stopOnFocus: true, // Prevents dismissing of toast on hover
            className: className,
            style: {
                background: "" // Override Toastify's default inline background to use our CSS classes
            }
        }).showToast();
    } else {
        // Fallback
        alert(message);
    }
};
