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
                .applicationId("SEU_APPLICATION_ID") // Substitua pelo seu Application ID
                .clientKey("SEU_CLIENT_KEY")         // Substitua pelo seu Client Key
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
