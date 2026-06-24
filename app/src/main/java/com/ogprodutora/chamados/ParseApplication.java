package com.ogprodutora.chamados;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Substitua pelas suas credenciais do Back4App
        // Acesse https://www.back4app.com > Dashboard > App Settings > Security & Keys
        ParseObject.registerSubclass(ChamadoParse.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gAfIX5xLxvVEz1910PdBNGhPQ1Zs9TeTar3PnHlt") // Substitua pelo seu Application ID
                .clientKey("HynTVrB36jRFnFaGTqN5UWsH2R9cNLNmHa10xUBG")         // Substitua pelo seu Client Key
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
