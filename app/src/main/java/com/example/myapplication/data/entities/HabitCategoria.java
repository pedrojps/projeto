package com.example.myapplication.data.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.DateTime;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.data.source.local.database.typeConverter.BitmapTypeConverter;

import java.util.ArrayList;

@Entity(tableName = "CATEGORIA_H")
public class HabitCategoria implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(index = true)
    private LocalDate dataCriat;

    private String discricao;

    private String nome;

    private DateTime exportado;

    @ColumnInfo(name = "day_of_week")
    private String dayOfWeek;

    //@Nullable
    //private String icone;

    public HabitCategoria() {
    }
    @Ignore
    public HabitCategoria(@NonNull String nome, @NonNull LocalDate dataCriat,String discricao,String dayOfWeek) {
        this.dataCriat = dataCriat;
        this.nome = nome;
        this.discricao = discricao;
      //  this.icone = icone;
        this.dayOfWeek = dayOfWeek;
    }

    @Ignore
    public HabitCategoria(long id,@NonNull String nome,  @NonNull LocalDate dataCriat, String discricao,String dayOfWeek, DateTime exportado) {
        this.id = id;
        this.dataCriat = dataCriat;
        this.discricao = discricao;
        this.exportado = exportado;
        //this.icone = icone;
        this.nome = nome;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HabitCategoria> CREATOR = new Creator<HabitCategoria>() {
        @Override
        public HabitCategoria createFromParcel(Parcel in) {
            return new HabitCategoria(in);
        }

        @Override
        public HabitCategoria[] newArray(int size) {
            return new HabitCategoria[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NonNull
    public LocalDate getDataCriat() {
        return dataCriat;
    }

    public void setDataCriat(@NonNull LocalDate dataCriat) {
        this.dataCriat = dataCriat;
    }

    public String getDiscricao() {
        return discricao;
    }

    public void setDiscricao(String discricao) {
        this.discricao = discricao;
    }

    public DateTime getExportado() {
        return exportado;
    }

    public void setExportado(DateTime exportado) {
        this.exportado = exportado;
    }

    public boolean isExportado(){
        return exportado != null;
    }


    @Ignore
    protected HabitCategoria(Parcel in) {

        id = in.readLong();
        nome = in.readString();
        dataCriat = new LocalDate(in.readLong());

        discricao = in.readString();
        dayOfWeek = in.readString();
        long exportadoValue = in.readLong();
        exportado = exportadoValue < 0 ? null : new DateTime(exportadoValue);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeLong(dataCriat.getTime());
        dest.writeString(discricao);
        dest.writeString(dayOfWeek);
        dest.writeLong(exportado == null ? -1 : exportado.getTime());
    }

}
