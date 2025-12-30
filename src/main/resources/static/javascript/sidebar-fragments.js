// sidebar-fragment.js - Funcionalidad para el sidebar del admin

document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('adminSidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const mainContent = document.querySelector('.main-content');
    
    // Inicializar sidebar
    function initSidebar() {
        // Verificar si estamos en móvil
        const isMobile = window.innerWidth <= 768;
        
        // En móvil, el sidebar empieza oculto
        if (isMobile) {
            sidebar.classList.remove('show');
        } else {
            // En desktop, podemos cargar el estado desde localStorage
            const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
            if (isCollapsed) {
                sidebar.classList.add('collapsed');
                if (mainContent) {
                    mainContent.classList.add('sidebar-collapsed');
                }
            }
        }
    }
    
    // Alternar sidebar (móvil)
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            if (window.innerWidth <= 768) {
                // En móvil, mostrar/ocultar sidebar
                sidebar.classList.toggle('show');
                
                // Crear overlay si no existe
                if (sidebar.classList.contains('show')) {
                    const overlay = document.createElement('div');
                    overlay.className = 'sidebar-overlay';
                    overlay.addEventListener('click', function() {
                        sidebar.classList.remove('show');
                        this.remove();
                    });
                    document.body.appendChild(overlay);
                } else {
                    const overlay = document.querySelector('.sidebar-overlay');
                    if (overlay) overlay.remove();
                }
            } else {
                // En desktop, colapsar/expandir sidebar
                sidebar.classList.toggle('collapsed');
                
                // Guardar estado en localStorage
                const isCollapsed = sidebar.classList.contains('collapsed');
                localStorage.setItem('sidebarCollapsed', isCollapsed);
                
                // Actualizar contenido principal
                if (mainContent) {
                    if (isCollapsed) {
                        mainContent.classList.add('sidebar-collapsed');
                    } else {
                        mainContent.classList.remove('sidebar-collapsed');
                    }
                }
            }
        });
    }
    
    // Cerrar sidebar en móvil al hacer clic en un enlace
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            if (window.innerWidth <= 768) {
                sidebar.classList.remove('show');
                const overlay = document.querySelector('.sidebar-overlay');
                if (overlay) overlay.remove();
            }
        });
    });
    
    // Manejar redimensionamiento de ventana
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768) {
            // En desktop, asegurar que el sidebar esté visible
            sidebar.classList.remove('show');
            const overlay = document.querySelector('.sidebar-overlay');
            if (overlay) overlay.remove();
        } else {
            // En móvil, asegurar que no esté colapsado
            sidebar.classList.remove('collapsed');
        }
        initSidebar();
    });
    
    // Inicializar sidebar
    initSidebar();
    
    // Prevenir scroll horizontal en toda la página
    document.body.classList.add('no-horizontal-scroll');
});