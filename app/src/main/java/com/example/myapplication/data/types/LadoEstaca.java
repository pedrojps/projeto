package com.example.myapplication.data.types;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({LadoEstaca.EIXO, LadoEstaca.DIREITA, LadoEstaca.ESQUERDA})
public @interface LadoEstaca {

    String EIXO = "X";

    String DIREITA = "D";

    String ESQUERDA = "E";
}
