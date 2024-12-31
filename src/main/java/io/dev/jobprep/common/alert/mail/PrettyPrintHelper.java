package io.dev.jobprep.common.alert.mail;

public class PrettyPrintHelper {

    public static final String BOLD_STRT = "<h3>";
    public static final String BOLD_END = "</h3>";
    public static final String LIGHT_BOLD_STRT = "<p><b>";
    public static final String LIGHT_BOLD_MID = "</b>";
    public static final String LIGHT_BOLD_END = "</p>";
    public static final String EMPTY = " ";
    public static final String LINE_BREAK = "\n";

    private PrettyPrintHelper() {
        throw new UnsupportedOperationException("Cannot instantiate this interface!");
    }

}
