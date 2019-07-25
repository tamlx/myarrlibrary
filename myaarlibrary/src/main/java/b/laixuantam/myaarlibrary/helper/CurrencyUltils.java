package b.laixuantam.myaarlibrary.helper;

public class CurrencyUltils {

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
