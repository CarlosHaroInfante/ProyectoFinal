<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>El Pase Sevillano</title>
  <!-- Si usas un archivo CSS externo, puedes incluirlo aquí -->
  <link rel="stylesheet" href="css.css">
  <script src="//unpkg.com/alpinejs" defer></script>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    /* Estilos para el botón de cierre de sesión */
    .logout-button {
      background-color: #f44336;
      color: white;
      padding: 10px 15px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
      transition: background-color 0.3s ease;
    }
    .logout-button:hover {
      background-color: #d32f2f;
    }
  </style>
</head>
<body>
  <!-- Cabecera -->
  <nav class="bg-white border-gray-200 py-2.5 dark:bg-gray-900">
    <div class="flex flex-wrap items-center justify-between max-w-screen-xl px-4 mx-auto">
      <a href="#" class="flex items-center">
        <img src="img/cabecera.png" class="h-6 mr-3 sm:h-9" alt="Landwind Logo">
        <span class="self-center text-xl font-semibold whitespace-nowrap dark:text-white"></span>
      </a>
      <div class="flex items-center lg:order-2">
        <% 
          // Obtenemos el usuario logueado (si existe) de la sesión
          String usuario = (String) session.getAttribute("usuarioLogueado");
          String rolUsuario = (String) session.getAttribute("rolUsuario");
          if (usuario != null) {
        %>
          <!-- Si hay usuario, se muestra el mensaje de bienvenida y el botón de logout -->
          <div class="hidden mt-2 mr-4 sm:inline-block">
            <span class="mr-4 text-gray-800 dark:text-white text-lg">
              Bienvenido/a: <strong><%= usuario %></strong>
            </span>
          </div>
          <a class="logout-button" href="<%= request.getContextPath() %>/logout">Cerrar Sesión</a> 
		  } %>
        <% }  
          else { %>
          <!-- Si no hay usuario, se muestra el enlace para iniciar sesión -->
          <a href="InicioSesion.html" class="text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 sm:mr-2 lg:mr-0 dark:bg-purple-600 dark:hover:bg-purple-700 focus:outline-none dark:focus:ring-purple-800">
            Iniciar Sesión
          </a>
          <a href="Admin.html" class="text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 sm:mr-2 lg:mr-0 dark:bg-purple-600 dark:hover:bg-purple-700 focus:outline-none dark:focus:ring-purple-800">
            Admin
          </a>
        <% } %>
        <button data-collapse-toggle="mobile-menu-2" type="button"
          class="inline-flex items-center p-2 ml-1 text-sm text-gray-500 rounded-lg lg:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600"
          aria-controls="mobile-menu-2" aria-expanded="true">
          <img id="userImage" class="rounded-full w-16 h-16 object-cover border-2 border-gray-300" alt="Foto de perfil">
          <span class="sr-only">Open main menu</span>
          <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd"
              d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
              clip-rule="evenodd"></path>
          </svg>
          <svg class="hidden w-6 h-6" fill="currentColor" viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd"
              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
              clip-rule="evenodd"></path>
          </svg>
        </button>
      </div>
      <div class="items-center justify-between w-full lg:flex lg:w-auto lg:order-1" id="mobile-menu-2">
        <ul class="flex flex-col mt-4 font-medium lg:flex-row lg:space-x-8 lg:mt-0">
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-white bg-purple-700 rounded lg:bg-transparent lg:text-purple-700 lg:p-0 dark:text-white"
              aria-current="page">Home</a>
          </li>
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-gray-700 border-b border-gray-100 hover:bg-gray-50 lg:hover:bg-transparent lg:border-0 lg:hover:text-purple-700 lg:p-0 dark:text-gray-400 lg:dark:hover:text-white dark:hover:bg-gray-700 dark:hover:text-white lg:dark:hover:bg-transparent dark:border-gray-700">
              Company</a>
          </li>
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-gray-700 border-b border-gray-100 hover:bg-gray-50 lg:hover:bg-transparent lg:border-0 lg:hover:text-purple-700 lg:p-0 dark:text-gray-400 lg:dark:hover:text-white dark:hover:bg-gray-700 dark:hover:text-white lg:dark:hover:bg-transparent dark:border-gray-700">
              Marketplace</a>
          </li>
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-gray-700 border-b border-gray-100 hover:bg-gray-50 lg:hover:bg-transparent lg:border-0 lg:hover:text-purple-700 lg:p-0 dark:text-gray-400 lg:dark:hover:text-white dark:hover:bg-gray-700 dark:hover:text-white lg:dark:hover:bg-transparent dark:border-gray-700">
              Features</a>
          </li>
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-gray-700 border-b border-gray-100 hover:bg-gray-50 lg:hover:bg-transparent lg:border-0 lg:hover:text-purple-700 lg:p-0 dark:text-gray-400 lg:dark:hover:text-white dark:hover:bg-gray-700 dark:hover:text-white lg:dark:hover:bg-transparent dark:border-gray-700">
              Team</a>
          </li>
          <li>
            <a href="#" class="block py-2 pl-3 pr-4 text-gray-700 border-b border-gray-100 hover:bg-gray-50 lg:hover:bg-transparent lg:border-0 lg:hover:text-purple-700 lg:p-0 dark:text-gray-400 lg:dark:hover:text-white dark:hover:bg-gray-700 dark:hover:text-white lg:dark:hover:bg-transparent dark:border-gray-700">
              Contact</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <br>
  <br>

  <!-- Carrusel -->
  <h2 class="text-3xl font-bold text-center sm:text-5xl">Noticias Destacadas</h2>
  <br>
  <br>
  <div class="max-w-2xl mx-auto">
    <div id="default-carousel" class="relative rounded-lg overflow-hidden shadow-lg" data-carousel="static">
      <!-- Carousel wrapper -->
      <div class="relative h-80 md:h-96" data-carousel-inner>
        <!-- Item 1 -->
        <div class="hidden duration-700 ease-in-out" data-carousel-item>
          <img src="https://flowbite.com/docs/images/carousel/carousel-1.svg" class="object-cover w-full h-full" alt="Slide 1">
          <span class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-xl font-semibold text-white md:text-2xl dark:text-gray-800">First Slide</span>
        </div>
        <!-- Item 2 -->
        <div class="hidden duration-700 ease-in-out" data-carousel-item>
          <img src="https://flowbite.com/docs/images/carousel/carousel-2.svg" class="object-cover w-full h-full" alt="Slide 2">
        </div>
        <!-- Item 3 -->
        <div class="hidden duration-700 ease-in-out" data-carousel-item>
          <img src="https://flowbite.com/docs/images/carousel/carousel-3.svg" class="object-cover w-full h-full" alt="Slide 3">
        </div>
      </div>
      <!-- Slider indicators -->
      <div class="flex absolute bottom-5 left-1/2 z-30 -translate-x-1/2 space-x-2" data-carousel-indicators>
        <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400 focus:outline-none focus:bg-gray-400 transition"></button>
        <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400 focus:outline-none focus:bg-gray-400 transition"></button>
        <button type="button" class="w-3 h-3 rounded-full bg-gray-300 hover:bg-gray-400 focus:outline-none focus:bg-gray-400 transition"></button>
      </div>
      <!-- Slider controls -->
      <button type="button" class="flex absolute top-1/2 left-3 z-40 items-center justify-center w-10 h-10 bg-gray-200/50 rounded-full hover:bg-gray-300 focus:outline-none transition" data-carousel-prev>
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
        </svg>
      </button>
      <button type="button" class="flex absolute top-1/2 right-3 z-40 items-center justify-center w-10 h-10 bg-gray-200/50 rounded-full hover:bg-gray-300 focus:outline-none transition" data-carousel-next>
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
        </svg>
      </button>
    </div>
    <script src="https://unpkg.com/flowbite@1.4.0/dist/flowbite.js"></script>
  </div>

  <br>
  <br>
  <br>

  <h2 class="text-3xl font-bold text-center sm:text-5xl">Todas las Noticias</h2>
  <br>
  <div class="max-w-screen-xl mx-auto p-5 sm:p-10 md:p-16 relative">
    <div class="grid grid-cols-1 sm:grid-cols-12 gap-5">
      <div class="sm:col-span-5">
        <a href="#">
          <div class="bg-cover text-center overflow-hidden"
            style="min-height: 300px; background-image: url('https://api.time.com/wp-content/uploads/2020/07/never-trumpers-2020-election-01.jpg?quality=85&amp;w=1201&amp;h=676&amp;crop=1')"
            title="Woman holding a mug">
          </div>
        </a>
        <div class="mt-3 bg-white rounded-b lg:rounded-b-none lg:rounded-r flex flex-col justify-between leading-normal">
          <div>
            <a href="#" class="text-xs text-indigo-600 uppercase font-medium hover:text-gray-900 transition duration-500 ease-in-out">
              Election
            </a>
            <a href="#" class="block text-gray-900 font-bold text-2xl mb-2 hover:text-indigo-600 transition duration-500 ease-in-out">
              Revenge of the Never Trumpers
            </a>
            <p class="text-gray-700 text-base mt-2">
              Meet the Republican dissidents fighting to push Donald Trump out of office—and reclaim their party
            </p>
          </div>
        </div>
      </div>
      <div class="sm:col-span-7 grid grid-cols-2 lg:grid-cols-3 gap-5">
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/07/president-trump-coronavirus-election.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            Trump Steps Back Into Coronavirus Spotlight
          </a>
        </div>
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/06/GettyImages-1222922545.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            How Trump's Mistakes Became Biden's Big Breaks
          </a>
        </div>
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/07/American-Flag.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            Survey: Many Americans 'Dissatisfied' With U.S.
          </a>
        </div>
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/06/GettyImages-1222922545.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            How Trump's Mistakes Became Biden's Big Breaks
          </a>
        </div>
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/07/American-Flag.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            Survey: Many Americans 'Dissatisfied' With U.S.
          </a>
        </div>
        <div>
          <a href="#">
            <div class="h-40 bg-cover text-center overflow-hidden"
              style="background-image: url('https://api.time.com/wp-content/uploads/2020/07/president-trump-coronavirus-election.jpg?quality=85&amp;w=364&amp;h=204&amp;crop=1')"
              title="Woman holding a mug">
            </div>
          </a>
          <a href="#" class="text-gray-900 inline-block font-semibold text-md my-2 hover:text-indigo-600 transition duration-500 ease-in-out">
            Trump Steps Back Into Coronavirus Spotlight
          </a>
        </div>
      </div>
    </div>

    <br>
    <br>
    <br>

    <div class="w-full h-full">
      <!-- Footer  -->
      <footer class="w-full h-fit bg-black text-white relative bottom-0">
        <div class="w-full mx-auto sm:px-10 px-4 pb-10">
          <div class="grid lg:grid-cols-3 grid-cols-1 gap-4 justify-items-start pt-12">
            <!-- col 1 -->
            <div class="mt-4">
              <div class="flex-1 flex justify-between items-center">
                <!--<img class="sm:w-[10rem] xs:w-[7rem] z-10" src="img/cabecera.png" alt="Logo" />-->
                <h2 class="text-white text-3xl font-semibold mb-8">Infomación sobre Nosotros</h2>
              </div>
              <p class="mt-4">
                Un periodico centrado en el fútbol sevillano, con las últimas noticias, post-partidos, alineaciones, fichajes y ... ¡mucho mas!
              </p>
              <!-- Socials -->
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
            <!-- col - 2 -->
            <div class="mt-4">
              <h2 class="text-white text-3xl font-semibold mb-8">Ultimas noticias</h2>
              <!-- 1 -->
              <div class="w-full flex flex-col mt-6">
                <div class="w-full flex gap-4">
                  <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm xs:outline xs:outline-[4px]" src="https://www.estadiodeportivo.com/imagenes/2223259d-af0b-44e0-b26a-c1c311b70a8e_1200x680.jpeg" alt="Badé la cabra" />
                  <div class="flex flex-col items-start">
                    <h3 class="xs:text-lg text-sm font-semibold">
                      Badé tumba la gran venta del Sevilla: las cifras del acuerdo con el Aston Villa
                    </h3>
                    <p class="text-sm text-gray-500">Ene 24, 2025</p>
                  </div>
                </div>
              </div>
              <!-- 2 -->
              <div class="w-full flex flex-col mt-6">
                <div class="w-full flex justify-start gap-4">
                  <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm xs:outline xs:outline-[4px]" src="https://www.estadiodeportivo.com/imagenes/a67c6b18-8874-4e47-9ca9-e13437a5b0aa_1200x680.jpeg" alt="Apuesto por amarilla" />
                  <div class="flex flex-col items-start">
                    <h3 class="xs:text-lg text-sm font-semibold">La RFEF da carpetazo al 'caso Kike Salas'</h3>
                    <p class="text-sm text-gray-500">Ene 17, 2025</p>
                  </div>
                </div>
              </div>
              <!-- 3 -->
              <div class="w-full flex flex-col mt-6">
                <div class="flex justify-start gap-4">
                  <img class="lg:w-[8rem] lg:h-[5rem] md:w-[6rem] md:h-[4rem] xs:w-[8rem] xs:h-[5rem] w-[6rem] h-[3rem] rounded-sm xs:outline xs:outline-[4px]" src="https://www.estadiodeportivo.com/imagenes/7b681bc2-339c-4d84-a0cc-a048dc03e8d5_1200x680.jpeg" alt="Marcao putero" />
                  <div class="flex flex-col gap-2 items-start">
                    <h3 class="xs:text-lg text-sm font-semibold">
                      El Gremio pide la cesión de Marcao, pero él prefiere quedarse
                    </h3>
                    <p class="text-sm text-gray-500">Ene 21, 2025</p>
                  </div>
                </div>
              </div>
            </div>
            <!-- col - 4 -->
            <div class="w-full mt-4 lg:pl-6">
              <h4 class="text-white text-3xl font-semibold mb-6">Nuestro Periodico</h4>
              <p class="text-gray-300 mb-7">Suscribete para no tener publicidad</p>
              <div class="w-full flex justify-center items-center rounded bg-gray-700">
                <input type="text" class="w-full h-full pl-4 text-gray-200 bg-gray-700 lg:text-left placeholder:text-gray-400 focus:outline-none focus:border-gray-500" placeholder="Email"/>
                <button type="submit" class="h-full py-3 xs:px-6 px-2 bg-blue-400 transition-all duration-500 shadow-md rounded-r text-sm text-white font-semibold w-fit hover:bg-fontOrange">
                  Subscribe
                </button>
              </div>
            </div>
          </div>
          <hr class="bg-gray mt-14" />
          <div class="w-full flex gap-2 flex-col items-center justify-center py-4">
            <div>Copyright &copy; 2024 Sammy-TG All Right Reserved</div>
          </div>
        </div>
      </footer>
    </div>
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    <script src="https://unpkg.com/flowbite@1.4.1/dist/flowbite.js"></script>
</body>
</html>
