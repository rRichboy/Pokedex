package com.example.pokemon3.Model

data class PokemonListResponse(val results: List<Pokemon>)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val abilities: List<Ability>,
    val stats: List<Stat>,
    val sprites: Sprites,
    val types: List<Type>
)
data class Ability(
    val ability: AbilityDetail
)

data class AbilityDetail(
    val name: String
)

data class Stat(
    val base_stat: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String
)

data class Type(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)

data class Sprites(val front_default: String?)

data class Pokemon(val pokemonId: Int, val name: String, val url: String) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${url.split("/").dropLast(1).last()}.png"

    fun getId(): Int {
        return url.split("/").dropLast(1).last().toInt()
    }
}



