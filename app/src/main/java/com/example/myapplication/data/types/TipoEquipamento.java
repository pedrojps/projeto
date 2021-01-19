package com.example.myapplication.data.types;



import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({TipoEquipamento.TRANSPORTADOR, TipoEquipamento.EQUIPAMENTO, TipoEquipamento.CARGA})
public @interface TipoEquipamento {

    int TRANSPORTADOR = 0;

    int EQUIPAMENTO = 1;

    int CARGA = 2;
}
