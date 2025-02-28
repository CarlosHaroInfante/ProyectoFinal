<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>El Pase Sevillano</title>
    <!-- Bootstrap CSS (versión 4.5.2) -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- AlpineJS -->
    <script src="//unpkg.com/alpinejs" defer></script>
    <!-- TailwindCSS (opcional) -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
      /* Quitar márgenes y padding por defecto */
      html, body { margin: 0; padding: 0; }
      /* Estilos para el submenú */
      .submenu { top: 100%; margin-top: 0; }
      .submenu a:hover { background-color: #f0f0f0; }
      
      /* Personalización para mostrar 4 columnas con menor espaciado y tarjetas de tamaño uniforme */
      .row > [class*="col-"] {
        padding: 0.5rem; /* Espaciado reducido */
      }
      .card {
        height: 500px; /* Aumentamos la altura de la tarjeta */
        display: flex;
        flex-direction: column;
      }
      .card-img-top {
        height: 200px; /* Aumentamos la altura de la imagen */
        object-fit: cover;
      }
      .card-body {
        flex-grow: 1;
        overflow: hidden;
      }
	    #carousel-inner .carousel-item img {
	    width: 100%;          /* Ancho completo del contenedor */
	    height: 300px;        /* Altura fija, ajusta este valor según tus necesidades */
	    object-fit: cover;    /* Recorta la imagen manteniendo la proporción */
	  }
	  
	  /* Opcional: Si usas Flowbite o Bootstrap, puedes ajustar los estilos de los slides */
	  .carousel-item {
	    height: 300px;       /* Altura fija para cada slide */
	  }
    </style>
  </head>
  <body>
    <!-- Barra de navegación (igual que antes) -->
    <nav class="bg-[#F1C158] text-black">
      <div class="max-w-screen-xl mx-auto px-4 py-3 grid grid-cols-3 items-center">
        <!-- Columna izquierda: Logo -->
        <div class="flex items-center h-10 relative overflow-visible">
          <a href="#">
            <img id="logoImg" src="img/cabecera-removebg-preview.png" alt="Logo" style="height: 10rem; position: absolute; top: 50%; transform: translate(-50%, -50%);" class="object-contain">
          </a>
        </div>
        <!-- Columna central: Menú -->
        <div class="flex justify-center space-x-8">
          <div class="relative group">
            <a href="#" class="hover:underline font-medium inline-block">Noticias</a>
            <div class="submenu absolute left-0 w-40 bg-white shadow-lg hidden group-hover:block z-50">
              <a href="#" class="block px-4 py-2 hover:underline">Sevilla</a>
              <a href="#" class="block px-4 py-2 hover:underline">Betis</a>
            </div>
          </div>
          <div class="relative group">
            <a href="#" class="hover:underline font-medium inline-block">Clasificación</a>
            <div class="submenu absolute left-0 w-40 bg-white shadow-lg hidden group-hover:block z-50">
              <a href="#" class="block px-4 py-2 hover:underline">Sevilla</a>
              <a href="#" class="block px-4 py-2 hover:underline">Betis</a>
            </div>
          </div>
          <div class="relative group">
            <a href="#" class="hover:underline font-medium inline-block">Fichajes</a>
            <div class="submenu absolute left-0 w-40 bg-white shadow-lg hidden group-hover:block z-50">
              <a href="#" class="block px-4 py-2 hover:underline">Sevilla</a>
              <a href="#" class="block px-4 py-2 hover:underline">Betis</a>
            </div>
          </div>
        </div>
        <!-- Columna derecha: Sesión -->
        <div class="flex justify-end items-center space-x-4">
          <%
            String usuarioLogueado = (String) session.getAttribute("usuarioLogueado");
            String imagenUsuario = (String) session.getAttribute("imagenUsuario");
            if (usuarioLogueado == null) {
          %>
            <a href="InicioSesion.html" class="bg-black text-white px-3 py-2 rounded hover:opacity-90 transition">Iniciar Sesión</a>
          <%
            } else {
              if (imagenUsuario != null && !imagenUsuario.isEmpty()) {
          %>
            <img src="data:image/png;base64,<%= imagenUsuario %>" alt="Foto de perfil" class="w-10 h-10 rounded-full">
          <%
              } else {
          %>
            <span class="font-medium">Bienvenido/a: <strong><%= usuarioLogueado %></strong></span>
          <%
              }
              if ("Admin".equalsIgnoreCase((String) session.getAttribute("rol"))) {
          %>
            <a href="<%= request.getContextPath() %>/Admin.html" class="bg-black text-white px-3 py-2 rounded hover:opacity-90 transition">Admin</a>
          <%
              }
          %>
            <a href="<%= request.getContextPath() %>/logout" class="bg-black text-white px-3 py-2 rounded hover:opacity-90 transition">Cerrar Sesión</a>
          <%
            }
          %>
        </div>
      </div>
    </nav>

    <br /><br />

    <!-- Carrusel (opcional) -->
    <h2 class="text-3xl font-bold text-center sm:text-5xl">Últimas Noticias</h2>
	<br /><br />
	<div class="max-w-2xl mx-auto">
	  <div id="default-carousel" class="carousel slide" data-ride="carousel">
	    <div id="carousel-inner" class="carousel-inner">
	      <!-- Los slides se inyectarán dinámicamente -->
	    </div>
	    <a class="carousel-control-prev" href="#default-carousel" role="button" data-slide="prev">
	      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
	      <span class="sr-only">Anterior</span>
	    </a>
	    <a class="carousel-control-next" href="#default-carousel" role="button" data-slide="next">
	      <span class="carousel-control-next-icon" aria-hidden="true"></span>
	      <span class="sr-only">Siguiente</span>
	    </a>
	  </div>
	  <!-- Indicadores del carrusel -->
	  <div class="flex absolute bottom-5 left-1/2 z-30 -translate-x-1/2 space-x-2" data-carousel-indicators>
	    <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400"></button>
	    <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400"></button>
	    <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400"></button>
	  </div>
	</div>
      <script src="https://unpkg.com/flowbite@1.4.0/dist/flowbite.js"></script>
    </div>

    <br /><br /><br />

    <!-- Sección "Todas las Noticias" -->
	<h2 class="text-3xl font-bold text-center sm:text-5xl">Todas las Noticias</h2>
	<br>
	<!-- Aquí limitamos el ancho máximo y centramos con margin: 0 auto -->
	<div class="container mt-4" style="max-width: 1200px; margin: 0 auto;">
	  <div class="row" id="noticias-container">
	    <!-- Aquí se cargarán las tarjetas de noticias dinámicamente -->
	  </div>
	</div>

    <br /><br /><br />

    <!-- Footer -->
    <footer class="bg-black text-white w-full">
      <div id="footerContent" class="max-w-7xl mx-auto px-4">
        <div class="grid lg:grid-cols-3 grid-cols-1 gap-4 pt-12">
          <!-- Columna 1: Información sobre Nosotros -->
          <div>
            <h2 class="text-white text-3xl font-semibold mb-8">Información sobre Nosotros</h2>
            <p>Un periódico centrado en el fútbol sevillano, con las últimas noticias, post-partidos, alineaciones, fichajes y ... ¡mucho más!</p>
            <div class="flex gap-2 items-center text-2xl text-white mt-6">
              <div class="flex items-center justify-center p-3 border rounded-full hover:bg-blue-500">
                <ion-icon name="logo-facebook"></ion-icon>
              </div>
              <div class="flex items-center justify-center p-3 border rounded-full hover:bg-pink-500">
                <ion-icon name="logo-instagram"></ion-icon>
              </div>
              <div class="flex items-center justify-center p-3 border rounded-full hover:bg-green-500">
                <ion-icon name="logo-whatsapp"></ion-icon>
              </div>
              <div class="flex items-center justify-center p-3 border rounded-full hover:bg-gray-900">
                <ion-icon name="logo-tiktok"></ion-icon>
              </div>
            </div>
          </div>
          <!-- Columna 2: Últimas Noticias -->
          <div>
            <h2 class="text-white text-3xl font-semibold mb-8">Últimas Noticias</h2>
            <div class="w-full flex flex-col mt-6">
              <div class="w-full flex gap-4">
                <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm" src="https://www.estadiodeportivo.com/imagenes/2223259d-af0b-44e0-b26a-c1c311b70a8e_1200x680.jpeg" alt="Noticia 1">
                <div class="flex flex-col items-start">
                  <h3 class="xs:text-lg text-sm font-semibold">
                    Badé tumba la gran venta del Sevilla: las cifras del acuerdo con el Aston Villa
                  </h3>
                  <p class="text-sm text-gray-500">Ene 24, 2025</p>
                </div>
              </div>
            </div>
            <div class="w-full flex flex-col mt-6">
              <div class="w-full flex justify-start gap-4">
                <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm" src="https://www.estadiodeportivo.com/imagenes/a67c6b18-8874-4e47-9ca9-e13437a5b0aa_1200x680.jpeg" alt="Noticia 2">
                <div class="flex flex-col items-start">
                  <h3 class="xs:text-lg text-sm font-semibold">La RFEF da carpetazo al 'caso Kike Salas'</h3>
                  <p class="text-sm text-gray-500">Ene 17, 2025</p>
                </div>
              </div>
            </div>
            <div class="w-full flex flex-col mt-6">
              <div class="flex justify-start gap-4">
                <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm" src="https://www.estadiodeportivo.com/imagenes/7b681bc2-339c-4d84-a0cc-a048dc03e8d5_1200x680.jpeg" alt="Noticia 3">
                <div class="flex flex-col gap-2 items-start">
                  <h3 class="xs:text-lg text-sm font-semibold">
                    El Gremio pide la cesión de Marcao, pero él prefiere quedarse
                  </h3>
                  <p class="text-sm text-gray-500">Ene 21, 2025</p>
                </div>
              </div>
            </div>
          </div>
          <!-- Columna 3: Nuestro Periódico -->
          <div class="lg:pl-6">
            <h4 class="text-white text-3xl font-semibold mb-6">Nuestro Periódico</h4>
            <p class="text-gray-300 mb-7">Suscríbete para no tener publicidad</p>
            <div class="flex justify-center items-center bg-gray-700 rounded">
              <input type="text" class="w-full pl-4 bg-gray-700 text-gray-200 placeholder-gray-400 focus:outline-none" placeholder="Email">
              <button type="submit" class="py-3 px-2 bg-blue-400 shadow-md rounded-r text-white font-semibold hover:bg-fontOrange">Subscribe</button>
            </div>
          </div>
        </div>
        <hr class="bg-gray mt-14">
        <div class="flex flex-col items-center py-4">
          <div>Copyright &copy; 2024 Sammy-TG All Right Reserved</div>
        </div>
      </div>
    </footer>

    <!-- Scripts de Ionicons, Flowbite, jQuery y Bootstrap JS -->
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    <script src="https://unpkg.com/flowbite@1.4.1/dist/flowbite.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
    <!-- Incluir el archivo JS externo para cargar noticias -->
    <script src="/VistaPrueba2/noticias.js"></script>
  </body>
</html>
