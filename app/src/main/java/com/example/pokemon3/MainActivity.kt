package com.example.pokemon3

import PokemonAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.pokemon3.ViewModel.PokemonViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView

    private var sortByNumber = false
    private var sortByName = false

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

        // Example button setup (replace with your actual button setup)
        val sortButton: Button = findViewById(R.id.sortButton)
        sortButton.setOnClickListener {
            showSortDialog()
        }

        viewModel.pokemonList.observe(this, Observer { pokemonList ->
            adapter.submitList(pokemonList) {
                recyclerView.scrollToPosition(0)
            }
        })
    }

    private fun showSortDialog() {
        val dialogView = layoutInflater.inflate(R.layout.sortirovka, null)
        val checkBoxSortByNumber: CheckBox = dialogView.findViewById(R.id.checkBoxSortByNumber)
        val checkBoxSortByName: CheckBox = dialogView.findViewById(R.id.checkBoxSortByName)

        checkBoxSortByNumber.isChecked = sortByNumber
        checkBoxSortByName.isChecked = sortByName

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Apply") { dialog, which ->
                sortByNumber = checkBoxSortByNumber.isChecked
                sortByName = checkBoxSortByName.isChecked

                if (sortByNumber) {
                    viewModel.sortByNumberAscending()
                } else if (sortByName) {
                    viewModel.sortByNameAscending()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}