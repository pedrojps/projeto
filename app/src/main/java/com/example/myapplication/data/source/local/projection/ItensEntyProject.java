package com.example.myapplication.data.source.local.projection;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;

import java.util.List;

public class ItensEntyProject {

    @Embedded
    public ItemEnty itemEnty;

    @Relation(entity = ItemCategoria.class, entityColumn = "id", parentColumn = "item_categori")
    public List<ItemCategoria> itemCategorias;

    public ItemCategoria getItemEnty(){
        return (itemCategorias == null || itemCategorias.isEmpty()) ? null : itemCategorias.get(0);
    }

}
