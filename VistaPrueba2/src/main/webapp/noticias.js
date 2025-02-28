document.addEventListener("DOMContentLoaded", function() {
  // Elemento para el loader
  const loader = document.getElementById('loader');

  // Realizar la petición al endpoint de la API para obtener todas las noticias
  fetch('/VistaPrueba2/mostrarNoticias', {
    headers: { "X-Requested-With": "XMLHttpRequest" }
  })
  .then(response => response.json())
  .then(data => {
    console.log("Noticias recibidas:", data);

    // Ocultar el loader una vez recibidos los datos
    if (loader) {
      loader.style.display = "none";
    }

    // Ordenar las noticias por fechaPublicacion (las más recientes primero)
    const noticiasOrdenadas = data.sort((a, b) => new Date(b.fechaPublicacion) - new Date(a.fechaPublicacion));

    // Seleccionar las 3 últimas noticias para el carrusel
    const ultimasTres = noticiasOrdenadas.slice(0, 3);

    // Construir los slides del carrusel
    const carouselContainer = document.getElementById('carousel-inner');
    if (carouselContainer) {
      let slidesHtml = "";
      ultimasTres.forEach((noticia, index) => {
        const activeClass = index === 0 ? 'active' : '';
        const imagen = noticia.imagenNoticia ? `data:image/png;base64,${noticia.imagenNoticia}` : 'default.png';
        slidesHtml += `
          <div class="carousel-item ${activeClass}">
            <img src="${imagen}" class="d-block w-100" alt="${noticia.titulo}">
            <div class="carousel-caption d-none d-md-block">
              <h5>${noticia.titulo}</h5>
              <p>${noticia.contenido}</p>
              ${noticia.fechaPublicacion ? `<p><small>${new Date(noticia.fechaPublicacion).toLocaleDateString()}</small></p>` : ""}
            </div>
          </div>
        `;
      });
      carouselContainer.innerHTML = slidesHtml;
    }

    // Construir las tarjetas para todas las noticias
    const container = document.getElementById('noticias-container');
    if (container) {
      let cardsHtml = "";
      noticiasOrdenadas.forEach(function(noticia) {
        const imagen = noticia.imagenNoticia ? `data:image/png;base64,${noticia.imagenNoticia}` : 'default.png';
        cardsHtml += `
          <div class="col-md-3 mb-4">
            <div class="card" style="width: 18rem; height: 350px;">
              <img src="${imagen}" class="card-img-top" alt="Imagen de noticia">
              <div class="card-body">
                <h5 class="card-title">${noticia.titulo}</h5>
                <p class="card-text">${noticia.contenido}</p>
                ${noticia.fechaPublicacion ? `<p class="card-text"><small class="text-muted">${noticia.fechaPublicacion}</small></p>` : ""}
              </div>
            </div>
          </div>
        `;
      });
      container.innerHTML = cardsHtml;
    }
  })
  .catch(function(error) {
    console.error('Error al cargar las noticias:', error);
    if (loader) {
      loader.style.display = "none";
    }
    const container = document.getElementById('noticias-container');
    if (container) {
      container.innerHTML = '<div class="col-12"><p class="text-center">Error al cargar las noticias.</p></div>';
    }
  });
});
