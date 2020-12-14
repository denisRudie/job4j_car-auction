package ru.job4j.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.persist.HbmAuction;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class ImageServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ImageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (OutputStream out = resp.getOutputStream()) {
            String imagename = req.getParameter("photoId");
            resp.setContentType("name=" + imagename);
            resp.setContentType("image/png");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + imagename + "\"");
            File file = new File("images" + File.separator + imagename);
            try (FileInputStream in = new FileInputStream(file)) {
                out.write(in.readAllBytes());
            }
            resp.setStatus(200);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        int carId = (int) req.getSession().getAttribute("carId");
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    int photoId = HbmAuction.instOf().addCarPhoto();
                    File file = new File(folder + File.separator + photoId);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                        out.flush();
                    }
                    HbmAuction.instOf().updateCarPhoto(carId, photoId);
                    req.getSession().removeAttribute("carId");
                }
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (FileUploadException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
