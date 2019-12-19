package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 20 February, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class FaqData implements Parcelable {

    /**
     * CHANNEL_ID : null
     * AREX_WS_FAQ_PK_ID : 1
     * QUESTION : DUMMY WHAT IS YOUR NAME
     * ANSWER : DUMMY TEST MY NAME IS KHAN
     */

    @SerializedName("AREX_WS_FAQ_PK_ID")
    private int id;
    @SerializedName("QUESTION")
    private String question;
    @SerializedName("ANSWER")
    private String answer;
    private int isReading;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIsReading() {
        return isReading;
    }

    public void setIsReading(int isReading) {
        this.isReading = isReading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeInt(this.isReading);
    }

    public FaqData() {
    }

    protected FaqData(Parcel in) {
        this.id = in.readInt();
        this.question = in.readString();
        this.answer = in.readString();
        this.isReading = in.readInt();
    }

    public static final Parcelable.Creator<FaqData> CREATOR = new Parcelable.Creator<FaqData>() {
        @Override
        public FaqData createFromParcel(Parcel source) {
            return new FaqData(source);
        }

        @Override
        public FaqData[] newArray(int size) {
            return new FaqData[size];
        }
    };
}
