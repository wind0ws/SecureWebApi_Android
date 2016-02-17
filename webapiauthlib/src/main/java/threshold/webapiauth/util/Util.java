package threshold.webapiauth.util;

import threshold.webapiauth.Configuration;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Time Format Util
 */
public class Util {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static SimpleDateFormat dateTimeFormat;

    private static SimpleDateFormat greenwichDateFormat;

    public static String getUtcTime() {
        if (dateTimeFormat == null) {
        dateTimeFormat = new SimpleDateFormat(Configuration.DateFormat, Locale.ENGLISH);
        dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        }
        return dateTimeFormat.format(new Date());
    }

    public static String getGreenwichDate() {
        if (greenwichDateFormat == null) {
            greenwichDateFormat = new SimpleDateFormat(Configuration.DateFormat, Locale.US);
            greenwichDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return greenwichDateFormat.format(Calendar.getInstance().getTime());
    }


}
