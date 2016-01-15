package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.Favoritos.FavoritosGet;
import com.usach.tbdgrupo7.iservifast.Controllers.OfrecerGet2;
import com.usach.tbdgrupo7.iservifast.Model.Categoria;
import com.usach.tbdgrupo7.iservifast.Model.Comunidad;
import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

public class FavoritosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CustomListAdapter adapter;
    private ListView list;
    private ProgressDialog progressDialog;
    private String[] titulos;
    private String[] descripciones;
    private OfertaGet[] servicios;
    private Usuario user;
    private Favorito favoritos[];
    private Comunidad comunidades[];
    private Categoria categorias[];
    private Bitmap imagenes[];


    Integer[] imgid={
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos_1);

        user = (Usuario) (getIntent().getSerializableExtra("usuario"));
        comunidades = (Comunidad[]) (getIntent().getSerializableExtra("comunidades"));
        categorias = (Categoria[]) (getIntent().getSerializableExtra("categorias"));

        progressDialog = new ProgressDialog(FavoritosActivity.this,R.style.AppTheme_Dark_Dialog);

        new FavoritosGet(this).execute(getResources().getString(R.string.servidor) + "Favoritos/users/" + user.getIdUsuario());

        list=(ListView)findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(FavoritosActivity.this, ServicioOfrecidoActivity.class);
                myIntent.putExtra("oferta", servicios[position]);
                myIntent.putExtra("usuario", user);
                startActivity(myIntent);

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    public void error_internet(){
        Toast.makeText(FavoritosActivity.this, getResources().getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
    }

    public void getServicios(Favorito favoritos[]){
        new OfrecerGet2(this).execute(getResources().getString(R.string.servidor) + "Oferta");
        this.favoritos = favoritos;
    }

    public void listarServicios(OfertaGet[] servicios) {
        int i;
        int j = 0;
        int largo_favoritos = favoritos.length;
        OfertaGet[] servs = new OfertaGet[largo_favoritos];
        if(largo_favoritos>0) {
            for (j=0;j<largo_favoritos;j++) {
                for (i = 0; i < servicios.length; i++) {
                    if (servicios[i].getIdServicio() == favoritos[j].getServicio_idServicio()) {
                        servs[j] = servicios[i];
                    }
                }
            }
            this.servicios = servs;
            titulos = crearArrayTitulo(servs);
            descripciones = crearArrayDescripcion(servs);
            adapter = new CustomListAdapter(this, titulos, descripciones, imagenes);
            list.setAdapter(adapter);
        }
    }

    private String[] crearArrayTitulo(OfertaGet[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getTitulo();
        }
        return aux;
    }

    private String[] crearArrayDescripcion(OfertaGet[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getDescripcion();
        }
        return aux;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SystemUtilities su = new SystemUtilities(getApplicationContext());
        if (su.isNetworkAvailable()) {
            if(categorias!=null&&comunidades!=null){
                if (id == R.id.servicios_ofrecidos) {
                    Intent intent = new Intent(FavoritosActivity.this, MainActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.servicios_solicitados) {
                    Intent intent = new Intent(FavoritosActivity.this, ServiciosSolicitadosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.ofrecer_servicio){
                    Intent intent = new Intent(FavoritosActivity.this,OfrecerActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario",user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.solicitar_servicio) {
                    Intent intent = new Intent(FavoritosActivity.this,SolicitarActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario",user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_ofrecidos){
                    Intent intent = new Intent(getApplicationContext(), MisServiciosOfrecidosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_solicitados){
                    Intent intent = new Intent(getApplicationContext(), MisServiciosSolicitados.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_favoritos_ofrecidos){
                    Intent intent = new Intent(getApplicationContext(), FavoritosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in,R.transition.slide_left_out);
                }
            }
            else{
                Toast.makeText(FavoritosActivity.this, "COMUNIDAD O CAT NULL", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(FavoritosActivity.this, "No se puede realizar la actividad solicitada, compruebe su conexión a internet.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
            overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
        }
    }
}
