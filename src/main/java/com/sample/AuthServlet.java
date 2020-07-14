package com.sample;

import program.SpotifyAuth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "authservlet",
        urlPatterns = "/auth"
)
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final int crossfade = Integer.parseInt(req.getParameter("crossfade"));
        GoServlet.setCrossfade(crossfade);

        final String playlistID = extractID(req.getParameter("playlist"));
        SaveServlet.setPlaylistID(playlistID);

        String value = req.getParameter("controls");
        GoServlet.setShowControls(value != null);

        GoServlet.reset();
        resp.sendRedirect(SpotifyAuth.getURL()); // sends user to oAuth page
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.html"); // should not be reached by get
    }

    /** extracts the playlist ID from a url **/
    private static String extractID(String url) {
        if (url == null) {
            return null;
        }

        final String before = "/playlist/";

        if (!url.contains(before)) {
            return null;
        }

        if (url.contains("?si=")) {
            url = url.substring(0, url.indexOf("?si="));
        }

        return url.substring(url.indexOf(before) + before.length());
    }
}
