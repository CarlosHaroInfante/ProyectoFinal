<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VistaPrueba2.Dtos.usuarioDTO" %>
<%@ page import="java.util.Base64" %>
<%
    // Convertir la imagen del usuario a Base64 para mostrarla y conservarla
    String imagenBase64 = "";
    usuarioDTO usuario = (usuarioDTO) request.getAttribute("usuario");
    if(usuario != null && usuario.getImagenUsuario() != null) {
        imagenBase64 = Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
    }
%>
<!DOCTYPE html>
<html lang="es" class="h-full bg-gray-100">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Modificar Usuario</title>
  <!-- Tailwind CSS -->
  <script src="https://cdn.tailwindcss.com"></script>
  <!-- Toastify CSS & JS: usar una única referencia -->
  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/dist/toastify.min.css">
  <script src="https://cdn.jsdelivr.net/npm/toastify-js/dist/toastify.min.js" defer></script>
  <script defer>
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

    // Función para validar un campo y mostrar u ocultar el mensaje de error
    function validateField(fieldId, errorId) {
      const field = document.getElementById(fieldId);
      const errorSpan = document.getElementById(errorId);
      if(field.value.trim() === "") {
        errorSpan.classList.remove("hidden");
        return false;
      } else {
        errorSpan.classList.add("hidden");
        return true;
      }
    }

    document.addEventListener("DOMContentLoaded", function() {
      // Validar al salir de cada campo
      document.getElementById("nombreCompleto").addEventListener("blur", function() {
        validateField("nombreCompleto", "nombreError");
      });
      document.getElementById("numeroUsuario").addEventListener("blur", function() {
        validateField("numeroUsuario", "numeroError");
      });
      document.getElementById("rolUsuario").addEventListener("blur", function() {
        validateField("rolUsuario", "rolError");
      });

      // Validar en el submit del formulario
      document.getElementById("modificarForm").addEventListener("submit", function(event) {
        const validNombre = validateField("nombreCompleto", "nombreError");
        const validNumero = validateField("numeroUsuario", "numeroError");
        const validRol = validateField("rolUsuario", "rolError");
        if (!validNombre || !validNumero || !validRol) {
          event.preventDefault();
          Toastify({
            text: "Por favor, completa todos los campos obligatorios.",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#FF0000"
          }).showToast();
        }
      });
    });
  </script>
  <style>
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-10px); }
      to { opacity: 1; transform: translateY(0); }
    }
    .animate-fade-in {
      animation: fadeIn 0.5s ease-out forwards;
    }
  </style>
</head>
<body class="flex items-center justify-center min-h-screen bg-gray-100">
  <div class="bg-black rounded-xl shadow-2xl p-8 max-w-md w-full animate-fade-in">
    <form id="modificarForm" action="ModificarUsuario" method="post" enctype="multipart/form-data" novalidate>
      <!-- Campo oculto para id -->
      <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
      <!-- Campo oculto para conservar la imagen actual (Base64) -->
      <input type="hidden" name="imagenUsuarioActual" value="<%= imagenBase64 %>">
      
      <h1 class="text-2xl font-bold text-yellow-500 mb-4 text-center">Modificar Usuario</h1>
      
      <div class="mb-4">
        <label for="nombreCompleto" class="block text-sm font-medium text-yellow-500 mb-2">Nombre Completo</label>
        <input type="text" id="nombreCompleto" name="nombreCompleto" value="${usuario.nombreCompleto}" class="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-yellow-500 focus:ring-2 focus:ring-yellow-500" required>
        <span id="nombreError" class="text-red-500 text-sm hidden">Este campo es obligatorio.</span>
      </div>
      
      <div class="mb-4">
        <label for="numeroUsuario" class="block text-sm font-medium text-yellow-500 mb-2">Número de Teléfono</label>
        <input type="tel" id="numeroUsuario" name="numeroUsuario" value="${usuario.numeroUsuario}" class="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-yellow-500 focus:ring-2 focus:ring-yellow-500" required>
        <span id="numeroError" class="text-red-500 text-sm hidden">Este campo es obligatorio.</span>
      </div>
      
      <div class="mb-4">
        <label for="rolUsuario" class="block text-sm font-medium text-yellow-500 mb-2">Rol de Usuario</label>
        <select id="rolUsuario" name="rolUsuario" class="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-yellow-500 focus:ring-2 focus:ring-yellow-500" required>
          <option value="">Seleccione un rol</option>
          <option value="Admin" <%= "Admin".equals(usuario.getRolUsuario()) ? "selected" : "" %>>Admin</option>
          <option value="Periodista" <%= "Periodista".equals(usuario.getRolUsuario()) ? "selected" : "" %>>Periodista</option>
          <option value="Usuario" <%= "Usuario".equals(usuario.getRolUsuario()) ? "selected" : "" %>>Usuario</option>
        </select>
        <span id="rolError" class="text-red-500 text-sm hidden">Este campo es obligatorio.</span>
      </div>
      
      <div class="mb-4">
        <label for="correoUsuario" class="block text-sm font-medium text-yellow-500 mb-2">Correo Electrónico</label>
        <!-- Campo readonly para mostrar el correo sin permitir edición -->
        <input type="email" id="correoUsuario" name="correoUsuario" value="${usuario.correoUsuario}" class="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-yellow-500 focus:ring-2 focus:ring-yellow-500 bg-gray-200" readonly>
      </div>
      
      <div class="mb-4">
        <label for="imagenUsuario" class="block text-sm font-medium text-yellow-500 mb-2">Foto de Perfil</label>
        <input type="file" id="imagenUsuario" name="imagenUsuario" accept="image/*" class="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-yellow-500 focus:ring-2 focus:ring-yellow-500" onchange="previewImage(event)">
        <!-- Mostrar la imagen actual usando el valor Base64 obtenido -->
        <img src="data:image/png;base64,<%= imagenBase64 %>" alt="Imagen actual" class="w-24 h-24 object-cover rounded mb-2" id="imagenActual">
        <img id="preview" class="mt-2 w-24 h-24 object-cover rounded hidden" alt="Vista previa de la nueva imagen">
        <p class="text-sm text-gray-500">Si no seleccionas una nueva imagen, se conservará la actual.</p>
      </div>
      
      <button type="submit" class="w-full bg-yellow-500 text-black py-3 rounded-lg font-semibold hover:bg-yellow-600">
        Guardar Cambios
      </button>
    </form>
  </div>
</body>
</html>
 