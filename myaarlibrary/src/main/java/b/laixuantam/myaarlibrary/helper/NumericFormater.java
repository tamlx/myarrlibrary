package b.laixuantam.myaarlibrary.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by thanhle on 6/16/16.
 */
public class NumericFormater
{
    private static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    private static DecimalFormat currency = new DecimalFormat("#,###.###", symbols);
    private static NumberFormat numbers = NumberFormat.getNumberInstance(Locale.getDefault());
    private static NumberFormat percents = NumberFormat.getPercentInstance(Locale.getDefault());

    {
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
    }

    public static synchronized String formatCurrency(double value)
    {
        return currency.format(value).replaceAll(",", ".");
    }

    public static String formatMoney(long s)
    {
        NumberFormat usFormat = NumberFormat.getIntegerInstance(Locale.US);
        String money = usFormat.format(s);
        money = money.replace(",", ".");
        return money;
    }

    public static String formatDigits(String value)
    {
        String digits = value.toString().replaceAll("\\D", "");
        return digits;
    }

    public static synchronized double parseCurrency(String value)
    {
        if (value.contains("."))
        {
            value = value.replace(".", ",");
        }

        try
        {
            return currency.parse(value).doubleValue();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized double round2(double value)
    {
        return (double) Math.round(value * 100) / 100;
    }

    public static synchronized String round2ToString(double value)
    {
        return formatNumber(round2(value));
    }

    public static synchronized String formatNumber(double value)
    {
        numbers.setGroupingUsed(false);
        numbers.setMinimumFractionDigits(0);
        numbers.setMaximumFractionDigits(3);
        return numbers.format(value);
    }

    public static synchronized double parseNumber(String value) throws Exception
    {
        return numbers.parse(value).doubleValue();
    }

    public static synchronized String formatPercent(double value)
    {
        percents.setMinimumFractionDigits(0);
        percents.setMaximumFractionDigits(2);
        return percents.format(value);
    }

    public static synchronized String formatPercent(double value, boolean addSign)
    {
        if (addSign)
        {
            if (value < 0)
            {
                return formatPercent(value);
            }
            else
            {
                return String.format("+%s", formatPercent(value));
            }
        }
        else
        {
            return formatPercent(value);
        }
    }

    public static synchronized boolean checkIntValue(double input)
    {
        boolean isIntegerQuantity = false;
        if ((input == Math.floor(input)) && !Double.isInfinite(input))
        {
            // integral type
            isIntegerQuantity = true;
        }
        return isIntegerQuantity;
    }

    private static String[] mangso = new String[] {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

    private static String dochangchuc(int so, boolean daydu)
    {
        String chuoi = "";
        int chuc = (int) Math.floor(so / 10);
        int donvi = so % 10;
        if (chuc > 1)
        {
            chuoi = " " + mangso[chuc] + " mươi";
            if (donvi == 1)
            {
                chuoi += " mốt";
            }
        }
        else if (chuc == 1)
        {
            chuoi = " mười";
            if (donvi == 1)
            {
                chuoi += " một";
            }
        }
        else if (daydu && donvi > 0)
        {
            chuoi = " lẻ";
        }
        if (donvi == 5 && chuc > 1)
        {
            chuoi += " lăm";
        }
        else if (donvi > 1 || (donvi == 1 && chuc == 0))
        {
            chuoi += " " + mangso[donvi];
        }
        return chuoi;
    }

    private static String docblock(int so, boolean daydu)
    {
        String chuoi = "";
        int tram = (int) Math.floor(so / 100);
        so = so % 100;
        if (daydu || tram > 0)
        {
            chuoi = " " + mangso[tram] + " trăm";
            chuoi += dochangchuc(so, true);
        }
        else
        {
            chuoi = dochangchuc(so, false);
        }
        return chuoi;
    }

    private static String dochangtrieu(int so, boolean daydu)
    {
        String chuoi = "";
        int trieu = (int) Math.floor(so / 1000000);
        so = so % 1000000;
        if (trieu > 0)
        {
            chuoi = docblock(trieu, daydu) + " triệu";
            daydu = true;
        }
        int nghin = (int) Math.floor(so / 1000);
        so = so % 1000;
        if (nghin > 0)
        {
            chuoi += docblock(nghin, daydu) + " nghìn";
            daydu = true;
        }
        if (so > 0)
        {
            chuoi += docblock(so, daydu);
        }
        return chuoi;
    }

    /**
     * Hàm đọc tiền thành chữ
     *
     * @param so
     * @return
     */
    public static String convertMoneyString(int so)
    {
        if (so == 0)
        {
            return mangso[0];
        }
        String chuoi = "", hauto = "";
        do
        {
            int ty = so % 1000000000;
            so = (int) Math.floor(so / 1000000000);
            if (so > 0)
            {
                chuoi = dochangtrieu(ty, true) + hauto + chuoi;
            }
            else
            {
                chuoi = dochangtrieu(ty, false) + hauto + chuoi;
            }
            hauto = " tỷ";
        } while (so > 0);
        // In hoa chữ cái đầu
        chuoi = chuoi.trim();
        if (chuoi != "")
        {
            chuoi = chuoi.substring(0, 1).toUpperCase() + chuoi.substring(1);
        }
        return chuoi;
    }
}
