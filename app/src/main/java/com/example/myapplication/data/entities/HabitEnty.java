package com.example.myapplication.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.DateTime;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;

@Entity(tableName = "ENTY_H", foreignKeys = {
        @ForeignKey(entity = HabitCategoria.class, parentColumns = {"id"},
                childColumns = {"categori_h"})})
public class HabitEnty implements Parcelable {
//transforma em itens habito
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "categori_h", index = true)
    private long categoriaId;

    @NonNull
    @ColumnInfo(index = true)
    private LocalDate data;

    private LocalTime hora;

    private String observacao;

    private DateTime exportado;

    public HabitEnty() {
    }
    @Ignore
    public HabitEnty(long obraId, @NonNull LocalDate data) {
        this.categoriaId = obraId;
        this.data = data;
    }

    @Ignore
    public HabitEnty(long id, long obraId, @NonNull LocalDate data, LocalTime hora, String observacao, DateTime exportado) {
        this.id = id;
        this.categoriaId = obraId;
        this.data = data;
        this.hora = hora;
        this.observacao = observacao;
        this.exportado = exportado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HabitEnty> CREATOR = new Creator<HabitEnty>() {
        @Override
        public HabitEnty createFromParcel(Parcel in) {
            return new HabitEnty(in);
        }

        @Override
        public HabitEnty[] newArray(int size) {
            return new HabitEnty[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(long categoriaId) {
        this.categoriaId = categoriaId;
    }

    @NonNull
    public LocalDate getData() {
        return data;
    }

    public void setData(@NonNull LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
    protected HabitEnty(Parcel in) {
        //A ordem de leitura é CRÍTICA para o parcelable, cuidado
        id = in.readLong();
        categoriaId = in.readLong();
        data = new LocalDate(in.readLong());

        long horaValue = in.readLong();
        hora = horaValue < 0 ? null : new LocalTime(horaValue);

        observacao = in.readString();

        long exportadoValue = in.readLong();
        exportado = exportadoValue < 0 ? null : new DateTime(exportadoValue);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(categoriaId);
        dest.writeLong(data.getTime()); //data is NonNull
        dest.writeLong(hora == null ? -1 : hora.getTime());
        dest.writeString(observacao);
        dest.writeLong(exportado == null ? -1 : exportado.getTime());
    }

}
