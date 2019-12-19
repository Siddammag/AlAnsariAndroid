package app.alansari.models;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SelectItemData {

    private String itemId;
    private int type;
    private String name, image, description;

    public SelectItemData() {
    }

    public SelectItemData(String itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    public SelectItemData(String itemId, String name, int type) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
    }

    public SelectItemData(String itemId, String name, String description, int type) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}