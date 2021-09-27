package com.example.pokedex;

import android.os.Bundle;

import com.example.pokedex.apiPoke.ApiPokeService;
import com.example.pokedex.apiPoke.ListaPokemonAdapter;
import com.example.pokedex.modelos.PokemonRespuesta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.view.Menu;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static final String TAG="POKEDEX";
    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapt;
    private FloatingActionButton btnAdd;
    private boolean aptoParaCargar;
    private TimerTask obtemerPokemonTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapt= new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapt);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager= new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " El final.");

                            aptoParaCargar = false;
                            obtenerDatos();
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        aptoParaCargar = true;

        btnAdd=(FloatingActionButton) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDatos();
            }
        });

        obtemerPokemonTask= new TimerTask() {
            @Override
            public void run() {
                obtenerDatos();
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(obtemerPokemonTask, 0, 30000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void obtenerDatos(){
        ApiPokeService service= retrofit.create(ApiPokeService.class);

        Call<PokemonRespuesta> pokemonRespuestaCall= service.obtenerPokemon((int)(Math.random()*800)+1, 1);
        Log.e(TAG, "numero "+((int)Math.random()*800)+1);
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar = true;
                if(response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.message());
                    listaPokemonAdapt.adicionarListaPokemon(response.body().getResults().get(0));

                }else{
                    Log.e(TAG, "onResponse: "+response.errorBody());
                }
                Log.e(TAG, "onResponse: "+call.request().toString());
                Log.e(TAG, "onResponse: "+response.body().toString());
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }

}