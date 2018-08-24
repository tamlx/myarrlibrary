/*
 * (c) Copyright GoTechCom 2016
 */

package b.laixuantam.myaarlibrary.helper;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageHelper
{
    public LanguageHelper()
    {
    }

    public static void setup(Resources resources)
    {
        String deviceLocale = LanguageHelper.getDashFormatFromLocale(resources.getConfiguration().locale);
        String value = LanguageHelper.getValidLanguage(deviceLocale);
        String language = LanguageHelper.toDashFormat(value);

        Locale locale = LanguageHelper.getLocaleFromDashFormat(language);

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static String[] getAvailableLanguages()
    {
        String[] result = new String[5];

        result[0] = getDashFormatFromLocale(Locale.UK);
        result[1] = getDashFormatFromLocale(Locale.GERMANY);
        result[2] = getDashFormatFromLocale(Locale.FRANCE);
        result[3] = getDashFormatFromLocale(Locale.ITALY);
        result[4] = getDashFormatFromLocale(new Locale("es", "ES"));

        return result;
    }

    public static String getValidLanguage(String language)
    {
        String formattedLanguage = toDashFormat(language);
        String[] availableLanguages = getAvailableLanguages();

        try
        {
            for (String current : availableLanguages)
            {
                String currentLanguage = current.split("-")[0];
                String languageLanguage = formattedLanguage.split("-")[0];

                if (TextUtils.equals(currentLanguage, languageLanguage))
                {
                    return current;
                }
            }
        }
        catch (Exception e)
        {
            MyLog.e(e);
        }

        return availableLanguages[0];
    }

    public static String getDashFormatFromLocale(Locale locale)
    {
        return String.format("%s-%s", locale.getLanguage().toLowerCase(), locale.getCountry().toLowerCase());
    }

    public static Locale getLocaleFromDashFormat(String value)
    {
        String[] parts = value.split("-");

        return new Locale(parts[0], parts[1].toUpperCase());
    }

    public static String toDashFormat(String text)
    {
        return text.replace("_", "-").toLowerCase();
    }

//    public static String getCountryByCode(String code)
//    {
//        Locale currentLocale = AppProvider.getPreferences().getLanguageAsLocale();
//
//        Locale locale = new Locale("", code);
//
//        return locale.getDisplayCountry(currentLocale);
//    }
}
