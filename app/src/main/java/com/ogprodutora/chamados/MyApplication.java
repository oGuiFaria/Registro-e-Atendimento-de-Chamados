package com.ogprodutora.chamados;

import android.app.Application;
import com.parse.Parse;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("SEU_APPLICATION_ID_AQUI")
                .clientKey("SEU_CLIENT_KEY_AQUI")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
