package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;

import VistaPrueba2.Servicios.EliminarUsuarioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EliminarUsuario")
public class EliminarUsuarioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EliminarUsuarioServicio eliminarUsuarioServicio;

    @Override
    public void init() throws ServletException {
        eliminarUsuarioServicio = new EliminarUsuarioServicio();
    }

    // Método doGet para pruebas: confirma que el servlet está desplegado
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().write("Servlet EliminarUsuario OK");
    }

    // Método doPost: recibe el parámetro 'idUsuario' y elimina el usuario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        boolean eliminado = eliminarUsuarioServicio.eliminarUsuario(idUsuario);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"eliminado\": " + eliminado + "}");
        out.flush();
    }
}
