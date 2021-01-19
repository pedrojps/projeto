package com.example.myapplication.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.DateTime;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;

@Entity(tableName = "CATEGORIA_H")
public class HabitCategoria implements Parcelable {
//transforma em itens habito
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(index = true)
    private LocalDate dataCriat;//quando foi criado

    private String discricao;

    private String nome;

    private DateTime exportado;

    public HabitCategoria() {
    }
    @Ignore
    public HabitCategoria(@NonNull String nome, @NonNull LocalDate dataCriat,String discricao) {
        this.dataCriat = dataCriat;
        this.nome=nome;
        this.discricao = discricao;
    }

    @Ignore
    public HabitCategoria(long id,@NonNull String nome,  @NonNull LocalDate dataCriat, String discricao, DateTime exportado) {
        this.id = id;
        this.dataCriat = dataCriat;
        this.discricao = discricao;
        this.exportado = exportado;
        this.nome = nome;
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
        //A ordem de leitura é CRÍTICA para o parcelable, cuidado
        id = in.readLong();
        nome = in.readString();
        dataCriat = new LocalDate(in.readLong());

        long horaValue = in.readLong();

        discricao = in.readString();

        long exportadoValue = in.readLong();
        exportado = exportadoValue < 0 ? null : new DateTime(exportadoValue);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeLong(dataCriat.getTime()); //data is NonNull
        dest.writeString(discricao);
        dest.writeLong(exportado == null ? -1 : exportado.getTime());
    }

}
