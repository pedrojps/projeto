package com.example.myapplication.utils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ricardo on 30/03/17.
 */

@Table(name = "Cache")
public class Cache extends Model {
    @Column(name = "key")
    public String key;

    @Column(name = "value")
    public String value;
}