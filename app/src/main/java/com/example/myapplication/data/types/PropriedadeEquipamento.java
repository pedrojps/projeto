package com.example.myapplication.data.types;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({PropriedadeEquipamento.PROPRIO, PropriedadeEquipamento.LOCADO, PropriedadeEquipamento.POR_PRODUCAO})
public @interface PropriedadeEquipamento {

    int PROPRIO = 0;

    int LOCADO = 1;

    int POR_PRODUCAO = 2;
}
