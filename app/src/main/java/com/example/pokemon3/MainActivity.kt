package com.example.pokemon3

import PokemonAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu
import com.example.pokemon3.ViewModel.PokemonViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView

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

        recyclerView = findViewById(R.id.pokemonRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchPokemon(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val sortByNumberButton: Button = findViewById(R.id.sortByNumberButton)
        val sortByNameButton: Button = findViewById(R.id.sortByNameButton)

        sortByNumberButton.setOnClickListener {
            showSortMenu(it, "number")
        }

        sortByNameButton.setOnClickListener {
            showSortMenu(it, "name")
        }

        viewModel.pokemonList.observe(this, Observer { pokemonList ->
            adapter.submitList(pokemonList) {
                recyclerView.scrollToPosition(0)  // Прокрутка к началу списка после обновления адаптера
            }
        })
    }

    private fun showSortMenu(anchor: View, sortType: String) {
        val popupMenu = PopupMenu(this, anchor)
        popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_ascending -> {
                    if (sortType == "number") {
                        viewModel.sortByNumberAscending()
                    } else {
                        viewModel.sortByNameAscending()
                    }
                }
                R.id.sort_descending -> {
                    if (sortType == "number") {
                        viewModel.sortByNumberDescending()
                    } else {
                        viewModel.sortByNameDescending()
                    }
                }
            }
            true
        }
        popupMenu.show()
    }
}
