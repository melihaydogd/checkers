package com.example.checkers

class Tile(
    private val _verticalPos: Int,
    private val _horizontalPos: Int
) {
    val verticalPos: Int
        get() = _verticalPos
    val horizontalPos: Int
        get() = _horizontalPos
    private var _stone: Stone? = null
    var stone: Stone?
        get() = _stone
        set(value) {
            _stone = value
        }

}