// createNews.js - Funcionalidades para crear noticias

document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const tituloInput = document.querySelector('input[th\\:field="*{titulo}"]');
    const imagenUrlInput = document.getElementById('imagenUrl');
    const contenidoTextarea = document.getElementById('contenidoNoticia');
    const facebookLinkInput = document.getElementById('facebookLink');
    const facebookTextarea = document.getElementById('facebookText');
    const destacadaSwitch = document.getElementById('destacadaSwitch');
    const activaSwitch = document.getElementById('activaSwitch');
    const charCountSpan = document.getElementById('charCount');
    const previewContent = document.getElementById('previewContent');
    const form = document.querySelector('form');

    // Imagen por defecto
    const defaultImage = '/images/noticias/default-news.jpg';

    // Contador de caracteres
    function updateCharCount() {
        const length = contenidoTextarea.value.length;
        charCountSpan.textContent = length;
        
        // Cambiar color si se acerca al límite
        if (length > 1000) {
            charCountSpan.classList.add('warning');
        } else {
            charCountSpan.classList.remove('warning');
        }
    }

    // Generar vista previa
    function generatePreview() {
        const titulo = tituloInput.value.trim() || 'Título de la Noticia';
        const imagenUrl = imagenUrlInput.value.trim() || defaultImage;
        const contenido = contenidoTextarea.value.trim() || 'Contenido de la noticia aparecerá aquí...';
        const facebookText = facebookTextarea.value.trim();
        const facebookLink = facebookLinkInput.value.trim();
        const esDestacada = destacadaSwitch.checked;
        const esActiva = activaSwitch.checked;

        // Crear HTML de vista previa
        let previewHTML = `
            <div class="preview-noticia">
                <img src="${imagenUrl}" alt="Imagen de noticia" 
                     onerror="this.src='${defaultImage}'" class="mb-3">
                
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <h4>${titulo}</h4>
                    <div>
                        ${esDestacada ? '<span class="status-indicator status-featured"><i class="fas fa-star"></i> Destacada</span>' : ''}
                        ${!esActiva ? '<span class="status-indicator status-draft"><i class="fas fa-eye-slash"></i> Borrador</span>' : ''}
                        ${esActiva ? '<span class="status-indicator status-active"><i class="fas fa-eye"></i> Publicada</span>' : ''}
                    </div>
                </div>
                
                <p>${contenido.substring(0, 200)}${contenido.length > 200 ? '...' : ''}</p>
        `;

        // Agregar sección de Facebook si hay enlace o texto
        if (facebookLink || facebookText) {
            previewHTML += `
                <div class="facebook-badge">
                    <div class="d-flex align-items-center mb-2">
                        <i class="fab fa-facebook fa-lg"></i>
                        <span class="fw-bold">Publicación en Facebook</span>
                    </div>
            `;
            
            if (facebookText) {
                previewHTML += `<p class="mb-2">${facebookText.substring(0, 150)}${facebookText.length > 150 ? '...' : ''}</p>`;
            }
            
            if (facebookLink) {
                previewHTML += `<small class="text-muted">Enlace: ${facebookLink.substring(0, 50)}...</small>`;
            }
            
            previewHTML += `</div>`;
        }

        previewHTML += `</div>`;
        
        // Actualizar vista previa
        previewContent.innerHTML = previewHTML;
    }

    // Auto-generar texto de Facebook si hay enlace
    function autoGenerateFacebookText() {
        if (facebookLinkInput.value && !facebookTextarea.value) {
            // Extraer información básica del enlace para sugerir texto
            const link = facebookLinkInput.value;
            if (tituloInput.value) {
                facebookTextarea.value = `📢 ¡Nueva noticia! ${tituloInput.value}\n\nMás información en nuestro sitio web. 👇\n\n#NiagaraViajes #NoticiasViajes`;
            }
        }
    }

    // Sugerir imagen basada en el título
    function suggestImage() {
        if (!imagenUrlInput.value && tituloInput.value) {
            // Aquí podrías integrar con una API de imágenes
            // Por ahora solo mostramos un placeholder
            imagenUrlInput.value = '';
        }
    }

    // Validar formulario antes de enviar
    function validateForm(e) {
        const titulo = tituloInput.value.trim();
        const contenido = contenidoTextarea.value.trim();
        
        if (!titulo) {
            e.preventDefault();
            showAlert('Por favor ingrese un título para la noticia', 'warning');
            tituloInput.focus();
            return false;
        }
        
        if (!contenido) {
            e.preventDefault();
            showAlert('Por favor ingrese el contenido de la noticia', 'warning');
            contenidoTextarea.focus();
            return false;
        }
        
        if (contenido.length < 50) {
            e.preventDefault();
            showAlert('El contenido debe tener al menos 50 caracteres', 'warning');
            contenidoTextarea.focus();
            return false;
        }
        
        // Validar URL de imagen si se proporciona
        if (imagenUrlInput.value && !isValidUrl(imagenUrlInput.value)) {
            e.preventDefault();
            showAlert('Por favor ingrese una URL válida para la imagen', 'warning');
            imagenUrlInput.focus();
            return false;
        }
        
        // Validar URL de Facebook si se proporciona
        if (facebookLinkInput.value && !isValidUrl(facebookLinkInput.value)) {
            e.preventDefault();
            showAlert('Por favor ingrese una URL válida para Facebook', 'warning');
            facebookLinkInput.focus();
            return false;
        }
        
        return true;
    }

    // Función auxiliar para validar URLs
    function isValidUrl(string) {
        try {
            new URL(string);
            return true;
        } catch (_) {
            return false;
        }
    }

    // Mostrar alerta temporal
    function showAlert(message, type = 'info') {
        const alertClass = {
            'info': 'alert-info',
            'warning': 'alert-warning',
            'success': 'alert-success',
            'danger': 'alert-danger'
        }[type];
        
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert ${alertClass} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            <i class="fas fa-info-circle me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        // Insertar al inicio del formulario
        const cardBody = document.querySelector('.card-body');
        cardBody.insertBefore(alertDiv, cardBody.firstChild);
        
        // Auto-eliminar después de 5 segundos
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.classList.remove('show');
                setTimeout(() => alertDiv.remove(), 150);
            }
        }, 5000);
    }

    // Event Listeners
    tituloInput.addEventListener('input', () => {
        generatePreview();
        suggestImage();
    });
    
    imagenUrlInput.addEventListener('input', generatePreview);
    contenidoTextarea.addEventListener('input', () => {
        updateCharCount();
        generatePreview();
    });
    
    facebookLinkInput.addEventListener('input', () => {
        autoGenerateFacebookText();
        generatePreview();
    });
    
    facebookTextarea.addEventListener('input', generatePreview);
    destacadaSwitch.addEventListener('change', generatePreview);
    activaSwitch.addEventListener('change', generatePreview);
    
    // Validar formulario al enviar
    form.addEventListener('submit', validateForm);
    
    // Inicializar
    updateCharCount();
    generatePreview();
    
    // Agregar tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Función para obtener vista previa de imagen
    imagenUrlInput.addEventListener('blur', function() {
        if (this.value) {
            // Pre-cargar imagen para verificar que existe
            const img = new Image();
            img.src = this.value;
            img.onload = function() {
                console.log('Imagen cargada correctamente');
            };
            img.onerror = function() {
                showAlert('No se pudo cargar la imagen desde la URL proporcionada', 'warning');
            };
        }
    });

    // Auto-guardado cada 30 segundos (opcional)
    let autoSaveInterval;
    function startAutoSave() {
        autoSaveInterval = setInterval(() => {
            if (tituloInput.value || contenidoTextarea.value) {
                console.log('Auto-guardando...');
                // Aquí podrías implementar auto-guardado real
            }
        }, 30000);
    }

    // Iniciar auto-guardado
    startAutoSave();
    
    // Limpiar intervalo al salir
    window.addEventListener('beforeunload', function() {
        clearInterval(autoSaveInterval);
    });
});