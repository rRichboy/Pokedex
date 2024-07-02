package com.example.pokemon3.Repository

import com.example.pokemon3.Network.PokeApi

class PokemonRepository {
    private val service = PokeApi.service

    suspend fun getPokemonList(limit: Int, offset: Int) = service.getPokemonList(limit, offset)
    suspend fun getPokemonDetail(id: Int) = service.getPokemonDetail(id)

}