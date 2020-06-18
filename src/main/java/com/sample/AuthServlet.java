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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GoServlet.reset();
        resp.sendRedirect(SpotifyAuth.getURL()); // sends user to oAuth page
    }
}
