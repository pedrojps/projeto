package com.example.myapplication.data.entities;


import static androidx.room.ForeignKey.CASCADE;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.LocalTime;

@Entity(tableName = "ALERT_CATEGORI", foreignKeys = {
        @ForeignKey(entity = HabitCategoria.class, parentColumns = {"id"},
                childColumns = {"categori_h"}, onDelete = CASCADE)})
public class AlertCategori implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "day_of_week")
    private String dayOfWeek;

    @ColumnInfo(name = "categori_h")
    private long categoriID;

    private boolean isAtive;

    @ColumnInfo(index = true)
    private LocalTime time;

    public AlertCategori(){}

    @Ignore
    public AlertCategori(long categoriID){
        this.categoriID = categoriID;
    }

    @Ignore
    protected AlertCategori(Parcel in) {
        id = in.readLong();
        dayOfWeek = in.readString();
        categoriID = in.readLong();
        long horaValue = in.readLong();
        time = horaValue < 0 ? null : new LocalTime(horaValue);
        isAtive = in.readByte() != 0;
    }

    public static final Creator<AlertCategori> CREATOR = new Creator<AlertCategori>() {
        @Override
        public AlertCategori createFromParcel(Parcel in) {
            return new AlertCategori(in);
        }

        @Override
        public AlertCategori[] newArray(int size) {
            return new AlertCategori[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(dayOfWeek);
        dest.writeLong(categoriID);
        dest.writeLong(time == null ? -1 : time.getTime());
        dest.writeByte((byte) (isAtive ? 1 : 0));
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setIsAtive(boolean isAtive) {
        this.isAtive = isAtive;
    }

    public void setCategoriID(long categoriID) {
        this.categoriID = categoriID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoriID() {
        return categoriID;
    }

    public long getId() {
        return id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getTime() {
        return time;
    }
    public boolean getIsAtive() {
        return isAtive;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
