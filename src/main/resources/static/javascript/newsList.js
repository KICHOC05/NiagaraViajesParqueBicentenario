/**
 * Scripts simplificados para la gestión de noticias
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips
    initTooltips();
    
    // Hacer la tabla responsive
    initResponsiveTable();
    
    // Ajustar layout según sidebar
    adjustLayout();
});

/**
 * Inicializar tooltips de Bootstrap
 */
function initTooltips() {
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    if (tooltips.length > 0) {
        tooltips.forEach(el => {
            new bootstrap.Tooltip(el);
        });
    }
}

/**
 * Hacer la tabla responsive en móviles
 */
function initResponsiveTable() {
    function adjustTable() {
        const table = document.querySelector('.table');
        if (!table) return;
        
        if (window.innerWidth < 768) {
            // En móviles, simplificar la tabla
            const headers = table.querySelectorAll('th');
            const rows = table.querySelectorAll('tbody tr');
            
            // Ocultar columnas menos importantes
            [2, 3, 4].forEach(index => {
                if (headers[index]) headers[index].classList.add('d-none');
                rows.forEach(row => {
                    const cell = row.querySelectorAll('td')[index];
                    if (cell) cell.classList.add('d-none');
                });
            });
            
            // Hacer botones más pequeños
            const actionBtns = document.querySelectorAll('.btn-action');
            actionBtns.forEach(btn => {
                btn.classList.add('btn-sm');
            });
        } else {
            // En desktop, mostrar todo
            const headers = table.querySelectorAll('th');
            const rows = table.querySelectorAll('tbody tr');
            
            headers.forEach(header => header.classList.remove('d-none'));
            rows.forEach(row => {
                row.querySelectorAll('td').forEach(cell => {
                    cell.classList.remove('d-none');
                });
            });
            
            const actionBtns = document.querySelectorAll('.btn-action');
            actionBtns.forEach(btn => {
                btn.classList.remove('btn-sm');
            });
        }
    }
    
    // Ajustar al cargar y al redimensionar
    adjustTable();
    window.addEventListener('resize', adjustTable);
}

/**
 * Ajustar layout según estado del sidebar
 */
function adjustLayout() {
    function updateMargin() {
        const sidebar = document.querySelector('.sidebar');
        const mainContent = document.querySelector('.main-content');
        
        if (sidebar && mainContent) {
            const isCollapsed = sidebar.classList.contains('collapsed');
            
            if (window.innerWidth <= 768) {
                mainContent.style.marginLeft = '0';
            } else {
                mainContent.style.marginLeft = isCollapsed ? '70px' : '250px';
            }
        }
    }
    
    updateMargin();
    window.addEventListener('resize', updateMargin);
    
    // Si hay toggle del sidebar, actualizar margen
    const sidebarToggle = document.querySelector('[data-bs-toggle="sidebar"]');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            setTimeout(updateMargin, 300);
        });
    }
}

/**
 * Función para filtrar noticias (opcional)
 */
function filterNews(filterType) {
    const rows = document.querySelectorAll('.table tbody tr');
    
    rows.forEach(row => {
        if (filterType === 'all') {
            row.style.display = '';
        } else if (filterType === 'active') {
            const isActive = row.querySelector('.badge-success');
            row.style.display = isActive ? '' : 'none';
        } else if (filterType === 'inactive') {
            const isInactive = row.querySelector('.badge-secondary');
            row.style.display = isInactive ? '' : 'none';
        } else if (filterType === 'featured') {
            const isFeatured = row.querySelector('.badge-warning');
            row.style.display = isFeatured ? '' : 'none';
        }
    });
}

/**
 * Mostrar notificación simple
 */
function showToast(message, type = 'success') {
    // Crear elemento toast
    const toast = document.createElement('div');
    toast.className = `position-fixed bottom-0 end-0 m-3 p-3 rounded border-0 text-white bg-${type}`;
    toast.style.zIndex = '1060';
    toast.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="fas fa-${type === 'success' ? 'check' : 'exclamation'}-circle me-2"></i>
            <span>${message}</span>
            <button type="button" class="btn-close btn-close-white ms-3" onclick="this.parentElement.parentElement.remove()"></button>
        </div>
    `;
    
    document.body.appendChild(toast);
    
    // Auto-remover después de 3 segundos
    setTimeout(() => {
        if (toast.parentNode) {
            toast.remove();
        }
    }, 3000);
}

/**
 * Confirmación mejorada para eliminación
 */
function confirmDelete(newsTitle = 'esta noticia') {
    return confirm(`¿Está seguro de eliminar ${newsTitle}?\n\nEsta acción marcará la noticia como inactiva.`);
}