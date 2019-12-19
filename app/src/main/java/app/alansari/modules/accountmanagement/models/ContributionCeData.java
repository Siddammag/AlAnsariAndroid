package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 12 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class ContributionCeData implements Parcelable {

    public static final Parcelable.Creator<ContributionCeData> CREATOR = new Parcelable.Creator<ContributionCeData>() {
        @Override
        public ContributionCeData createFromParcel(Parcel source) {
            return new ContributionCeData(source);
        }

        @Override
        public ContributionCeData[] newArray(int size) {
            return new ContributionCeData[size];
        }
    };
    /**
     * CONTRIBUTION_ID : 2
     * CONTRIBUTION_AMT : 605
     */

    @SerializedName("CONTRIBUTION_ID")
    private int contributionId;
    @SerializedName("CONTRIBUTION_AMT")
    private String contributionAmount;

    public ContributionCeData() {
    }

    protected ContributionCeData(Parcel in) {
        this.contributionId = in.readInt();
        this.contributionAmount = in.readString();
    }

    public int getContributionId() {
        return contributionId;
    }

    public void setContributionId(int contributionId) {
        this.contributionId = contributionId;
    }

    public String getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(String contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.contributionId);
        dest.writeString(this.contributionAmount);
    }
}
