// main.js - Versión simplificada y funcional

document.addEventListener('DOMContentLoaded', function() {
    console.log('=== NIAGARA VIAJES - Inicializando ===');
    
    // 1. INICIALIZAR AOS PARA ANIMACIONES
    if (typeof AOS !== 'undefined') {
        AOS.init({
            duration: 800,
            once: true,
            offset: 100
        });
    }
    
    // 2. BARRA DE CARGA
    const loadingBar = document.getElementById('loadingBar');
    if (loadingBar) {
        setTimeout(() => loadingBar.style.transform = 'scaleX(1)', 100);
        setTimeout(() => {
            loadingBar.style.transform = 'scaleX(0)';
            setTimeout(() => loadingBar.style.display = 'none', 300);
        }, 1000);
    }
    
    // 3. CONFIGURAR BOTONES "VER DETALLES" - SOLUCIÓN DEFINITIVA
    console.log('Configurando botones "Ver Detalles"...');
    
    // Esperar a que Thymeleaf termine de renderizar
    setTimeout(() => {
        const verDetallesButtons = document.querySelectorAll('[data-bs-toggle="modal"]');
        console.log(`Encontrados ${verDetallesButtons.length} botones "Ver Detalles"`);
        
        verDetallesButtons.forEach(button => {
            // Agregar evento de clic MANUALMENTE
            button.addEventListener('click', function(event) {
                console.log('Botón "Ver Detalles" clickeado');
                
                // Obtener datos del botón
                const titulo = this.getAttribute('data-package-title');
                const precio = this.getAttribute('data-package-price');
                const dias = this.getAttribute('data-package-days');
                const noches = this.getAttribute('data-package-nights');
                const personas = this.getAttribute('data-package-people');
                const descripcion = this.getAttribute('data-package-description');
                const imagen = this.getAttribute('data-package-image');
                
                console.log('Datos obtenidos:', {
                    titulo, precio, dias, noches, personas, descripcion, imagen
                });
                
                // ACTUALIZAR EL MODAL CON LOS DATOS
                actualizarModalPaquete({
                    titulo: titulo || 'Sin título',
                    precio: precio || '0',
                    dias: dias || '0',
                    noches: noches || '0',
                    personas: personas || '0',
                    descripcion: descripcion || 'Sin descripción disponible',
                    imagen: imagen || ''
                });
            });
        });
    }, 300); // Pequeño delay para asegurar que Thymeleaf terminó
    
    // 4. CONFIGURAR BOTONES "RESERVAR" EN TARJETAS
    console.log('Configurando botones "Reservar"...');
    
    setTimeout(() => {
        const reservarButtons = document.querySelectorAll('.reservar-btn');
        console.log(`Encontrados ${reservarButtons.length} botones "Reservar"`);
        
        reservarButtons.forEach(button => {
            button.addEventListener('click', function() {
                const titulo = this.getAttribute('data-package-title') || 'Paquete de viaje';
                const precio = this.getAttribute('data-package-price') || '0';
                
                console.log('Reservando:', titulo);
                
                // Abrir WhatsApp con mensaje
                abrirWhatsApp(`¡Hola! Estoy interesado en reservar el paquete:\n\n*${titulo}*\nPrecio: ${formatearPrecio(precio)}\n\nMe gustaría recibir más información.`);
            });
        });
    }, 300);
    
    // 5. CONFIGURAR BOTÓN DE WHATSAPP EN EL MODAL
    const whatsappBtnModal = document.querySelector('.whatsapp-btn-enhanced');
    if (whatsappBtnModal) {
        whatsappBtnModal.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const titulo = document.getElementById('modalPackageTitle')?.textContent || 'Paquete de viaje';
            const precio = document.getElementById('modalPackagePrice')?.textContent || '$0.00';
            
            console.log('WhatsApp desde modal:', titulo);
            
            abrirWhatsApp(`¡Hola! Estoy interesado en reservar el paquete:\n\n*${titulo}*\nPrecio: ${precio}\n\nMe gustaría recibir más información y proceder con la reserva.`);
        });
    }
    
    // 6. CONFIGURAR FORMULARIO DE CONTACTO
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const nombre = document.getElementById('nombre')?.value || '';
            const telefono = document.getElementById('telefono')?.value || '';
            const email = document.getElementById('email')?.value || '';
            const mensaje = document.getElementById('mensaje')?.value || '';
            
            const texto = `*NUEVO CONTACTO - NIAGARA VIAJES*%0A%0A` +
                         `*Nombre:* ${nombre}%0A` +
                         `*Teléfono:* ${telefono}%0A` +
                         `*Email:* ${email}%0A` +
                         `*Mensaje:* ${mensaje}%0A%0A` +
                         `_Contacto desde sitio web_`;
            
            abrirWhatsApp(texto);
            
            // Limpiar formulario
            contactForm.reset();
            alert('¡Gracias por contactarnos! Se abrirá WhatsApp para enviar tu mensaje.');
        });
    }
    
    // 7. SCROLL SUAVE PARA ENLACES INTERNOS
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            const href = this.getAttribute('href');
            if (href && href !== '#') {
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    window.scrollTo({
                        top: target.offsetTop - 80,
                        behavior: 'smooth'
                    });
                }
            }
        });
    });
    
    // 8. CARRUSEL DE PROVEEDORES
    const providerCarouselElement = document.getElementById('providerCarousel');
    if (providerCarouselElement && typeof bootstrap !== 'undefined') {
        new bootstrap.Carousel(providerCarouselElement, {
            interval: 3000,
            wrap: true
        });
    }
    
    console.log('=== Configuración completada ===');
});

// ============================================
// FUNCIONES DE UTILIDAD
// ============================================

// Función para actualizar el modal con datos del paquete
function actualizarModalPaquete(datos) {
    console.log('Actualizando modal con:', datos);
    
    // Actualizar cada elemento del modal
    const elementos = {
        'modalPackageTitle': datos.titulo,
        'modalPackagePrice': formatearPrecio(datos.precio),
        'modalPackageDays': datos.dias,
        'modalPackageNights': datos.noches,
        'modalPackagePeople': datos.personas,
        'modalPackageDescription': datos.descripcion
    };
    
    // Actualizar texto de elementos
    for (const [id, valor] of Object.entries(elementos)) {
        const elemento = document.getElementById(id);
        if (elemento) {
            elemento.textContent = valor;
            console.log(`Elemento ${id} actualizado: ${valor}`);
        }
    }
    
    // Actualizar imagen
    const imagenElemento = document.getElementById('modalPackageImage');
    if (imagenElemento) {
        if (datos.imagen && datos.imagen.trim() !== '' && datos.imagen !== 'null') {
            imagenElemento.src = datos.imagen;
            console.log('Imagen actualizada:', datos.imagen);
        } else {
            imagenElemento.src = 'https://via.placeholder.com/800x400/1e40af/ffffff?text=Imagen+No+Disponible';
            console.log('Imagen por defecto establecida');
        }
    }
    
    console.log('Modal actualizado exitosamente');
}

// Función para formatear precios
function formatearPrecio(precio) {
    try {
        const numero = parseFloat(precio);
        if (isNaN(numero)) return '$0.00';
        
        return numero.toLocaleString('es-MX', {
            style: 'currency',
            currency: 'MXN',
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    } catch (error) {
        return '$0.00';
    }
}

// Función para abrir WhatsApp
function abrirWhatsApp(mensaje) {
    // NÚMERO DE WHATSAPP - ¡REEMPLAZA ESTE NÚMERO CON EL DE TU AGENCIA!
    const numeroWhatsApp = '521234567890'; // Ejemplo: 5215512345678
    
    // Codificar mensaje para URL
    const mensajeCodificado = encodeURIComponent(mensaje);
    
    // Crear URL de WhatsApp
    const urlWhatsApp = `https://wa.me/${numeroWhatsApp}?text=${mensajeCodificado}`;
    
    console.log('Abriendo WhatsApp:', urlWhatsApp);
    
    // Abrir en nueva pestaña
    window.open(urlWhatsApp, '_blank');
}

// Función global para debug (opcional)
window.debugPaquetes = function() {
    console.log('=== DEBUG PAQUETES ===');
    
    // Mostrar todos los botones con datos
    const botones = document.querySelectorAll('[data-package-title]');
    console.log(`Total botones: ${botones.length}`);
    
    botones.forEach((btn, i) => {
        console.log(`Botón ${i}:`, {
            clase: btn.className,
            titulo: btn.getAttribute('data-package-title'),
            precio: btn.getAttribute('data-package-price'),
            dias: btn.getAttribute('data-package-days'),
            tieneEvento: btn.hasAttribute('onclick')
        });
    });
    
    // Mostrar elementos del modal
    console.log('Elementos del modal:');
    const elementosModal = [
        'modalPackageTitle',
        'modalPackagePrice', 
        'modalPackageDays',
        'modalPackageNights',
        'modalPackagePeople',
        'modalPackageDescription',
        'modalPackageImage'
    ];
    
    elementosModal.forEach(id => {
        const elem = document.getElementById(id);
        console.log(`  ${id}:`, elem ? 'EXISTE' : 'NO EXISTE');
    });
};95