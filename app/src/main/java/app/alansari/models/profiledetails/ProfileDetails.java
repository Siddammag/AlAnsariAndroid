package app.alansari.models.profiledetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ProfileDetails implements Parcelable {

	@SerializedName("MEM_SHIP_CARD_NO")
	private String mEMSHIPCARDNO;

	@SerializedName("LOYALTY_POINTS")
	private String lOYALTYPOINTS;

	@SerializedName("FIRST_NAME")
	private String fIRSTNAME;

	@SerializedName("LAST_NAME")
	private String lASTNAME;

	@SerializedName("TEMPLATE_LIST")
	private List<TEMPLATELISTItem> tEMPLATELIST;

	protected ProfileDetails(Parcel in) {
		mEMSHIPCARDNO = in.readString();
		fIRSTNAME = in.readString();
		lASTNAME = in.readString();
		lOYALTYPOINTS=in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mEMSHIPCARDNO);
		dest.writeString(fIRSTNAME);
		dest.writeString(lASTNAME);
		dest.writeString(lOYALTYPOINTS);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ProfileDetails> CREATOR = new Creator<ProfileDetails>() {
		@Override
		public ProfileDetails createFromParcel(Parcel in) {
			return new ProfileDetails(in);
		}

		@Override
		public ProfileDetails[] newArray(int size) {
			return new ProfileDetails[size];
		}
	};

	public void setMEMSHIPCARDNO(String mEMSHIPCARDNO){
		this.mEMSHIPCARDNO = mEMSHIPCARDNO;
	}

	public String getMEMSHIPCARDNO(){
		return mEMSHIPCARDNO;
	}

	public void setLOYALTYPOINTS(String lOYALTYPOINTS){
		this.lOYALTYPOINTS = lOYALTYPOINTS;
	}

	public String getLOYALTYPOINTS(){
		return lOYALTYPOINTS;
	}

	public void setFIRSTNAME(String fIRSTNAME){
		this.fIRSTNAME = fIRSTNAME;
	}

	public String getFIRSTNAME(){
		return fIRSTNAME;
	}

	public void setLASTNAME(String lASTNAME){
		this.lASTNAME = lASTNAME;
	}

	public String getLASTNAME(){
		return lASTNAME;
	}

	public void setTEMPLATELIST(List<TEMPLATELISTItem> tEMPLATELIST){
		this.tEMPLATELIST = tEMPLATELIST;
	}

	public List<TEMPLATELISTItem> getTEMPLATELIST(){
		return tEMPLATELIST;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"mEM_SHIP_CARD_NO = '" + mEMSHIPCARDNO + '\'' + 
			",lOYALTY_POINTS = '" + lOYALTYPOINTS + '\'' + 
			",fIRST_NAME = '" + fIRSTNAME + '\'' + 
			",lAST_NAME = '" + lASTNAME + '\'' + 
			",tEMPLATE_LIST = '" + tEMPLATELIST + '\'' + 
			"}";
		}

//Class Created


	public static class TEMPLATELISTItem implements Parcelable {

		@SerializedName("CATEGORY_ID")
		private String cATEGORYID;

		@SerializedName("CATEGORY_DESC")
		private String cATEGORYDESC;

		@SerializedName("ACTION")
		private String aCTION;

		@SerializedName("SCREEN_TYPE")
		private String sCREENTYPE;

		@SerializedName("TEMPALTE_DETAILS")
		private List<TEMPALTEDETAILSItem> tEMPALTEDETAILS;

		@SerializedName("IS_DATA_VALID")
		private boolean iSDATAVALID;

		protected TEMPLATELISTItem(Parcel in) {
			cATEGORYID = in.readString();
			cATEGORYDESC = in.readString();
			aCTION = in.readString();
			sCREENTYPE = in.readString();
			tEMPALTEDETAILS = in.createTypedArrayList(TEMPALTEDETAILSItem.CREATOR);
			iSDATAVALID = in.readByte() != 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(cATEGORYID);
			dest.writeString(cATEGORYDESC);
			dest.writeString(aCTION);
			dest.writeString(sCREENTYPE);
			dest.writeTypedList(tEMPALTEDETAILS);
			dest.writeByte((byte) (iSDATAVALID ? 1 : 0));
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Creator<TEMPLATELISTItem> CREATOR = new Creator<TEMPLATELISTItem>() {
			@Override
			public TEMPLATELISTItem createFromParcel(Parcel in) {
				return new TEMPLATELISTItem(in);
			}

			@Override
			public TEMPLATELISTItem[] newArray(int size) {
				return new TEMPLATELISTItem[size];
			}
		};

		public void setCATEGORYID(String cATEGORYID){
			this.cATEGORYID = cATEGORYID;
		}

		public String getCATEGORYID(){
			return cATEGORYID;
		}

		public void setCATEGORYDESC(String cATEGORYDESC){
			this.cATEGORYDESC = cATEGORYDESC;
		}

		public String getCATEGORYDESC(){
			return cATEGORYDESC;
		}

		public void setACTION(String aCTION){
			this.aCTION = aCTION;
		}

		public String getACTION(){
			return aCTION;
		}

		public void setSCREENTYPE(String sCREENTYPE){
			this.sCREENTYPE = sCREENTYPE;
		}

		public String getSCREENTYPE(){
			return sCREENTYPE;
		}

		public void setTEMPALTEDETAILS(List<TEMPALTEDETAILSItem> tEMPALTEDETAILS){
			this.tEMPALTEDETAILS = tEMPALTEDETAILS;
		}

		public List<TEMPALTEDETAILSItem> getTEMPALTEDETAILS(){
			return tEMPALTEDETAILS;
		}

		public void setISDATAVALID(boolean iSDATAVALID){
			this.iSDATAVALID = iSDATAVALID;
		}

		public boolean isISDATAVALID(){
			return iSDATAVALID;
		}

		@Override
		public String toString(){
			return
					"TEMPLATELISTItem{" +
							"cATEGORY_ID = '" + cATEGORYID + '\'' +
							",cATEGORY_DESC = '" + cATEGORYDESC + '\'' +
							",aCTION = '" + aCTION + '\'' +
							",sCREEN_TYPE = '" + sCREENTYPE + '\'' +
							",tEMPALTE_DETAILS = '" + tEMPALTEDETAILS + '\'' +
							",iS_DATA_VALID = '" + iSDATAVALID + '\'' +
							"}";
		}

//class Created

		public static class TEMPALTEDETAILSItem implements Parcelable {

			@SerializedName("FIELD")
			private String fIELD;

			@SerializedName("MANDATORY")
			private String mANDATORY;

			@SerializedName("EDIT_ALLOWED")
			private String eDITALLOWED;

			@SerializedName("PREFIX")
			private Object pREFIX;

			@SerializedName("LENGTH")
			private String lENGTH;

			@SerializedName("TYPE")
			private String tYPE;

			@SerializedName("INPUT_TYPE")
			private String iNPUTTYPE;

			@SerializedName("FIELD_ID")
			private String fIELDID;

			@SerializedName("FIELD_VALUE")
			private String fIELDVALUE;

			protected TEMPALTEDETAILSItem(Parcel in) {
				fIELD = in.readString();
				mANDATORY = in.readString();
				eDITALLOWED = in.readString();
				tYPE = in.readString();
				iNPUTTYPE = in.readString();
				fIELDID = in.readString();
				fIELDVALUE = in.readString();
				lENGTH=in.readString();
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(fIELD);
				dest.writeString(mANDATORY);
				dest.writeString(eDITALLOWED);
				dest.writeString(tYPE);
				dest.writeString(iNPUTTYPE);
				dest.writeString(fIELDID);
				dest.writeString(fIELDVALUE);
				dest.writeString(lENGTH);
			}

			@Override
			public int describeContents() {
				return 0;
			}

			public static final Creator<TEMPALTEDETAILSItem> CREATOR = new Creator<TEMPALTEDETAILSItem>() {
				@Override
				public TEMPALTEDETAILSItem createFromParcel(Parcel in) {
					return new TEMPALTEDETAILSItem(in);
				}

				@Override
				public TEMPALTEDETAILSItem[] newArray(int size) {
					return new TEMPALTEDETAILSItem[size];
				}
			};

			public void setFIELD(String fIELD){
				this.fIELD = fIELD;
			}

			public String getFIELD(){
				return fIELD;
			}

			public void setMANDATORY(String mANDATORY){
				this.mANDATORY = mANDATORY;
			}

			public String getMANDATORY(){
				return mANDATORY;
			}

			public void setEDITALLOWED(String eDITALLOWED){
				this.eDITALLOWED = eDITALLOWED;
			}

			public String getEDITALLOWED(){
				return eDITALLOWED;
			}

			public void setPREFIX(Object pREFIX){
				this.pREFIX = pREFIX;
			}

			public Object getPREFIX(){
				return pREFIX;
			}

			public void setLENGTH(String lENGTH){
				this.lENGTH = lENGTH;
			}

			public String getLENGTH(){
				return lENGTH;
			}

			public void setTYPE(String tYPE){
				this.tYPE = tYPE;
			}

			public String getTYPE(){
				return tYPE;
			}

			public void setINPUTTYPE(String iNPUTTYPE){
				this.iNPUTTYPE = iNPUTTYPE;
			}

			public String getINPUTTYPE(){
				return iNPUTTYPE;
			}

			public void setFIELDID(String fIELDID){
				this.fIELDID = fIELDID;
			}

			public String getFIELDID(){
				return fIELDID;
			}

			public void setFIELDVALUE(String fIELDVALUE){
				this.fIELDVALUE = fIELDVALUE;
			}

			public String getFIELDVALUE(){
				return fIELDVALUE;
			}

			@Override
			public String toString(){
				return
						"TEMPALTEDETAILSItem{" +
								"fIELD = '" + fIELD + '\'' +
								",mANDATORY = '" + mANDATORY + '\'' +
								",eDIT_ALLOWED = '" + eDITALLOWED + '\'' +
								",pREFIX = '" + pREFIX + '\'' +
								",lENGTH = '" + lENGTH + '\'' +
								",tYPE = '" + tYPE + '\'' +
								",iNPUT_TYPE = '" + iNPUTTYPE + '\'' +
								",fIELD_ID = '" + fIELDID + '\'' +
								",fIELD_VALUE = '" + fIELDVALUE + '\'' +
								"}";
			}
		}


	}

}