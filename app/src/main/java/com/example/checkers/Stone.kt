package com.example.checkers

class Stone(private val _color: String) {

    companion object {
        const val WHITE = "White"
        const val BLACK = "Black"
    }

    val color: String
        get() = _color
    private var _isKing = false
    var isKing: Boolean
        get() = _isKing
        set(value) {
            _isKing = value
        }
    private var _position: Tile? = null
    var position: Tile?
        get() = _position
        set(value) {
            _position = value
        }


}