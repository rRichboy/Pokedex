package com.example.pokemon3

import PokemonAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon3.ViewModel.PokemonViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        adapter = PokemonAdapter { pokemon ->
            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra(PokemonDetailActivity.EXTRA_POKEMON_ID, pokemon.getId())
            }
            startActivity(intent)
        }


        val recyclerView: RecyclerView = findViewById(R.id.pokemonRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 колонки
        recyclerView.adapter = adapter

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchPokemon(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val sortByNumberRadioButton: RadioButton = findViewById(R.id.sortByNumberRadioButton)
        val sortByNameRadioButton: RadioButton = findViewById(R.id.sortByNameRadioButton)

        sortByNumberRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.sortByNumber()
            }
        }

        sortByNameRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.sortByName()
            }
        }

        viewModel.pokemonList.observe(this, Observer { pokemonList ->
            adapter.submitList(pokemonList)
        })
    }
}
