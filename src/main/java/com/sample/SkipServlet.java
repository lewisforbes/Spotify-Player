package com.sample;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "skipservlet",
        urlPatterns = "/skip"
)

public class SkipServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SpotifyApi api = GoServlet.getApi();
        if (api == null) {
            resp.sendRedirect("index.html");
            return;
        }

        skipSong(api);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Was unable to pause.");
        }
        resp.sendRedirect("go");
    }

    /** skips to the next playing song **/
    private static void skipSong(SpotifyApi api) {
        final SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = api.skipUsersPlaybackToNextTrack().build();
        try {
            skipUsersPlaybackToNextTrackRequest.execute();
        } catch (Exception ignored) { } // nothing happens if unsuccessful
    }
}
