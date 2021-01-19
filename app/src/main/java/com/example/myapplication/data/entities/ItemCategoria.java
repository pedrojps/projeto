package com.example.myapplication.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.DateTime;

@Entity(tableName = "CATEGORIA_I",
        foreignKeys = {
                @ForeignKey(entity = HabitCategoria.class, parentColumns = {"id"},
                        childColumns = {"categori_h"}),
        })
public class ItemCategoria implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "categori_h")
    private long categoriID;

    private DateTime exportado;

    private String nome;

    private int tipo;


    protected ItemCategoria() {}
    @Ignore
    public ItemCategoria(long categoriID, DateTime exportado,
                         String nome
    ) {
        this.categoriID = categoriID;
        this.exportado = exportado;
        this.nome = nome;
    }
    @Ignore
    public ItemCategoria(long categoriID,  DateTime exportado,
                         String nome, int tipo
    ) {
        this.categoriID = categoriID;
        this.exportado = exportado;
        this.nome = nome;
        this.tipo = tipo;
    }

    public static final Creator<ItemCategoria> CREATOR = new Creator<ItemCategoria>() {
        @Override
        public ItemCategoria createFromParcel(Parcel in) {
            return new ItemCategoria(in);
        }

        @Override
        public ItemCategoria[] newArray(int size) {
            return new ItemCategoria[size];
        }
    };

    public long getCategoriID() {
        return categoriID;
    }

    public long getId() {
        return id;
    }

    public void setCategoriID(long categoriID) {
        this.categoriID = categoriID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getExportado() {
        return this.exportado;
    }

    public void setExportado(DateTime date) {
        this.exportado = date;
    }

    public String getNome(){return this.nome;}

    public void setNome(String nome){this.nome = nome;}

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    @Ignore
    protected ItemCategoria(Parcel in) {
        categoriID = in.readLong();

        long exportadoValue = in.readLong();
        exportado = exportadoValue < 0 ? null : new DateTime(exportadoValue);
        nome = in.readString();
        tipo= in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(categoriID);
        dest.writeLong(exportado == null ? -1 : exportado.getTime());
        dest.writeString(nome);
        dest.writeInt(tipo);
    }

}
