package app.alansari.keypadview;

/**
 * Created by Parveen Dala on 06 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public enum Password {

    NUMBER(0),
    TEXT(1);

    private int type;

    private Password(int type) {
        this.type = type;
    }

}
