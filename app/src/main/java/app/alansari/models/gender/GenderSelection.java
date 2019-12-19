package app.alansari.models.gender;

import android.os.Parcel;
import android.os.Parcelable;

public class GenderSelection implements Parcelable {
    String name;
    int id;

    public GenderSelection(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected GenderSelection(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GenderSelection> CREATOR = new Creator<GenderSelection>() {
        @Override
        public GenderSelection createFromParcel(Parcel in) {
            return new GenderSelection(in);
        }

        @Override
        public GenderSelection[] newArray(int size) {
            return new GenderSelection[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicId() {
        return id;
    }

    public void setPicId(int picId) {
        this.id = picId;
    }
}


