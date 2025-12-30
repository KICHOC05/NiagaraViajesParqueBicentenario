// header.js - CON FUNCIONALIDAD DE SCROLL
document.addEventListener('DOMContentLoaded', function() {
    const navbar = document.querySelector('.navbar-custom');
    const navbarNav = document.querySelector('.navbar-nav');
    
    if (navbar) {
        // Efecto scroll para navbar
        window.addEventListener('scroll', function() {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
        
        // Inicializar estado de scroll
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        }
        
        // Función para manejar scroll horizontal en desktop
        if (navbarNav && window.innerWidth >= 992) {
            setupHorizontalScroll();
        }
        
        // Redimensionar ventana
        window.addEventListener('resize', function() {
            if (navbarNav) {
                if (window.innerWidth >= 992) {
                    setupHorizontalScroll();
                } else {
                    removeHorizontalScroll();
                }
            }
        });
        
        // Cerrar navbar en móviles al hacer clic
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', function() {
                if (window.innerWidth < 992) {
                    const navbarCollapse = document.querySelector('.navbar-collapse');
                    const navbarToggler = document.querySelector('.navbar-toggler');
                    
                    if (navbarCollapse && navbarCollapse.classList.contains('show')) {
                        navbarCollapse.classList.remove('show');
                        navbarToggler.setAttribute('aria-expanded', 'false');
                    }
                }
            });
        });
    }
    
    // Función para configurar scroll horizontal
    function setupHorizontalScroll() {
        const navbarNav = document.querySelector('.navbar-nav');
        if (!navbarNav) return;
        
        // Verificar si hay overflow
        function checkOverflow() {
            const hasOverflow = navbarNav.scrollWidth > navbarNav.clientWidth;
            
            if (hasOverflow) {
                navbarNav.classList.add('scrollable');
                
                // Añadir indicadores de scroll si no existen
                if (!document.querySelector('.scroll-indicator')) {
                    addScrollIndicators();
                }
            } else {
                navbarNav.classList.remove('scrollable');
                removeScrollIndicators();
            }
            
            // Actualizar clase de inicio
            updateScrollPosition();
        }
        
        // Añadir indicadores de scroll
        function addScrollIndicators() {
            // Indicador izquierdo
            const leftIndicator = document.createElement('div');
            leftIndicator.className = 'scroll-indicator scroll-left d-none';
            leftIndicator.innerHTML = '<i class="fas fa-chevron-left text-primary"></i>';
            leftIndicator.addEventListener('click', function() {
                navbarNav.scrollBy({ left: -150, behavior: 'smooth' });
            });
            navbarNav.parentNode.appendChild(leftIndicator);
            
            // Indicador derecho
            const rightIndicator = document.createElement('div');
            rightIndicator.className = 'scroll-indicator scroll-right';
            rightIndicator.innerHTML = '<i class="fas fa-chevron-right text-primary"></i>';
            rightIndicator.addEventListener('click', function() {
                navbarNav.scrollBy({ left: 150, behavior: 'smooth' });
            });
            navbarNav.parentNode.appendChild(rightIndicator);
        }
        
        // Remover indicadores
        function removeScrollIndicators() {
            document.querySelectorAll('.scroll-indicator').forEach(indicator => {
                indicator.remove();
            });
        }
        
        // Actualizar posición de scroll
        function updateScrollPosition() {
            const leftIndicator = document.querySelector('.scroll-left');
            const rightIndicator = document.querySelector('.scroll-right');
            
            if (!leftIndicator || !rightIndicator) return;
            
            // Mostrar/ocultar indicadores según posición
            if (navbarNav.scrollLeft > 0) {
                leftIndicator.classList.remove('d-none');
                navbarNav.classList.remove('scroll-start');
            } else {
                leftIndicator.classList.add('d-none');
                navbarNav.classList.add('scroll-start');
            }
            
            if (navbarNav.scrollLeft + navbarNav.clientWidth < navbarNav.scrollWidth - 10) {
                rightIndicator.classList.remove('d-none');
            } else {
                rightIndicator.classList.add('d-none');
            }
        }
        
        // Evento de scroll
        navbarNav.addEventListener('scroll', updateScrollPosition);
        
        // Verificar overflow inicial y al redimensionar
        checkOverflow();
        window.addEventListener('resize', checkOverflow);
        
        // También verificar después de que se carguen las fuentes
        window.addEventListener('load', checkOverflow);
    }
    
    // Función para remover funcionalidad de scroll horizontal
    function removeHorizontalScroll() {
        const navbarNav = document.querySelector('.navbar-nav');
        if (navbarNav) {
            navbarNav.classList.remove('scrollable');
        }
        
        // Remover indicadores
        document.querySelectorAll('.scroll-indicator').forEach(indicator => {
            indicator.remove();
        });
    }
});