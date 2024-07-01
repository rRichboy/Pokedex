package com.example.pokemon3

import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import com.example.pokemon3.Model.PokemonDetail
import com.example.pokemon3.Network.PokeApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonNumberTextView: TextView
    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonTypeTextView: TextView
    private lateinit var pokemonWeightTextView: TextView
    private lateinit var pokemonHeightTextView: TextView
    private lateinit var pokemonMovesTextView: TextView
    private lateinit var pokemonDescriptionTextView: TextView
    private lateinit var hpTextView: TextView
    private lateinit var attackTextView: TextView
    private lateinit var defenseTextView: TextView
    private lateinit var specialAttackTextView: TextView
    private lateinit var specialDefenseTextView: TextView
    private lateinit var speedTextView: TextView
    private lateinit var hpProgressBar: ProgressBar
    private lateinit var attackProgressBar: ProgressBar
    private lateinit var defenseProgressBar: ProgressBar
    private lateinit var specialAttackProgressBar: ProgressBar
    private lateinit var specialDefenseProgressBar: ProgressBar
    private lateinit var speedProgressBar: ProgressBar
    private lateinit var nextButton: ImageView
    private lateinit var backButton: ImageView
    private var currentPokemonId: Int = 1
    private val maxPokemonId = 25

    private val typeColors = mapOf(
        "bug" to "#A7B723",
        "dark" to "#75574C",
        "dragon" to "#7037FF",
        "electric" to "#F9CF30",
        "fairy" to "#E69EAC",
        "fighting" to "#C12239",
        "fire" to "#F57D31",
        "flying" to "#A891EC",
        "ghost" to "#70559B",
        "normal" to "#AAA67F",
        "grass" to "#74CB48",
        "ground" to "#DEC16B",
        "ice" to "#9AD6DF",
        "poison" to "#A43E9E",
        "psychic" to "#FB5584",
        "rock" to "#B69E31",
        "steel" to "#B7B7CE",
        "water" to "#6493EB"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        pokemonNameTextView = findViewById(R.id.pokemonNameTextView)
        pokemonNumberTextView = findViewById(R.id.pokemonNumberTextView)
        pokemonImageView = findViewById(R.id.pokemonImageView)
        pokemonTypeTextView = findViewById(R.id.pokemonTypeTextView)
        pokemonWeightTextView = findViewById(R.id.pokemonWeightTextView)
        pokemonHeightTextView = findViewById(R.id.pokemonHeightTextView)
        pokemonMovesTextView = findViewById(R.id.pokemonMovesTextView)
        pokemonDescriptionTextView = findViewById(R.id.pokemonDescriptionTextView)
        hpTextView = findViewById(R.id.hpTextView)
        attackTextView = findViewById(R.id.attackTextView)
        defenseTextView = findViewById(R.id.defenseTextView)
        specialAttackTextView = findViewById(R.id.specialAttackTextView)
        specialDefenseTextView = findViewById(R.id.specialDefenseTextView)
        speedTextView = findViewById(R.id.speedTextView)
        hpProgressBar = findViewById(R.id.hpProgressBar)
        attackProgressBar = findViewById(R.id.attackProgressBar)
        defenseProgressBar = findViewById(R.id.defenseProgressBar)
        specialAttackProgressBar = findViewById(R.id.specialAttackProgressBar)
        specialDefenseProgressBar = findViewById(R.id.specialDefenseProgressBar)
        speedProgressBar = findViewById(R.id.speedProgressBar)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)

        val pokemonId = intent.getIntExtra(EXTRA_POKEMON_ID, 0)
        if (pokemonId != 0) {
            currentPokemonId = pokemonId
        }


        updateNavigationButtons()

        backButton.setOnClickListener {
            if (currentPokemonId > 1) {
                currentPokemonId--
                loadPokemonDetail(currentPokemonId)
                updateNavigationButtons()
            }
        }

        nextButton.setOnClickListener {
            if (currentPokemonId < maxPokemonId) {
                currentPokemonId++
                loadPokemonDetail(currentPokemonId)
                updateNavigationButtons()
            }
        }

        loadPokemonDetail(currentPokemonId)
    }

    private fun loadPokemonDetail(pokemonId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = PokeApi.service.getPokemonDetail(pokemonId)
                withContext(Dispatchers.Main) {
                    displayPokemonDetail(response)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun displayPokemonDetail(pokemonDetail: PokemonDetail) {
        pokemonNameTextView.text = pokemonDetail.name.replaceFirstChar { it.uppercase() }
        pokemonNumberTextView.text = "#${pokemonDetail.id}"
        Picasso.get()
            .load(pokemonDetail.sprites.front_default)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(pokemonImageView)

        pokemonTypeTextView.text =
            pokemonDetail.types.joinToString(", ") { it.type.name.replaceFirstChar { char -> char.uppercase() } }

        val typeColor = typeColors[pokemonDetail.types.first().type.name] ?: "#FFFFFF"
        findViewById<View>(R.id.pokemonDetailLayout).setBackgroundColor(Color.parseColor(typeColor))

        setProgressBarColor(hpProgressBar, typeColor)
        setProgressBarColor(attackProgressBar, typeColor)
        setProgressBarColor(defenseProgressBar, typeColor)
        setProgressBarColor(specialAttackProgressBar, typeColor)
        setProgressBarColor(specialDefenseProgressBar, typeColor)
        setProgressBarColor(speedProgressBar, typeColor)

        pokemonWeightTextView.text =
            getString(R.string.weight_format, pokemonDetail.weight / 10.0) // weight in kg
        pokemonHeightTextView.text =
            getString(R.string.height_format, pokemonDetail.height / 10.0) // height in m
        pokemonMovesTextView.text = pokemonDetail.abilities.joinToString(", ") { it.ability.name }

        pokemonDescriptionTextView.text =
            "There is a plant seed on its back right from the day this Pokémon is born. The seed slowly grows larger."

        val statsMap = pokemonDetail.stats.associateBy { it.stat.name }

        val hp = statsMap["hp"]?.base_stat ?: 0
        val attack = statsMap["attack"]?.base_stat ?: 0
        val defense = statsMap["defense"]?.base_stat ?: 0
        val specialAttack = statsMap["special-attack"]?.base_stat ?: 0
        val specialDefense = statsMap["special-defense"]?.base_stat ?: 0
        val speed = statsMap["speed"]?.base_stat ?: 0

        hpTextView.text = getString(R.string.hp_format, hp)
        attackTextView.text = getString(R.string.attack_format, attack)
        defenseTextView.text = getString(R.string.defense_format, defense)
        specialAttackTextView.text = getString(R.string.special_attack_format, specialAttack)
        specialDefenseTextView.text = getString(R.string.special_defense_format, specialDefense)
        speedTextView.text = getString(R.string.speed_format, speed)

        hpProgressBar.progress = hp
        attackProgressBar.progress = attack
        defenseProgressBar.progress = defense
        specialAttackProgressBar.progress = specialAttack
        specialDefenseProgressBar.progress = specialDefense
        speedProgressBar.progress = speed
    }

    private fun setProgressBarColor(progressBar: ProgressBar, colorString: String) {
        val color = Color.parseColor(colorString)
        val progressDrawable = progressBar.progressDrawable as LayerDrawable
        val progressLayerDrawable =
            progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        DrawableCompat.setTint(progressLayerDrawable, color)
        progressBar.progressDrawable = progressDrawable
    }

    companion object {
        const val EXTRA_POKEMON_ID = "extra_pokemon_id"
    }

    private fun updateNavigationButtons() {
        backButton.visibility = if (currentPokemonId > 1) View.VISIBLE else View.GONE
        nextButton.visibility = if (currentPokemonId < maxPokemonId) View.VISIBLE else View.GONE
    }
}
