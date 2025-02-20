package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.PanelAdminServicio;

@WebServlet("/mostrarUsuarios")
public class PanelAdminControlador extends HttpServlet {

    private PanelAdminServicio panelAdminServicio = new PanelAdminServicio();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se obtiene el parámetro "limit" para limitar la cantidad de usuarios a mostrar.
        String limitParam = request.getParameter("limit");
        int limit = 0;
        try {
            limit = (limitParam != null) ? Integer.parseInt(limitParam) : 0;
        } catch (NumberFormatException e) {
            limit = 0;
        }
        
        // Obtener la lista de usuarios (ordenados descendientemente, con límite si se pasa)
        List<usuarioDTO> usuarios = panelAdminServicio.obtenerUsuarios(limit);
        
        // Convertir la lista a JSON
        Gson gson = new Gson();
        String json = gson.toJson(usuarios);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
    }
}
