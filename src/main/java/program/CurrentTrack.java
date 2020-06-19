package program;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;
import program.location.CountryFromPlace;
import program.location.GetLocation;

import java.util.ArrayList;

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
    /** the track's artists' names **/
    private ArrayList<String> artistsNames;
    public ArrayList<String> getArtistsNames() { return artistsNames; }

    /** audio features **/
    private float acousticness;
    private float danceability;
    private float energy;
    private float instrumentalness;
    private float liveness;
    private float speechiness;
    private float valence;
    /** the embedded chart with all features info **/
    private String chartsEmbed;
    public String getChartsEmbed() { return chartsEmbed; }


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
            setArtistsNames();
            setChartsEmbed();


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
        this.chartsEmbed = null;
        this.artistsNames = null;
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

    /** sets the tracks' artists **/
    private void setArtistsNames() {
        ArrayList<String> output = new ArrayList<>();
        for (ArtistSimplified as : apiTrack.getArtists()) {
            output.add(as.getName());
        }
        this.artistsNames = output;
    }

    /** creates and sets the chart embed visualising the track's audio features **/
    private String mkFeaturesEmbed() {
        return
           "google.charts.setOnLoadCallback(drawFeatures);\n" +
                   "\n" +
                   " function drawFeatures() {\n" +
                   "   var features_data = google.visualization.arrayToDataTable([\n" +
                   "     ['Feature', 'Value', {\n" +
                   "       role: 'style'\n" +
                   "     }],\n" +
                   "     ['Acousticness', " + this.acousticness + ", 'color: 5D35FC'],\n" +
                   "     ['Danceability', " + this.danceability + ", 'color: 1C047D'],\n" +
                   "     ['Energy', " + this.energy + ", 'color: A29BBD'],\n" +
                   "     ['Instrumentalness', " + this.instrumentalness + ", 'color: 312C47'],\n" +
                   "     ['Speechiness', " + this.speechiness + ", 'color: 896DFC'],\n" +
                   "     ['Liveness', " + this.liveness + ", 'color: 957DC9'],\n" +
                   "     ['Valence', " + this.valence + ", 'color: 4225B8'],\n" +
                   "   ]);\n" +
                   "\n" +
                   "   var features_options = {\n" +
                   "     axisTitlePosition: 'none',\n" +
                   "     hAxis: {\n" +
                   "       ticks: []\n" +
                   "     },\n" +
                   "     legend: {\n" +
                   "       position: 'none'\n" +
                   "     },\n" +
                   "     chartArea: {\n" +
                   "       left: 100,\n" +
                   "       width: 400,\n" +
                   "     },\n" +
                   "     backgroundColor: {\n" +
                   "       fill: 'gainsboro',\n" +
//                   "       stroke: '85828F',\n" +
//                   "       strokeWidth: 3\n" +
                   "     },\n" +
                   "     enableInteractivity: 'false',\n" +
                   "   };\n" +
                   "\n" +
                   "   var features_chart = new google.visualization.BarChart(document.getElementById('features_chart'));\n" +
                   "\n" +
                   "   google.visualization.events.addListener(features_chart, 'ready', function() {\n" +
                   "     features_chart.innerHTML = '<img src=\"' + features_chart.getImageURI() + '\">';\n" +
                   "     console.log(features_chart.innerHTML);\n" +
                   "   });\n" +
                   "\n" +
                   "   features_chart.draw(features_data, features_options);\n" +
                   " }";
    }

    /** sets the maps embed **/
    private String mkMapEmbed() {
        assert artistsNames != null;
        /* makes list of unique artists' countries */
        ArrayList<String> artistsCountries = new ArrayList<>();
        String countryToAdd;
        for (String artistName : artistsNames) {
            countryToAdd = CountryFromPlace.getCountryCode(GetLocation.getLoc(artistName));
            if (!artistsCountries.contains(countryToAdd)) {
                artistsCountries.add(countryToAdd);
            }
        }

        /* formats the countries for embedding */
        StringBuilder formattedCountries = new StringBuilder("['Country', 'Popularity']");
        for (String country : artistsCountries) {
            formattedCountries.append(",\n['").append(country).append("', ").append("1").append("]");
        }

        return
            "google.charts.setOnLoadCallback(drawMap);\n" +
            "function drawMap() {\n" +
            "        var map_data = google.visualization.arrayToDataTable([\n" +
            formattedCountries.toString() +
            "        ]);\n" +
            "\n" +
            "var map_options = {\n" +
                    "     minValue: 0,\n" +
                    "     colors: ['5D35FC'],\n" +
                    "     backgroundColor: {\n" +
                    "       fill: 'gainsboro',\n" +
//                    "       stroke: '85828F',\n" +
//                    "       strokeWidth: 3\n" +
                    "     },\n" +
                    "     datalessRegionColor: 'A29BBD',\n" +
                    "     defaultColor: 'A29BBD',\n" +
                    "     keepAspectRatio: 'true',\n" +
                    "     enableInteractivity: 'false',\n" +
                    "     legend: 'hide',\n" +
                    "   };\n" +
                    "   var map_chart = new google.visualization.GeoChart(document.getElementById('artists_map'));\n" +
                    "\n" +
                    "\n" +
                    "   google.visualization.events.addListener(map_chart, 'ready', function() {\n" +
                    "     artists_map.innerHTML = '<img src=\"' + map_chart.getImageURI() + '\">';\n" +
                    "     console.log(artists_map.innerHTML);\n" +
                    "   });\n" +
                    "\n" +
                    "   map_chart.draw(map_data, map_options);\n" +
                    " }";
    }

    /** makes and sets the full JS for the embedded charts **/
    private void setChartsEmbed() {
        this.chartsEmbed =
                " google.charts.load('current', {\n" +
                "        'packages':['geochart', 'corechart'],\n" +
                "        'mapsApiKey': '" + Confidential.ReturnObject_API_KEY + "'\n" +
                "      });" + "\n" + mkFeaturesEmbed() + "\n" + mkMapEmbed();
    }
}
