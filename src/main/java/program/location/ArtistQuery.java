package program.location;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ArtistQuery {

    /** the two forms of the query **/
    private String decoded;
    private String encoded;

    /** the two forms of the name in the form 'surname, firstname' **/
    private String sortDecoded = null;
    private String sortEncoded = null;


    public ArtistQuery(String query) {
        query = unify(query);
        this.decoded = query;
        this.encoded = encode(query);

        String[] splitQuery = query.split(" ");
        if (splitQuery.length == 2) {
            this.sortDecoded = splitQuery[1] + ", " + splitQuery[0];
            this.sortEncoded = encode(sortDecoded);
        } else if ((splitQuery.length>=2) && (splitQuery[0].equalsIgnoreCase("the"))) {
            /* removes 'the' */
            StringBuilder sortName = new StringBuilder();
            for (int i=1; i<splitQuery.length; i++) {
                sortName.append(splitQuery[i]).append(" ");
            }
            this.sortDecoded = sortName.toString();
            this.sortEncoded = encode(sortDecoded);
        }
    }

    /** returns the encoded form of the query **/
    public String getEncoded() { return encoded; }

    /** returns the decoded sort name **/
    public String getDecodedSortName() { return sortDecoded; }

    /** returns the encoded sort name **/
    public String getEncodedSortName() { return sortEncoded; }

    /** encodes string for url **/
    public static String encode(String input) {
        try {
            return URLEncoder.encode(input, String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Was unable to encode: " + input);
        }
    }

    /** deals with edge cases where similar but different characters are used **/
    public static String unify(String input) {
        final String chosenHyphen = "-";
        final String[] incorrectHyphens = new String[] {"‐", "‒", "—", "―", "–"};
        input = makeReplacements(input, chosenHyphen, incorrectHyphens);

        final String chosenApostrophe = "'";
        final String[] incorrectApostrophes = new String[] {"’", "‘"};
        input = makeReplacements(input, chosenApostrophe, incorrectApostrophes);

        final String chosenQuotes = "\"";
        final String[] incorrectQuotes = new String[] {"“", "”", "«", "»"};
        input = makeReplacements(input, chosenQuotes, incorrectQuotes);

        return input;
    }

    /** replaces all substrings of 'input' listed in 'toReplace' with 'replacement' **/
    private static String makeReplacements(String input, String replacement, String[] toReplace) {
        for (String toReplaceNow : toReplace) {
            input = input.replace(toReplaceNow, replacement);
        }
        return input;
    }
    @Override
    public String toString() {
        return decoded;
    }
}
