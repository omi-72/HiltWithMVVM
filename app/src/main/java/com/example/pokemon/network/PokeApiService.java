package com.example.pokemon.network;

import com.example.pokemon.model.PokemonResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface PokeApiService {

    @GET("pokemon")
    Observable<PokemonResponse> getPokemons();
}
