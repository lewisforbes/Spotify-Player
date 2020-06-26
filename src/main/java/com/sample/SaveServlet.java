package com.sample;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import com.wrapper.spotify.requests.data.library.SaveTracksForUserRequest;
import com.wrapper.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "saveservlet",
        urlPatterns = "/save"
)
public class SaveServlet extends HttpServlet {

    /** the object used to access the Spotify API **/
    private static SpotifyApi api;

    /** the ID of the playlist to save to **/
    private static String playlistID;
    public static void setPlaylistID(String givenID) { playlistID = givenID; }

    /** the message to display on the snackbar if save successful **/
    private static String snackbarMessage;
    /** the name of the playlist to save to **/
    private static String playlistName = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        api = GoServlet.getApi();
        snackbarMessage = null;
        String trackID = GoServlet.getCurrentTrackID();
        if (trackID != null) {
            if (playlistID != null) {
                if (playlistName == null) {
                    playlistName = getPlaylistName(playlistID);
                }
                saveToPlaylist(trackID);
            } else {
                if (inSavedSongs(trackID)) {
                    snackbarMessage = "Song already in Saved Songs.";
                } else {
                    saveToSavedSongs(trackID);
                }
            }
        }

        GoServlet.setSnackbarMessage(snackbarMessage);
        resp.sendRedirect("go"); // nothing happens if no track playing
    }

    /** saves the current song to the pre-specified playlist **/
    private static void saveToPlaylist(String trackID) {
        assert playlistID != null;
        assert trackID != null;

        final AddItemsToPlaylistRequest addItemsToPlaylistRequest = api.
                addItemsToPlaylist(playlistID, new String[]{"spotify:track:" + trackID}).build();

        try {
            addItemsToPlaylistRequest.execute();
            snackbarMessage = "Successfully saved to: " + playlistName;
        } catch (Exception ignored) { }
    }

    /** returns true iff song is already in saved songs **/
    private static boolean inSavedSongs(String trackID) {
        final CheckUsersSavedTracksRequest checkUsersSavedTracksRequest = api.checkUsersSavedTracks(new String[]{trackID}).build();

        try {
            final Boolean[] booleans = checkUsersSavedTracksRequest.execute();
            return booleans[0];

        } catch (Exception ignored) { }
        return false;
    }

    /** saves the current song to the user's saved songs **/
    private static void saveToSavedSongs(String trackID) {
        assert trackID != null;

        final SaveTracksForUserRequest saveTracksForUserRequest =
                api.saveTracksForUser(new String[]{trackID}).build();

        try {
            saveTracksForUserRequest.execute();
            snackbarMessage = "Successfully saved to Saved Songs.";
        } catch (Exception ignored) { }
    }

    /** gets the name of a given playlist **/
    private static String getPlaylistName(String playlistID) {
        assert  playlistID != null;

        final GetPlaylistRequest getPlaylistRequest = api.getPlaylist(playlistID).build();
        try {
            final Playlist playlist = getPlaylistRequest.execute();
            return playlist.getName();
        } catch (Exception ignored) {
            return null;
        }
    }

}
