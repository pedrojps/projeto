package com.example.myapplication.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import static androidx.room.ForeignKey.CASCADE;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.common.time.DateTime;

@Entity(tableName = "ENTY_I",
        foreignKeys = {
                @ForeignKey(entity = HabitEnty.class, parentColumns = {"id"},
                        childColumns = {"habit_enty"}, onDelete = CASCADE),
                @ForeignKey(entity = ItemCategoria.class, parentColumns = {"id"},
                childColumns = {"item_categori"}, onDelete = CASCADE)
        })
public class ItemEnty implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "habit_enty")
    private long habitEntyID;

    @ColumnInfo(name = "item_categori")
    private long itemCategoriID;

    private DateTime exportado;

    private String valor;


    public ItemEnty() {}
    @Ignore
    public ItemEnty(long habitEntyID, DateTime exportado,
                    String valor
    ) {
        this.habitEntyID = habitEntyID;
        this.exportado = exportado;
        this.valor = valor;
    }
    @Ignore
    public ItemEnty(long habitEntyID, long itemCategoriID, DateTime exportado,
                    String valor
    ) {
        this.habitEntyID = habitEntyID;
        this.itemCategoriID = itemCategoriID;
        this.exportado = exportado;
        this.valor = valor;
    }

    public static final Creator<ItemEnty> CREATOR = new Creator<ItemEnty>() {
        @Override
        public ItemEnty createFromParcel(Parcel in) {
            return new ItemEnty(in);
        }

        @Override
        public ItemEnty[] newArray(int size) {
            return new ItemEnty[size];
        }
    };

    public long getHabitEntyID() {
        return habitEntyID;
    }

    public long getId() {
        return id;
    }

    public void setHabitEntyID(long habitEntyID) {
        this.habitEntyID = habitEntyID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemCategoriID() {
        return itemCategoriID;
    }

    public void setItemCategoriID(long itemCategoriID) {
        this.itemCategoriID = itemCategoriID;
    }

    public DateTime getExportado() {
        return this.exportado;
    }

    public void setExportado(DateTime date) {
        this.exportado = date;
    }

    public String getValor(){return this.valor;}

    public void setValor(String valor){this.valor = valor;}


    @Ignore
    protected ItemEnty(Parcel in) {
        id = in.readLong();
        habitEntyID = in.readLong();
        this.itemCategoriID = in.readLong();
        long exportadoValue = in.readLong();
        exportado = exportadoValue < 0 ? null : new DateTime(exportadoValue);
        valor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(habitEntyID);
        dest.writeLong(itemCategoriID);
        dest.writeLong(exportado == null ? -1 : exportado.getTime());
        dest.writeString(valor);
    }

}
