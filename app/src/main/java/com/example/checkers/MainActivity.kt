package com.example.checkers

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var allButtonArray: Array<Int>

    private lateinit var board: Board
    private lateinit var buttons: Array<Array<ImageButton>>
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var currentPlayer: Player
    private var currentTile: Tile? = null
    private var canContinue = false
    private lateinit var move: Array<ArrayList<Stone>>
    private var forceJump = true

    private val listener: OnClickListener = OnClickListener {

        val tag: Int = it.tag as Int
        val vertical = tag / 10
        val horizontal = tag % 10

        val tile = board.getTile(vertical,horizontal)!!

        if(forceJump) {
            if (move[1].isNotEmpty()) {
                if (move[1].contains(tile.stone)) {
                    play(vertical, horizontal, forceJump)
                }
            } else {
                if (move[0].contains(tile.stone)) {
                    play(vertical, horizontal, !forceJump)
                }
            }
            if(buttons[vertical][horizontal].drawable.constantState == ContextCompat.getDrawable(this,R.drawable.point)!!.constantState) {
                move(vertical,horizontal)
            }
        } else {
            if(move[0].contains(tile.stone) || move[1].contains(tile.stone)) {
                play(vertical, horizontal, forceJump)
            }
            if(buttons[vertical][horizontal].drawable.constantState == ContextCompat.getDrawable(this,R.drawable.point)!!.constantState) {
                move(vertical,horizontal)
            }
            else if(buttons[vertical][horizontal].background.constantState == ContextCompat.getDrawable(this,R.drawable.selected_grid)!!.constantState && canContinue) {
                doNothingChangeTurn()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allButtonArray = arrayOf(R.id.button1, R.id.button3, R.id.button5, R.id.button7,
            R.id.button10, R.id.button12, R.id.button14, R.id.button16,
            R.id.button17, R.id.button19, R.id.button21, R.id.button23,
            R.id.button26, R.id.button28, R.id.button30, R.id.button32,
            R.id.button33, R.id.button35, R.id.button37, R.id.button39,
            R.id.button42, R.id.button44, R.id.button46, R.id.button48,
            R.id.button49, R.id.button51, R.id.button53, R.id.button55,
            R.id.button58, R.id.button60, R.id.button62, R.id.button64)

        player1 = Player(Stone.BLACK)
        player2 = Player(Stone.WHITE)
        currentPlayer = player1
        board = Board()
        move = Array(2) {
            ArrayList<Stone>()
        }
        move[0].add(board.getTile(5,1)!!.stone!!)
        move[0].add(board.getTile(5,3)!!.stone!!)
        move[0].add(board.getTile(5,5)!!.stone!!)
        move[0].add(board.getTile(5,7)!!.stone!!)
        buttons = Array(8) { vertical ->
            Array(8) { horizontal ->
                ImageButton(this)
            }
        }
        connectButtons()
        forceJumpDialog()
        update()

    }

    private fun forceJumpDialog(){
        val choice: Array<CharSequence> = arrayOf("On", "Off")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Force Jump")
        builder.setItems(choice, DialogInterface.OnClickListener { dialogInterface, i ->
            this.forceJump = i == 0
        })
        builder.show()
    }

    fun restartButton(view: View) {
        val choice: Array<CharSequence> = arrayOf("Restart", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Do you want to restart the game?")
        builder.setItems(choice, DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == 0) {
                restart()
            }
        })
        builder.show()
    }


    private fun connectButtons() {
        var temp = 0
        for(i in 0..7) {
            for(j in 0..6 step 2) {
                val k = i % 2
                buttons[i][j+k] = findViewById(allButtonArray[temp])
                buttons[i][j+k].tag = i*10 + (j+k)
                buttons[i][j+k].setOnClickListener(listener)
                temp += 1
            }
        }
    }

    private fun update() {
        for(i in 0..7) {
            for(j in 0..6 step 2) {
                val k = i % 2
                if(board.getTile(i,j+k)!!.stone == null) {
                    buttons[i][j+k].setImageResource(R.color.transparent)
                    buttons[i][j+k].setBackgroundResource(R.color.transparent)
                } else {
                    val tile = board.getTile(i,j+k)!!
                    if(tile.stone!!.color == Stone.WHITE) {
                        buttons[i][j+k].setImageResource(R.drawable.white)
                    } else {
                        buttons[i][j+k].setImageResource(R.drawable.black)
                    }
                    if(forceJump) {
                        if (move[1].isNotEmpty()) {
                            if (move[1].contains(tile!!.stone)) {
                                buttons[i][j + k].setBackgroundResource(R.drawable.circle)
                            } else {
                                buttons[i][j + k].setBackgroundResource(R.color.transparent)
                            }
                        } else {
                            if (move[0].contains(tile!!.stone)) {
                                buttons[i][j + k].setBackgroundResource(R.drawable.circle)
                            } else {
                                buttons[i][j + k].setBackgroundResource(R.color.transparent)
                            }
                        }
                    } else {
                        if(move[0].contains(tile!!.stone) || move[1].contains(tile!!.stone)) {
                            buttons[i][j + k].setBackgroundResource(R.drawable.circle)
                        } else {
                            buttons[i][j + k].setBackgroundResource(R.color.transparent)
                        }
                    }
                }
            }
        }
    }

    private fun play(vertical: Int, horizontal: Int, forceJump: Boolean) {
        if(currentPlayer == player1) {
            val tile = board.getTile(vertical,horizontal)!!
            if(tile.stone?.color == player1.color && !canContinue) {
                currentTile = tile
                update()
                buttons[vertical][horizontal].setBackgroundResource(R.drawable.selected_grid)
                lookAround(vertical,horizontal, tile, player1.color, forceJump)
            }
        } else {
            val tile = board.getTile(vertical,horizontal)!!
            if(tile.stone?.color == player2.color && !canContinue) {
                currentTile = tile
                update()
                buttons[vertical][horizontal].setBackgroundResource(R.drawable.selected_grid)
                lookAround(vertical,horizontal, tile, player2.color, forceJump)
            }
        }
    }

    private fun doNothingChangeTurn() {
        canContinue = false
        currentTile = null
        changeTurn()
    }

    private fun lookAround(vertical: Int, horizontal: Int, tile: Tile, color: String, forceJump: Boolean) {
        val oppositeColor: String
        if(color == Stone.BLACK) {
            oppositeColor = Stone.WHITE
        } else {
            oppositeColor = Stone.BLACK
        }
        val leftUp = board.getTile(vertical-1, horizontal-1)
        val rightUp = board.getTile(vertical-1, horizontal+1)
        val leftDown = board.getTile(vertical+1, horizontal-1)
        val rightDown = board.getTile(vertical+1, horizontal+1)
        if(leftUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
            if(leftUp.stone == null && !forceJump) {
                buttons[vertical-1][horizontal-1].setImageResource(R.drawable.point)
            } else if(leftUp.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical-2,horizontal-2)
                if(temp != null && temp.stone == null) {
                    buttons[vertical-2][horizontal-2].setImageResource(R.drawable.point)
                }
            }
        }
        if(rightUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
            if(rightUp.stone == null && !forceJump) {
                buttons[vertical-1][horizontal+1].setImageResource(R.drawable.point)
            } else if(rightUp.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical-2,horizontal+2)
                if(temp != null && temp.stone == null) {
                    buttons[vertical-2][horizontal+2].setImageResource(R.drawable.point)
                }
            }
        }
        if(leftDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
            if(leftDown.stone == null && !forceJump) {
                buttons[vertical+1][horizontal-1].setImageResource(R.drawable.point)
            } else if(leftDown.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical+2,horizontal-2)
                if(temp != null && temp.stone == null) {
                    buttons[vertical+2][horizontal-2].setImageResource(R.drawable.point)
                }
            }
        }
        if(rightDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
            if(rightDown.stone == null && !forceJump) {
                buttons[vertical+1][horizontal+1].setImageResource(R.drawable.point)
            } else if(rightDown.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical+2,horizontal+2)
                if(temp != null && temp.stone == null) {
                    buttons[vertical+2][horizontal+2].setImageResource(R.drawable.point)
                }
            }
        }
    }

    private fun move(vertical: Int, horizontal: Int) {
        canContinue = false
        var gameOver = false
        val currentStone = currentTile!!.stone!!
        val verticalControl = currentTile!!.verticalPos.plus(vertical)
        val horizontalControl = currentTile!!.horizontalPos.plus(horizontal)
        val tile = board.getTile(vertical,horizontal)!!
        tile.stone = currentStone
        currentStone.position = tile
        currentTile!!.stone = null
        currentTile = null
        if(verticalControl % 2 == 0 && horizontalControl % 2 == 0) {
            val stone = board.getTile(verticalControl / 2, horizontalControl / 2)!!.stone
            board.getTile(verticalControl / 2, horizontalControl / 2)!!.stone = null
            makeKing(currentStone, vertical)
            update()
            if (currentPlayer == player1) {
                board.whiteStone.remove(stone)
                if (board.whiteStone.isEmpty()) {
                    gameOverDialog(Stone.BLACK)
                    gameOver = true
                } else {
                    lookAroundtoContinue(vertical,horizontal,tile,player1.color)
                }
            } else {
                board.blackStone.remove(stone)
                if (board.blackStone.isEmpty()) {
                    gameOverDialog(Stone.WHITE)
                    gameOver = true
                } else {
                    lookAroundtoContinue(vertical,horizontal,tile,player2.color)
                }
            }
        } else {
            makeKing(currentStone, vertical)
            update()
        }

        if(!gameOver) {
            if (!canContinue) {
                changeTurn()
            } else {
                currentTile = tile
                buttons[vertical][horizontal].setBackgroundResource(R.drawable.selected_grid)
            }
        }
    }

    private fun lookAroundtoContinue(vertical: Int, horizontal: Int, tile: Tile, color: String) {
        val oppositeColor: String
        if(color == Stone.BLACK) {
            oppositeColor = Stone.WHITE
        } else {
            oppositeColor = Stone.BLACK
        }
        val leftUp = board.getTile(vertical - 1, horizontal - 1)
        val rightUp = board.getTile(vertical - 1, horizontal + 1)
        val leftDown = board.getTile(vertical + 1, horizontal - 1)
        val rightDown = board.getTile(vertical + 1, horizontal + 1)
        if (leftUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
            if (leftUp.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical - 2, horizontal - 2)
                if (temp != null && temp.stone == null) {
                    buttons[vertical - 2][horizontal - 2].setImageResource(R.drawable.point)
                    canContinue = true
                }
            }
        }
        if (rightUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
            if (rightUp.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical - 2, horizontal + 2)
                if (temp != null && temp.stone == null) {
                    buttons[vertical - 2][horizontal + 2].setImageResource(R.drawable.point)
                    canContinue = true
                }
            }
        }
        if (leftDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
            if (leftDown.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical + 2, horizontal - 2)
                if (temp != null && temp.stone == null) {
                    buttons[vertical + 2][horizontal - 2].setImageResource(R.drawable.point)
                    canContinue = true
                }
            }
        }
        if (rightDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
            if (rightDown.stone?.color == oppositeColor) {
                val temp = board.getTile(vertical + 2, horizontal + 2)
                if (temp != null && temp.stone == null) {
                    buttons[vertical + 2][horizontal + 2].setImageResource(R.drawable.point)
                    canContinue = true
                }
            }
        }
    }

    private fun gameOverDialog(color: String) {
        val choice: Array<CharSequence> = arrayOf("Play Again", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("$color wins!")
        builder.setItems(choice, DialogInterface.OnClickListener { dialogInterface, i ->
            if(i == 0) {
                restart()
            }
        })
        builder.show()
    }

    private fun restart() {
        currentTile = null
        player1 = Player(Stone.BLACK)
        player2 = Player(Stone.WHITE)
        currentPlayer = player1
        board = Board()
        move = Array(2) {
            ArrayList<Stone>()
        }
        move[0].add(board.getTile(5,1)!!.stone!!)
        move[0].add(board.getTile(5,3)!!.stone!!)
        move[0].add(board.getTile(5,5)!!.stone!!)
        move[0].add(board.getTile(5,7)!!.stone!!)
        forceJumpDialog()
        update()
    }

    private fun changeTurn() {
        if (currentPlayer == player1) {
            currentPlayer = player2
            move = board.hasMoves(player2.color)
            if(move[0].isEmpty() && move[1].isEmpty()) {
                gameOverDialog(player1.color)
            }
        } else {
            currentPlayer = player1
            move = board.hasMoves(player1.color)
            if(move[0].isEmpty() && move[1].isEmpty()) {
                gameOverDialog(player2.color)
            }
        }
        update()
    }

    private fun makeKing(currentStone: Stone, vertical: Int) {
        if (!currentStone.isKing && vertical == 0 && currentStone.color == Stone.BLACK) {
            currentStone.isKing = true
        } else if (!currentStone.isKing && vertical == 7 && currentStone.color == Stone.WHITE) {
            currentStone.isKing = true
        }
    }
}