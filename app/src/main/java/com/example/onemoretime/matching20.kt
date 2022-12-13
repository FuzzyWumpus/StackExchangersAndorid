package com.example.onemoretime


import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.onemoretime.R.drawable.*


private const val Tag = "matching"
class matching20 : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    private var points = 0
    private var correctpairs = 0
    private lateinit var score: TextView
    private var name = ""
    private var clicked = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching20)

        val possibleImages = mutableListOf(twoofheartssmall, mariosmall, luigismall, twoofdiamondssmall, starsmall, nineofdiamondssmall,
            mushroomsmall, fireflowersmall, fiveofdiamondssmall, cloudsmall)
        possibleImages.shuffle()

        val images = mutableListOf(possibleImages.get(0), possibleImages.get(1),possibleImages.get(2), possibleImages.get(3),
            possibleImages.get(4), possibleImages.get(5),possibleImages.get(6),possibleImages.get(7),possibleImages.get(8),
            possibleImages.get(9)
            )


        // Add each image twice so we can create pairs
        images.addAll(images)
        // Randomize the order of images
        images.shuffle()

        score = findViewById(R.id.textView9)
        score.setText("Points: " + points)


        buttons = listOf(
            findViewById(R.id.imageButton85),
            findViewById(R.id.imageButton86),
            findViewById(R.id.imageButton87),
            findViewById(R.id.imageButton88),
            findViewById(R.id.imageButton89),
            findViewById(R.id.imageButton90),
            findViewById(R.id.imageButton91),
            findViewById(R.id.imageButton92),
            findViewById(R.id.imageButton93),
            findViewById(R.id.imageButton103),
            findViewById(R.id.imageButton94),
            findViewById(R.id.imageButton95),
            findViewById(R.id.imageButton104),
            findViewById(R.id.imageButton96),
            findViewById(R.id.imageButton97),
            findViewById(R.id.imageButton98),
            findViewById(R.id.imageButton99),
            findViewById(R.id.imageButton100),
            findViewById(R.id.imageButton101),
            findViewById(R.id.imageButton102),
        )

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }
        //Try Again Button
        findViewById<Button>(R.id.button19).setOnClickListener {

            if(indexOfSingleSelectedCard == null) {
                restoreCards()
                updateViews()
            }
        }
        //New game button
        findViewById<Button>(R.id.button18).setOnClickListener {
            finish()
            startActivity(getIntent())
            overridePendingTransition(0,0)
        }
        // End game Button
        findViewById<Button>(R.id.button17).setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.endGame8).setOnClickListener {
            cards.forEachIndexed { index, card ->
                val button = buttons[index]
                card.isFaceUp = true;
                updateViews()
                toogleTryAgain()
            }
        }


        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "button clicked!!")
                // Update models
                updateModels(index)
                // Update the UI for the game
                updateViews()
            }
        }
    }
    private fun toogleTryAgain() {
        findViewById<Button>(R.id.button19).setOnClickListener {
            Toast.makeText(this, "Nah you gave up, start a new game", Toast.LENGTH_SHORT).show()

        }


    }
    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.cardbacksmall)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking:
        if (card.isFaceUp) {

            Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show()
            return
        }
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards previously

            restoreCards()


            indexOfSingleSelectedCard = position
        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null


        }
        card.isFaceUp = !card.isFaceUp



    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false

            }
        }
    }




    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            Toast.makeText(this, "Nice Job!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            correctpairs++
            points += 2
            score.text = "Points: " + points

            if (correctpairs == 10) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Name for your score")

// Set up the input

// Set up the input
                val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT
                builder.setView(input)

// Set up the buttons

// Set up the buttons
                builder.setPositiveButton("Set Name",
                    DialogInterface.OnClickListener { dialog, which ->
                        name = input.text.toString()
                    })
                builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()
            }
        }
        else
            if (points > 0)
                points--
        score.text = "Points: " + points

    }


}
