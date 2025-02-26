document.addEventListener("DOMContentLoaded", function() {
  // Si tu aplicación no está en el contexto raíz, ajusta la ruta en el fetch.
  fetch('/VistaPrueba2/mostrarNoticias', {
    headers: { "X-Requested-With": "XMLHttpRequest" }
  })
  .then(response => response.json())
  .then(data => {
    console.log("Noticias recibidas:", data);
    const container = document.getElementById('noticias-container');
    if (data && data.length > 0) {
      data.forEach(function(noticia) {
        // Utilizamos col-md-3 para 4 columnas en dispositivos medianos y superiores
        const col = document.createElement('div');
        col.className = 'col-md-3 mb-4';
        col.innerHTML = `
          <div class="card" style="width: 18rem; height: 350px;">
            ${noticia.imagenNoticia 
              ? `<img src="data:image/png;base64,${noticia.imagenNoticia}" class="card-img-top" alt="Imagen de noticia">`
              : `<img src="default.png" class="card-img-top" alt="Imagen por defecto">`
            }
            <div class="card-body">
              <h5 class="card-title">${noticia.titulo}</h5>
              <p class="card-text">${noticia.contenido}</p>
              ${noticia.fechaPublicacion 
                ? `<p class="card-text"><small class="text-muted">${noticia.fechaPublicacion}</small></p>`
                : ''
              }
            </div>
          </div>
        `;
        container.appendChild(col);
      });
    } else {
      container.innerHTML = '<div class="col-12"><p class="text-center">No hay noticias disponibles.</p></div>';
    }
  })
  .catch(function(error) {
    console.error('Error al cargar las noticias:', error);
    document.getElementById('noticias-container').innerHTML = '<div class="col-12"><p class="text-center">Error al cargar las noticias.</p></div>';
  });
});
