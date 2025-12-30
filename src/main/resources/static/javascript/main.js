// Inicializar AOS inmediatamente (no dentro de DOMContentLoaded)
AOS.init({
    duration: 800,
    once: true,
    offset: 100
});

// FUNCIONES GLOBALES (para onclick)
window.reservarWhatsAppFromModal = function() {
    const packageTitle = document.getElementById('modalPackageTitle').textContent;
    const packagePrice = document.getElementById('modalPackagePrice').textContent;
    
    const numeroWhatsApp = '521234567890';
    const mensaje = `¡Hola! 👋\n\nMe interesa reservar el paquete:\n🏷️ *${encodeURIComponent(packageTitle)}*\n💰 *Desde ${encodeURIComponent(packagePrice)}*\n\nPor favor, envíenme más información sobre disponibilidad y formas de pago. ¡Gracias! ✈️`;
    const urlWhatsApp = `https://wa.me/${numeroWhatsApp}?text=${mensaje}`;
    
    window.open(urlWhatsApp, '_blank');
    
    const modalElement = document.getElementById('packageModal');
    if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement);
        if (modal) {
            modal.hide();
        }
    }
};

window.enviarWhatsApp = function(packageTitle) {
    const numeroWhatsApp = '521234567890';
    const mensaje = `Hola, me interesa el paquete: ${encodeURIComponent(packageTitle)}.`;
    const urlWhatsApp = `https://wa.me/${numeroWhatsApp}?text=${mensaje}`;
    window.open(urlWhatsApp, '_blank');
};

// Configuración cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Loading bar animation
    const loadingBar = document.getElementById('loadingBar');
    if (loadingBar) {
        setTimeout(() => {
            loadingBar.style.transform = 'scaleX(1)';
        }, 100);
        
        setTimeout(() => {
            loadingBar.style.transform = 'scaleX(0)';
            setTimeout(() => {
                loadingBar.style.display = 'none';
            }, 300);
        }, 1000);
    }
    
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
    
    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                window.scrollTo({
                    top: target.offsetTop - 80,
                    behavior: 'smooth'
                });
                
                const navbarToggler = document.querySelector('.navbar-toggler');
                if (navbarToggler && !navbarToggler.classList.contains('collapsed')) {
                    navbarToggler.click();
                }
            }
        });
    });
    
    // Configurar el modal de detalles del paquete
    const packageModal = document.getElementById('packageModal');
    if (packageModal) {
        packageModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            
            const title = button.getAttribute('data-package-title');
            const price = button.getAttribute('data-package-price');
            const days = button.getAttribute('data-package-days');
            const nights = button.getAttribute('data-package-nights');
            const people = button.getAttribute('data-package-people');
            const description = button.getAttribute('data-package-description');
            const image = button.getAttribute('data-package-image');
            
            document.getElementById('modalPackageTitle').textContent = title;
            
            const formattedPrice = parseFloat(price).toLocaleString('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
            document.getElementById('modalPackagePrice').textContent = '$' + formattedPrice;
            
            document.getElementById('modalPackageDays').textContent = days;
            document.getElementById('modalPackageNights').textContent = nights;
            document.getElementById('modalPackagePeople').textContent = people;
            
            let fullDescription = description;
            if (!fullDescription || fullDescription.trim() === '') {
                fullDescription = "¡Un viaje inolvidable te espera! Este paquete incluye todo lo necesario para disfrutar al máximo de tu experiencia de viaje. Con nosotros tendrás la garantía de calidad y atención personalizada que nos caracteriza.";
            }
            document.getElementById('modalPackageDescription').textContent = fullDescription;
            
            const modalImage = document.getElementById('modalPackageImage');
            modalImage.src = image || 'https://via.placeholder.com/800x400/1e40af/ffffff?text=Viaje+Inolvidable';
            
            modalImage.onload = function() {
                modalImage.style.opacity = '1';
            };
            modalImage.style.opacity = '0';
            modalImage.style.transition = 'opacity 0.5s ease';
        });
        
        packageModal.addEventListener('shown.bs.modal', function() {
            AOS.refresh();
        });
    }
    
    // Configurar botones de reserva
    document.querySelectorAll('.reservar-btn').forEach(button => {
        button.addEventListener('click', function() {
            const packageTitle = this.getAttribute('data-package-title');
            enviarWhatsApp(packageTitle);
        });
    });
    
    // Configurar el formulario de contacto
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const nombre = document.getElementById('nombre').value;
            const telefono = document.getElementById('telefono').value;
            const email = document.getElementById('email').value;
            const mensaje = document.getElementById('mensaje').value;
            
            const texto = `Hola, me interesa recibir información sobre viajes.%0A%0ANombre: ${nombre}%0ATeléfono: ${telefono}%0AEmail: ${email}%0AMensaje: ${mensaje}`;
            const numeroWhatsApp = '521234567890';
            const urlWhatsApp = `https://wa.me/${numeroWhatsApp}?text=${texto}`;
            
            window.open(urlWhatsApp, '_blank');
        });
    }
});