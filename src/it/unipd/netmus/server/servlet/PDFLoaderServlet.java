package it.unipd.netmus.server.servlet;

import it.unipd.netmus.server.utils.GdataManager;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Nome: PDFLoaderServlet.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 * 
 */

public class PDFLoaderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String resourceId = req.getParameter("id");

            resp.setContentType("netmus/pdf");
            InputStream inStream = null;
            try {
                inStream = GdataManager.manager().getPdfInputStream(resourceId);

                int c;
                while ((c = inStream.read()) != -1) {
                    resp.getOutputStream().write(c);
                }
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
            }
            resp.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    
}
