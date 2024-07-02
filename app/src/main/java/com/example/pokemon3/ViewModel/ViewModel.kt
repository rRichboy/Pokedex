package com.example.pokemon3.ViewModel

import androidx.lifecycle.*
import com.example.pokemon3.Repository.PokemonRepository
import com.example.pokemon3.Model.Pokemon
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _allPokemonList = mutableListOf<Pokemon>()

    init {
        getPokemonList()
    }

    private fun getPokemonList() {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(100, 0)
                _allPokemonList.clear()
                _allPokemonList.addAll(response.results)
                _pokemonList.value = _allPokemonList
            } catch (e: Exception) {
            }
        }
    }

    fun searchPokemon(query: String) {
        _pokemonList.value = if (query.isEmpty()) {
            _allPokemonList
        } else {
            val filteredList = try {
                if (query.startsWith("#")) {
                    val number = query.substring(1).toInt()
                    _allPokemonList.filter { it.getId() == number }
                } else {
                    val number = query.toInt()
                    _allPokemonList.filter { it.getId() == number }
                }
            } catch (e: NumberFormatException) {
                _allPokemonList.filter { it.name.contains(query, ignoreCase = true) }
            }
            filteredList
        }
    }

    fun sortByNumberAscending() {
        _pokemonList.value = _allPokemonList.sortedBy { it.getId() }
    }


    fun sortByNameAscending() {
        _pokemonList.value = _allPokemonList.sortedBy { it.name }
    }


}
