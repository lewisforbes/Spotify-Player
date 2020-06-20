package com.sample;

import com.wrapper.spotify.SpotifyApi;
import program.CurrentTrack;
import program.SpotifyAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "goservlet",
        urlPatterns = "/go"
)

public class GoServlet extends HttpServlet {
    /** the api for the program **/
    private static SpotifyApi api;
    public static SpotifyApi getApi() { return api; }

    /** the currently playing track **/
    private static CurrentTrack current;

    /** the user's crossfade setting **/
    private static int crossfade;
    public static void setCrossfade(int given) { crossfade = given; }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (current != null) {
            current.update();
            forward(req, resp);
            return;
        }

        if (api == null) {
            String fullQuery = "";
            fullQuery += req.getQueryString(); // done like this to avoid NPE
            if (fullQuery.contains("code=")) {
                String code = fullQuery.substring(fullQuery.indexOf("=") + 1);
                api = SpotifyAuth.getAPI(code);
            } else {
                resp.sendRedirect("index.html");
                return;
            }
        }

        current = new CurrentTrack(api);
        forward(req, resp);
    }

    /** resets everything bar crossfade **/
    public static void reset() {
        api = null;
        current = null;
    }

    /** forwards the view once current is set **/
    private static void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (current.isTrackPlaying()) {
            int refreshTime = current.getRefreshTime();
            refreshTime -= (crossfade+1);
            if (refreshTime<0) { refreshTime = 0; }

            req.setAttribute("songTitle", current.getTrackName());
            req.setAttribute("albumCoverURL", current.getAlbumCoverURL());
            req.setAttribute("chartsEmbed", current.getChartsEmbed());
            req.setAttribute("artistsNames", current.getArtistsNamesStr());
            req.setAttribute("refreshTime", "" + refreshTime);

            RequestDispatcher view = req.getRequestDispatcher("trackPlaying.jsp");
            view.forward(req,resp);
        } else {
            RequestDispatcher view = req.getRequestDispatcher("noTrackPlaying.jsp");
            view.forward(req,resp);
       }

    }
}
