package com.example.myapplication.data.source.local.projection;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemEnty;

import java.util.List;

public class HabitEntyDetails {

    @Embedded
    public HabitEnty habitEnty;

    @Relation(entity = ItemEnty.class, entityColumn = "habit_enty", parentColumn = "id")
    public List<ItensEntyProject> itensEntyProjects;

    public ItensEntyProject getItemEnty(){
        return (itensEntyProjects == null || itensEntyProjects.isEmpty()) ? null : itensEntyProjects.get(0);
    }
}
