<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VistaPrueba2.Dtos.usuarioDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Modificar Usuario</title>
<script src="https://cdn.tailwindcss.com"></script>
  <!-- Toastify CSS & JS -->
  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <script>
      function previewImage(event) {
          const input = event.target;
          const reader = new FileReader();
          reader.onload = function() {
              const img = document.getElementById("preview");
              img.src = reader.result;
              img.style.display = "block";
          }
          if (input.files && input.files[0]) {
              reader.readAsDataURL(input.files[0]);
          }
      }
    </script>
</head>
<body class="bg-gray-100">
  <div class="max-w-md mx-auto mt-10 bg-white p-6 rounded-lg shadow-md">
    <h1 class="text-2xl font-bold mb-4 text-center">Modificar Usuario</h1>
    <form id="modificarForm" action="ModificarUsuario" method="post" enctype="multipart/form-data">
      <!-- Campo oculto para id -->
      <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
      <!-- Campo oculto para conservar la imagen actual (en Base64) -->
      <input type="hidden" name="imagenUsuarioActual" value="${usuario.imagenUsuario}">
      
      <div class="mb-4">
        <label for="nombreCompleto" class="block text-gray-700 mb-2">Nombre Completo</label>
        <input type="text" id="nombreCompleto" name="nombreCompleto" value="${usuario.nombreCompleto}" class="w-full px-3 py-2 border rounded" required>
      </div>
      
      <div class="mb-4">
        <label for="numeroUsuario" class="block text-gray-700 mb-2">Número de Teléfono</label>
        <input type="text" id="numeroUsuario" name="numeroUsuario" value="${usuario.numeroUsuario}" class="w-full px-3 py-2 border rounded" required>
      </div>
      
      <div class="mb-4">
        <label for="rolUsuario" class="block text-gray-700 mb-2">Rol de Usuario</label>
        <input type="text" id="rolUsuario" name="rolUsuario" value="${usuario.rolUsuario}" class="w-full px-3 py-2 border rounded" required>
      </div>
      
      <div class="mb-4">
        <label for="correoUsuario" class="block text-gray-700 mb-2">Correo Electrónico</label>
        <!-- Campo readonly para mostrar el correo sin permitir edición -->
        <input type="email" id="correoUsuario" name="correoUsuario" value="${usuario.correoUsuario}" class="w-full px-3 py-2 border rounded bg-gray-200" readonly>
      </div>
      
      <div class="mb-4">
        <label for="imagenUsuario" class="block text-gray-700 mb-2">Imagen de Perfil</label>
        <div>
          <!-- Mostrar la imagen actual con dimensiones fijas -->
          <img src="data:image/png;base64,${usuario.imagenUsuario}" alt="Imagen actual" class="w-24 h-24 object-cover rounded mb-2" id="imagenActual">
        </div>
        <input type="file" id="imagenUsuario" name="imagenUsuario" accept="image/*" onchange="previewImage(event)">
        <img id="preview" class="mt-2 w-24 h-24 object-cover rounded hidden" alt="Vista previa de la nueva imagen">
        <p class="text-sm text-gray-500">Si no seleccionas una nueva imagen, se conservará la actual.</p>
      </div>
      
      <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded">Guardar Cambios</button>
    </form>
  </div>
</body>
</html>
