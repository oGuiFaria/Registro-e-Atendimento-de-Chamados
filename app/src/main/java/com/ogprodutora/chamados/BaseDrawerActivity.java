package com.ogprodutora.chamados;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseDrawerActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;
    protected NavigationView navigationView;

    @Override
    public void setContentView(int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_drawer, null);
        FrameLayout contentFrame = drawerLayout.findViewById(R.id.content_frame_base);
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        super.setContentView(drawerLayout);

        toolbar = drawerLayout.findViewById(R.id.toolbar_base);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_view_base);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_novo_chamado) {
                    if (!(BaseDrawerActivity.this instanceof CadastrarChamadoActivity)) {
                        startActivity(new Intent(BaseDrawerActivity.this, CadastrarChamadoActivity.class));
                    }
                } else if (id == R.id.nav_listar_chamados) {
                    if (!(BaseDrawerActivity.this instanceof ListarChamadosActivity)) {
                        startActivity(new Intent(BaseDrawerActivity.this, ListarChamadosActivity.class));
                    }
                } else if (id == R.id.nav_estatisticas) {
                    if (!(BaseDrawerActivity.this instanceof EstatisticasActivity)) {
                        startActivity(new Intent(BaseDrawerActivity.this, EstatisticasActivity.class));
                    }
                } else if (id == R.id.nav_sobre) {
                    if (!(BaseDrawerActivity.this instanceof SobreActivity)) {
                        startActivity(new Intent(BaseDrawerActivity.this, SobreActivity.class));
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
