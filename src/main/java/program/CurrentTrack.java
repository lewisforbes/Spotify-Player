package program;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

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
    /** is a track playing currently? **/
    private boolean trackPlaying;
    public boolean isTrackPlaying() { return trackPlaying; }

    /** audio features **/
    private float acousticness;
    private float danceability;
    private float energy;
    private float instrumentalness;
    private float liveness;
    private float speechiness;
    private float valence;
    /** the embedded chart with all features info **/
    private String featuresEmbed;
    public String getFeaturesEmbed() { return featuresEmbed; }


    /** make a new CurrentTrack **/
    public CurrentTrack(SpotifyApi api) {
        this.api = api;
        update();
    }

    /** update the with current info **/
    public void update() {
        this.apiTrack = getCurrentTrack();
        if (apiTrack == null) {
            this.trackPlaying = false;
            reset();
        } else {
            this.trackPlaying = true;
            this.trackName = apiTrack.getName();
            this.albumCoverURL = apiTrack.getAlbum().getImages()[0].getUrl();
            setAudioFeatures();
            setFeaturesEmbed();
        }
    }

    /** gets a user's currently playing track **/
    private Track getCurrentTrack() {
        final GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest = api.getUsersCurrentlyPlayingTrack().build();
        try {
            final CurrentlyPlaying currentlyPlaying = getUsersCurrentlyPlayingTrackRequest.execute();
            return (Track) currentlyPlaying.getItem();
        } catch (Exception e) {
            // no track playing
            return null;
        }
    }

    /** clears all values of the track **/
    private void reset() {
        this.apiTrack = null;
        this.trackName = null;
        this.albumCoverURL = null;
        this.featuresEmbed = null;
    }

    /** gets and sets the audio features for the track **/
    private void setAudioFeatures() {
        assert apiTrack != null;

        final GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = api.getAudioFeaturesForTrack(apiTrack.getId()).build();
        AudioFeatures audioFeatures;
        try {
            audioFeatures = getAudioFeaturesForTrackRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Was unable to get audio features.");
        }

        this.acousticness = audioFeatures.getAcousticness();
        this.danceability = audioFeatures.getDanceability();
        this.energy = audioFeatures.getEnergy();
        this.instrumentalness = audioFeatures.getInstrumentalness();
        this.liveness = audioFeatures.getLiveness();
        this.speechiness = audioFeatures.getSpeechiness();
        this.valence = audioFeatures.getValence();
    }

    /** creates and sets the chart embed visualising the track's audio features **/
    private void setFeaturesEmbed() {
        this.featuresEmbed = "google.charts.load('current', {packages: ['corechart', 'bar']});\n" +
                "google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "function drawChart() {\n" +
                "      var data = google.visualization.arrayToDataTable([\n" +
                "        ['Feature', 'Value', { role: 'style' } ],\n" +
                "        ['Acousticness', " + this.acousticness + ", 'color: 5D35FC'],\n" +
                "        ['Danceability', " + this.danceability + ", 'color: 1C047D'],\n" +
                "        ['Energy', " + this.energy + ", 'color: A29BBD'],\n" +
                "        ['Instrumentalness', " + this.instrumentalness + ", 'color: 312C47'],\n" +
                "        ['Speechiness', " + this.speechiness + ", 'color: 896DFC'],\n" +
                "        ['Liveness', " + this.liveness + ", 'color: 957DC9'],\n" +
                "        ['Valence', " + this.valence + ", 'color: 4225B8'],\n" +
                "      ]);\n" +
                "      \n" +
                "      var options = {\n" +
                "      axisTitlePosition: 'none',\n" +
                "      hAxis: { ticks: [] },\n" +
                "      legend: { position: 'none' },\n" +
                "      chartArea: { left: 100, width: 200 },\n" +
                "      };\n" +
                "\n" +
                "      var chart = new google.visualization.BarChart(document.getElementById('features_div'));\n" +
                "      chart.draw(data, options);\n" +
                "    }";
    }
}
