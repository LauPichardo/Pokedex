package com.example.pokedex.apiPoke;

import com.example.pokedex.modelos.PokemonRespuesta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiPokeService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerPokemon(@Query("offset") int id, @Query("limit") int limit);

}

