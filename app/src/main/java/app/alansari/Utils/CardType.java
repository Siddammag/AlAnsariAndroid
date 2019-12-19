package app.alansari.Utils;

import java.util.regex.Pattern;

/**
 * Created by Parveen Dala on 13 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class CardType {

/*  protected static final String PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$";

    protected static final String PATTERN_MASTER_CARD = "^5[1-5][0-9]{14}$";

    protected static final String PATTERN_MAESTRO_CARD = "^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d+$";

    protected static final String PATTERN_JCB_CARD = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";

    protected static final String PATTERN_AMERICAN_EXPRESS = "^3[47][0-9]{13}$"; */

    public static final String VISA = "VISA";
    public static final String MASTERCARD = "MASTER";
    public static final String MAESTRO = "MAESTRO";
    public static final String JCB = "JCB";
    public static final String AMERICAN_EXPRESS = "AMERICAN EXPRESS";
    public static final String DINERS_CLUB = "DINERS CLUB";
    public static final String DISCOVER = "DISCOVER";
    public static final String UNION = "UNION";

    protected static final String PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$";

    protected static final String PATTERN_MASTER_CARD = "^((222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)|(5[1-5][0-9][0-9]))\\d+$";

    protected static final String PATTERN_MAESTRO_CARD = "^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d+$";

    protected static final String PATTERN_JCB_CARD = "^35[3-8][0-9]{13}$";

    protected static final String PATTERN_JCB_CARD1 = "^(3528|3529)\\d+$";

    protected static final String PATTERN_AMERICAN_EXPRESS = "^3[47][0-9][0-9]{13}$";

    protected static final String PATTERN_DINERS_CLUB = "^((36[0-9][0-9])|(30[0-5][0-9])|(3095)|(3[8-9][0-9][0-9]))\\d+$";

    protected static final String PATTERN_DISCOVER = "^((6011)|(6[4-5][0-9][09]))\\d+$";

    protected static final String PATTERN_UNION = "^(62[0-9][0-9])\\d+$";


    public static String getCardType(String cardNumber) {
        if (cardNumber.length() > 0) {
            if (Pattern.compile(PATTERN_MASTER_CARD).matcher(cardNumber).matches()) {
                return MASTERCARD;
            } else if (Pattern.compile(PATTERN_VISA).matcher(cardNumber).matches()) {
                return VISA;
            } else if (Pattern.compile(PATTERN_MAESTRO_CARD).matcher(cardNumber).matches()) {
                return MAESTRO;
            } else if (Pattern.compile(PATTERN_JCB_CARD).matcher(cardNumber).matches()) {
                return JCB;
            } else if (Pattern.compile(PATTERN_JCB_CARD1).matcher(cardNumber).matches()) {
                return JCB;
            } else if (Pattern.compile(PATTERN_AMERICAN_EXPRESS).matcher(cardNumber).matches()) {
                return AMERICAN_EXPRESS;
            } else if (Pattern.compile(PATTERN_DINERS_CLUB).matcher(cardNumber).matches()) {
                return DINERS_CLUB;
            } else if (Pattern.compile(PATTERN_DISCOVER).matcher(cardNumber).matches()) {
                return DISCOVER;
            }else if (Pattern.compile(PATTERN_UNION).matcher(cardNumber).matches()) {
                return UNION;
            }
        }
        return null;
    }

    public static int getCardLogo(String cardType) {
        switch (cardType) {
            case VISA:
                return app.alansari.R.drawable.icn_card_visa;
            case MASTERCARD:
                return app.alansari.R.drawable.svg_card_master;
            case MAESTRO:
                return app.alansari.R.drawable.svg_card_maestro;
            case JCB:
                return app.alansari.R.drawable.icn_card_jcb;
            case AMERICAN_EXPRESS:
                return app.alansari.R.drawable.icn_card_ae;
            case DINERS_CLUB:
                return app.alansari.R.drawable.icn_card_diner;
            case DISCOVER:
                return app.alansari.R.drawable.icn_card_discover;
            case UNION:
                return app.alansari.R.drawable.icn_card_union;
            default:
                return 0;
        }
    }
}
