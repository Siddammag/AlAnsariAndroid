package app.alansari.models;

/**
 * Created by Teja on 13-10-2016.
 */

public class LeftMenu {
    String name;
    int picId;

    public LeftMenu(String name, int picId) {
        this.name = name;
        this.picId = picId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }
}
