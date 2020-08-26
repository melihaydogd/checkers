package com.example.checkers

import java.lang.IllegalArgumentException

class Board {
    private lateinit var board: Array<Array<Tile>>
    private var _whiteStone: ArrayList<Stone> = ArrayList()
    val whiteStone: ArrayList<Stone>
        get() = _whiteStone
    private var _blackStone: ArrayList<Stone> = ArrayList()
    val blackStone: ArrayList<Stone>
        get() = _blackStone

    init {
        board = Array(8) { vertical ->
            Array(8) {horizontal ->
                Tile(vertical,horizontal)
            }
        }
        for(horizontal in 0..6 step 2) {
            val white = Stone(Stone.WHITE)
            val white1 = Stone(Stone.WHITE)
            val white2 = Stone(Stone.WHITE)

            val black = Stone(Stone.BLACK)
            val black1 = Stone(Stone.BLACK)
            val black2 = Stone(Stone.BLACK)

            board[0][horizontal].stone = white
            white.position = board[0][horizontal]
            board[1][horizontal+1].stone = white1
            white1.position = board[1][horizontal+1]
            board[2][horizontal].stone = white2
            white2.position = board[2][horizontal]

            board[5][horizontal+1].stone = black
            black.position = board[5][horizontal+1]
            board[6][horizontal].stone = black1
            black1.position = board[6][horizontal]
            board[7][horizontal+1].stone = black2
            black2.position = board[7][horizontal+1]

            _whiteStone.add(white)
            _whiteStone.add(white1)
            _whiteStone.add(white2)

            _blackStone.add(black)
            _blackStone.add(black1)
            _blackStone.add(black2)
        }
    }

    fun getTile(vertical: Int, horizontal: Int): Tile? {
        if(vertical in 0..7 && horizontal in 0..7) {
            return board[vertical][horizontal]
        } else {
            return null
        }
    }

//    fun hasMoves(color: String): Boolean {
//        val oppositeColor: String
//        if(color == Stone.BLACK) {
//            oppositeColor = Stone.WHITE
//        } else {
//            oppositeColor = Stone.BLACK
//        }
//        val stoneArray: ArrayList<Stone>
//        if(color == Stone.BLACK) {
//            stoneArray = _blackStone
//        } else {
//            stoneArray = _whiteStone
//        }
//        for(stone in stoneArray) {
//            val tile = stone.position!!
//            val tileVerticalPos = tile.verticalPos
//            val tileHorizontalPos = tile.horizontalPos
//            val leftUp = getTile(tileVerticalPos - 1, tileHorizontalPos - 1)
//            val rightUp = getTile(tileVerticalPos - 1, tileHorizontalPos + 1)
//            val leftDown = getTile(tileVerticalPos + 1, tileHorizontalPos - 1)
//            val rightDown = getTile(tileVerticalPos + 1, tileHorizontalPos + 1)
//            if (leftUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
//                if (leftUp.stone == null) {
//                    return true
//                } else if (leftUp.stone?.color == oppositeColor) {
//                    val temp = getTile(tileVerticalPos - 2, tileHorizontalPos - 2)
//                    if (temp != null && temp.stone == null) {
//                        return true
//                    }
//                }
//            }
//            if (rightUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
//                if (rightUp.stone == null) {
//                    return true
//                } else if (rightUp.stone?.color == oppositeColor) {
//                    val temp = getTile(tileVerticalPos - 2, tileHorizontalPos + 2)
//                    if (temp != null && temp.stone == null) {
//                        return true
//                    }
//                }
//            }
//            if (leftDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
//                if (leftDown.stone == null) {
//                    return true
//                } else if (leftDown.stone?.color == oppositeColor) {
//                    val temp = getTile(tileVerticalPos + 2, tileHorizontalPos - 2)
//                    if (temp != null && temp.stone == null) {
//                        return true
//                    }
//                }
//            }
//            if (rightDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
//                if (rightDown.stone == null) {
//                    return true
//                } else if (rightDown.stone?.color == oppositeColor) {
//                    val temp = getTile(tileVerticalPos + 2, tileHorizontalPos + 2)
//                    if (temp != null && temp.stone == null) {
//                        return true
//                    }
//                }
//            }
//        }
//        return false
//    }
//}

    fun hasMoves(color: String): Array<ArrayList<Stone>> {
        val oppositeColor: String
        if(color == Stone.BLACK) {
            oppositeColor = Stone.WHITE
        } else {
            oppositeColor = Stone.BLACK
        }
        val stoneArray: ArrayList<Stone>
        if(color == Stone.BLACK) {
            stoneArray = _blackStone
        } else {
            stoneArray = _whiteStone
        }
        val move = Array(2) {
            ArrayList<Stone>()
        }
        for(stone in stoneArray) {
            val tile = stone.position!!
            val tileVerticalPos = tile.verticalPos
            val tileHorizontalPos = tile.horizontalPos
            val leftUp = getTile(tileVerticalPos - 1, tileHorizontalPos - 1)
            val rightUp = getTile(tileVerticalPos - 1, tileHorizontalPos + 1)
            val leftDown = getTile(tileVerticalPos + 1, tileHorizontalPos - 1)
            val rightDown = getTile(tileVerticalPos + 1, tileHorizontalPos + 1)
            if (leftUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
                if (leftUp.stone == null) {
                    if(!move[0].contains(stone)) {
                        move[0].add(stone)
                    }
                } else if (leftUp.stone?.color == oppositeColor) {
                    val temp = getTile(tileVerticalPos - 2, tileHorizontalPos - 2)
                    if (temp != null && temp.stone == null) {
                        if(!move[1].contains(stone)) {
                            move[1].add(stone)
                        }
                    }
                }
            }
            if (rightUp != null && (color == Stone.BLACK || (color == Stone.WHITE && tile.stone?.isKing!!))) {
                if (rightUp.stone == null) {
                    if(!move[0].contains(stone)) {
                        move[0].add(stone)
                    }
                } else if (rightUp.stone?.color == oppositeColor) {
                    val temp = getTile(tileVerticalPos - 2, tileHorizontalPos + 2)
                    if (temp != null && temp.stone == null) {
                        if(!move[1].contains(stone)) {
                            move[1].add(stone)
                        }
                    }
                }
            }
            if (leftDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
                if (leftDown.stone == null) {
                    if(!move[0].contains(stone)) {
                        move[0].add(stone)
                    }
                } else if (leftDown.stone?.color == oppositeColor) {
                    val temp = getTile(tileVerticalPos + 2, tileHorizontalPos - 2)
                    if (temp != null && temp.stone == null) {
                        if(!move[1].contains(stone)) {
                            move[1].add(stone)
                        }
                    }
                }
            }
            if (rightDown != null && (color == Stone.WHITE || (color == Stone.BLACK && tile.stone?.isKing!!))) {
                if (rightDown.stone == null) {
                    if(!move[0].contains(stone)) {
                        move[0].add(stone)
                    }
                } else if (rightDown.stone?.color == oppositeColor) {
                    val temp = getTile(tileVerticalPos + 2, tileHorizontalPos + 2)
                    if (temp != null && temp.stone == null) {
                        if(!move[1].contains(stone)) {
                            move[1].add(stone)
                        }
                    }
                }
            }
        }
        return move
    }
}