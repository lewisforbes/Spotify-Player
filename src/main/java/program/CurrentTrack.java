package program;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

public class CurrentTrack {
    /** the current track as an api object **/
    private Track apiTrack;

    /** the api used to get information **/
    private SpotifyApi api;

    /** the track name **/
    private String trackName;
    public String getTrackName() { return trackName; }
    /** link to the album art **/
    private String albumCoverURL;
    public String getAlbumCoverURL() { return albumCoverURL; }

    /** make a new CurrentTrack **/
    public CurrentTrack(SpotifyApi api) {
        this.api = api;
        update();
    }

    /** update the with current info **/
    public void update() {
        this.apiTrack = getCurrentTrack();
        this.trackName = apiTrack.getName();
        this.albumCoverURL = apiTrack.getAlbum().getImages()[0].getUrl();
    }

    /** gets a user's currently playing track **/
    private Track getCurrentTrack() {
        final GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest = api.getUsersCurrentlyPlayingTrack().build();
        try {
            final CurrentlyPlaying currentlyPlaying = getUsersCurrentlyPlayingTrackRequest.execute();
            return (Track) currentlyPlaying.getItem();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}
