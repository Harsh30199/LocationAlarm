package com.example.mahesh.alarmlocator.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Alarmmodel implements Parcelable{
    @SerializedName("id")
    private String id;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("text")
    private String text;
    @SerializedName("address")
    private String address;
    @SerializedName("isDisable")
    private boolean isDisable;
    @SerializedName("stime")
    private String stime;
    @SerializedName("sDate")
    public Date sDate;
    @SerializedName("etime")
    private String etime;
    @SerializedName("edate")
    public Date edate;
      public Alarmmodel(String id) {
        this.id = id;
    }

    protected Alarmmodel(Parcel in) {
        id = in.readString();
        lat = in.readString();
        lng = in.readString();
        text = in.readString();
        address = in.readString();
        isDisable = in.readByte() != 0;


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(text);
        dest.writeString(address);

        dest.writeByte((byte) (isDisable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alarmmodel> CREATOR = new Creator<Alarmmodel>() {
        @Override
        public Alarmmodel createFromParcel(Parcel in) {
            return new Alarmmodel(in);
        }

        @Override
        public Alarmmodel[] newArray(int size) {
            return new Alarmmodel[size];
        }
    };

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setetime(String t){this.etime = t ;}
    public String getEtime() throws ParseException {return (etime) ;}
    public void setstime(String t){this.stime = t ;}
    public String getStime() throws ParseException {return (stime) ;}
    public void setedate(Date t){this.edate = t ;}
    public Date getEdate() throws ParseException {
        return (edate) ;
        }
    public void setsdate(Date t){this.sDate = t ;}
    public Date getSdate() throws ParseException {return (sDate) ;}


    @Override
    public boolean equals(Object obj) {
        Alarmmodel alarmmodel= (Alarmmodel) obj;
        if(this.id.equals(alarmmodel.getId()))
            return true;
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
