package com.example.pokemon3.Network

import com.example.pokemon3.Model.Pokemon

data class PokemonResponse(
    val results: List<Pokemon>
)