
       // Toggle sidebar on mobile
       document.getElementById('sidebarToggle').addEventListener('click', function() {
           document.querySelector('.sidebar').classList.toggle('active');
       });
       
       // Cerrar sidebar al hacer clic en un enlace (mobile)
       document.querySelectorAll('.nav-link').forEach(link => {
           link.addEventListener('click', function() {
               if (window.innerWidth < 992) {
                   document.querySelector('.sidebar').classList.remove('active');
               }
           });
       });

       // Agregar clase active al enlace actual
       document.addEventListener('DOMContentLoaded', function() {
           const currentPath = window.location.pathname;
           document.querySelectorAll('.nav-link').forEach(link => {
               if (link.getAttribute('href') === currentPath) {
                   link.classList.add('active');
               } else {
                   link.classList.remove('active');
               }
           });
       });
