package com.example.hope.movieapp_stage2.utilities;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Hope on 6/5/2017.
 */

public interface JSONParser <T> {
    List<T> parseJson (String jsonStr)throws JSONException;
}
