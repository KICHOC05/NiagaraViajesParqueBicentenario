document.addEventListener('DOMContentLoaded', function() {
    // Inicializar AOS
    AOS.init({
        duration: 800,
        once: true,
        offset: 100
    });
    
    // Loading bar animation
    const loadingBar = document.getElementById('loadingBar');
    setTimeout(() => {
        loadingBar.style.transform = 'scaleX(1)';
    }, 100);
    
    setTimeout(() => {
        loadingBar.style.transform = 'scaleX(0)';
        setTimeout(() => {
            loadingBar.style.display = 'none';
        }, 300);
    }, 1000);
    
    // Navbar scroll effect
    const navbar = document.getElementById('mainNavbar');
    if (navbar) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }
    
    // Animación de scroll para elementos fade-in
    const fadeElements = document.querySelectorAll('.fade-in');
    
    const checkFadeElements = () => {
        fadeElements.forEach(element => {
            const elementTop = element.getBoundingClientRect().top;
            const elementVisible = 150;
            
            if (elementTop < window.innerHeight - elementVisible) {
                element.classList.add('visible');
            }
        });
    };
    
    // Verificar elementos al cargar y al hacer scroll
    window.addEventListener('scroll', checkFadeElements);
    checkFadeElements(); // Verificar inicialmente
    
    // Smooth scrolling para anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            
            // Solo aplicar smooth scroll para enlaces dentro de la misma página
            if (href.startsWith('#') && href.length > 1) {
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    window.scrollTo({
                        top: target.offsetTop - 80,
                        behavior: 'smooth'
                    });
                }
            }
            
            // Cerrar el navbar en móviles después de hacer clic en un enlace
            const navbarToggler = document.querySelector('.navbar-toggler');
            const navbarCollapse = document.querySelector('.navbar-collapse');
            
            if (navbarToggler && navbarCollapse && navbarCollapse.classList.contains('show')) {
                navbarToggler.click();
            }
        });
    });
    
    // Preload de imágenes para evitar bugs visuales
    const preloadImages = () => {
        const imageUrls = [
            '/images/galeria/xplor.jpg',
            '/images/galeria/Museo.jpg',
            '/images/galeria/viaje3.jpg',
            '/images/galeria/viaje4.jpg',
            '/images/galeria/viaje5.jpg',
            '/images/galeria/viaje6.jpg',
            '/images/galeria/default-trip.jpg'
        ];
        
        imageUrls.forEach(url => {
            const img = new Image();
            img.src = url;
        });
    };
    
    // Ejecutar preload después de un tiempo
    setTimeout(preloadImages, 1000);
});