package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class PopUpData implements Parcelable {
  /*
  
  {
"SESSION_TIMER":null,
"OTP_TIMER":null,
"SESSION_TIMER_MSG":null,
"OTP_TIMER_MSG":null,
"RESULT":[
{
"popUpPkId":20,
"popUpName":"before_login_promotions",
"title":"be the lucky winner !!!!",
"message":"visit any al Ansari exchange branch and register yourself for mobile app. triple your chances for lucky draw.",
"userCount":"319",
"popUpType":"G",
"loginType":"B",
"imageUrl":null,
"activeInd":"A",
"registrationMode":null,
"createdBy":"100260026",
"createdDate":1549345107000,
"creatorName":"MIR ABUL QASIM ABEDI",
"approvedBy":"100260539",
"approvedDate":1549345510000,
"approvedStatus":"A",
"approverName":"PRAKASH BIRUDALA",
"remarks":"approve ",
"modifiedBy":null,
"modifierName":null,
"modifiedDate":null,
"imageUrlLocal":null,
"popUpStartDate":1549310400000,
"popUpEndDate":1551297600000,
"screenTypeKey":"WU",
"screenTypeValue":"Western union"
}
],
"BENEF_PIC_FILE_EXTN":null,
"BENEF_PIC_FILE_SIZE":null,
"BENEF_PIC_COMP_LIMIT":null,
"BENEF_PIC_ENC_ENABLED":null,
"STATUS_CODE":257,
"PROMO_ACTIVE":null,
"PROMO_LIST_ACTIVE":null,
"NEXT_RECORD":0,
"WU_CARD_NO":null,
"WU_SENDER_NAME":null,
"WU_LOOKUP_PROMO_CODE":null,
"WU_CARD_NEXT_RECEIVER_CONTEXT":null,
"WU_TOTAL_POINTS_EARNED":null,
"WU_RECEIVER_MORE_FLAG":null,
"STATUS_MSG":"SUCCESS",
"USER_STATUS":null,
"OTP":null,
"STATUS":null,
"MESSAGE":"SUCCESS",
"AMOUNT":null,
"ID":null
}
  
  
  */

    @SerializedName("title")
    String title;

    @SerializedName("imageUrl")
    String imageURL;

    @SerializedName("message")
    String message;

    @SerializedName("screenTypeKey")
    String screen_type_key;
    @SerializedName("POP_URL")
    String popUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreen_type_key() {
        return screen_type_key;
    }

    public void setScreen_type_key(String screen_type_key) {
        this.screen_type_key = screen_type_key;
    }

    public PopUpData(String title, String imageURL, String message) {
        this.title = title;
        this.imageURL = imageURL;
        this.message = message;
    }

    public String getPopUrl() {
        return popUrl;
    }

    public void setPopUrl(String popUrl) {
        this.popUrl = popUrl;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.imageURL);
        dest.writeString(this.message);
        dest.writeString(this.screen_type_key);
        dest.writeString(this.popUrl);
    }

    protected PopUpData(Parcel in) {
        this.title = in.readString();
        this.imageURL = in.readString();
        this.message = in.readString();
        this.screen_type_key = in.readString();
        this.popUrl = in.readString();
    }

  public static final Parcelable.Creator<PopUpData> CREATOR = new Parcelable.Creator<PopUpData>() {
        @Override
        public PopUpData createFromParcel(Parcel source) {
            return new PopUpData(source);
        }

        @Override
        public PopUpData[] newArray(int size) {
            return new PopUpData[size];
        }
    };


}
