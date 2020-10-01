package b.laixuantam.myaarlibrary.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyFormater {
    private static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.UK);
    private static DecimalFormat currency = new DecimalFormat("#,###.###", symbols);
    private static NumberFormat numbers = NumberFormat.getNumberInstance(Locale.UK);
    private static NumberFormat percents = NumberFormat.getPercentInstance(Locale.UK);

    static {
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
    }

    public static synchronized String formatCurrency(double value) {
        return currency.format(value);
    }

    public static synchronized double parseCurrency(String value) {
        try {
            return currency.parse(value).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized double round3(double value) {
        return (double) Math.round(value * 1000) / 1000;
    }

    public static synchronized String formatQuantity(double value) {
        return formatNumber(round3(value));
    }

    public static synchronized String formatNumber(double value) {
        numbers.setGroupingUsed(false);
        numbers.setMinimumFractionDigits(0);
        numbers.setMaximumFractionDigits(3);
        return numbers.format(value);
    }

    public static synchronized double parseNumber(String value) throws Exception {
        return numbers.parse(value).doubleValue();
    }

    public static synchronized String formatPercent(double value) {
        percents.setMinimumFractionDigits(0);
        percents.setMaximumFractionDigits(2);
        return percents.format(value);
    }

    public static synchronized String formatPercent(double value, boolean addSign) {
        if (addSign) {
            if (value < 0) {
                return formatPercent(value);
            } else {
                return String.format("+%s", formatPercent(value));
            }
        } else {
            return formatPercent(value);
        }
    }

    public static synchronized boolean checkIntValue(double input) {
        boolean isIntegerQuantity = false;
        if ((input == Math.floor(input)) && !Double.isInfinite(input)) {
            // integral type
            isIntegerQuantity = true;
        }
        return isIntegerQuantity;
    }

    private static String[] mangso = new String[]{"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

    private static String dochangchuc(int so, boolean daydu) {
        String chuoi = "";
        int chuc = (int) Math.floor(so / 10);
        int donvi = so % 10;
        if (chuc > 1) {
            chuoi = " " + mangso[chuc] + " mươi";
            if (donvi == 1) {
                chuoi += " mốt";
            }
        } else if (chuc == 1) {
            chuoi = " mười";
            if (donvi == 1) {
                chuoi += " một";
            }
        } else if (daydu && donvi > 0) {
            chuoi = " lẻ";
        }
        if (donvi == 5 && chuc > 1) {
            chuoi += " lăm";
        } else if (donvi > 1 || (donvi == 1 && chuc == 0)) {
            chuoi += " " + mangso[donvi];
        }
        return chuoi;
    }

    private static String docblock(int so, boolean daydu) {
        String chuoi = "";
        int tram = (int) Math.floor(so / 100);
        so = so % 100;
        if (daydu || tram > 0) {
            chuoi = " " + mangso[tram] + " trăm";
            chuoi += dochangchuc(so, true);
        } else {
            chuoi = dochangchuc(so, false);
        }
        return chuoi;
    }

    private static String dochangtrieu(int so, boolean daydu) {
        String chuoi = "";
        int trieu = (int) Math.floor(so / 1000000);
        so = so % 1000000;
        if (trieu > 0) {
            chuoi = docblock(trieu, daydu) + " triệu";
            daydu = true;
        }
        int nghin = (int) Math.floor(so / 1000);
        so = so % 1000;
        if (nghin > 0) {
            chuoi += docblock(nghin, daydu) + " nghìn";
            daydu = true;
        }
        if (so > 0) {
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
    public static String convertMoneyString(int so) {
        if (so == 0) {
            return mangso[0];
        }
        String chuoi = "", hauto = "";
        do {
            int ty = so % 1000000000;
            so = (int) Math.floor(so / 1000000000);
            if (so > 0) {
                chuoi = dochangtrieu(ty, true) + hauto + chuoi;
            } else {
                chuoi = dochangtrieu(ty, false) + hauto + chuoi;
            }
            hauto = " tỷ";
        } while (so > 0);
        // In hoa chữ cái đầu
        chuoi = chuoi.trim();
        if (chuoi != "") {
            chuoi = chuoi.substring(0, 1).toUpperCase() + chuoi.substring(1);
        }
        return chuoi;
    }

    public static String LONG_NEGOTIATE = "Thương lượng";
    public static String SHORT_NEGOTIATE = "TL";
    public static int SHORT_PRICE = 1;
    public static int LONG_PRICE = 2;

    public static String getStringPrice(Long price, int option) {
        if (option == SHORT_PRICE) {
            float fPrice;
            if (price >= 1000000000) {
                fPrice = (float) price / 1000000000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + " Tỉ";
                } else {
                    return fPrice + " Tỉ";
                }
            } else if (price >= 1000000) {
                fPrice = (float) price / 1000000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + " Tr";
                } else {
                    return fPrice + " Tr";
                }
            } else if (price > 1000) {
                fPrice = (float) price / 1000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + ".000";
                } else {
                    return fPrice + ".000";
                }
            } else {
                return SHORT_NEGOTIATE;
            }
        } else {
            float fPrice;
            if (price >= 1000000000) {
                fPrice = (float) price / 1000000000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + " Tỉ";
                } else {
                    return fPrice + " Tỉ";
                }
            } else if (price >= 1000000) {
                fPrice = (float) price / 1000000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + " Triệu";
                } else {
                    return fPrice + " Triệu";
                }
            } else if (price > 1000) {
                fPrice = (float) price / 1000;
                fPrice = (float) (Math.round(fPrice * 100.0) / 100.0);
                if (fPrice == (int) fPrice) {
                    return (int) fPrice + ".000";
                } else {
                    return fPrice + ".000";
                }
            } else {
                return LONG_NEGOTIATE;
            }
        }
    }
}
