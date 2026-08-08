package com.tournament.chess.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View

class ChessBoardView(context: Context) : View(context) {
    private val lightSquarePaint = Paint().apply { color = android.graphics.Color.parseColor("#F0D9B5") }
    private val darkSquarePaint = Paint().apply { color = android.graphics.Color.parseColor("#B58863") }
    private val piecePaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOARD
    }

    private val board = arrayOf(
        arrayOf("r", "n", "b", "q", "k", "b", "n", "r"),
        arrayOf("p", "p", "p", "p", "p", "p", "p", "p"),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("P", "P", "P", "P", "P", "P", "P", "P"),
        arrayOf("R", "N", "B", "Q", "K", "B", "N", "R")
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = minOf(width, height)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val tileSize = width / 8f
        piecePaint.textSize = tileSize * 0.75f

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val x = col * tileSize
                val y = row * tileSize

                val paint = if ((row + col) % 2 == 0) lightSquarePaint else darkSquarePaint
                canvas.drawRect(x, y, x + tileSize, y + tileSize, paint)

                val piece = board[row][col]
                if (piece.isNotEmpty()) {
                    val unicodeChar = getChessUnicode(piece)
                    val xCenter = x + tileSize / 2f
                    val yCenter = y + tileSize / 2f - (piecePaint.descent() + piecePaint.ascent()) / 2f

                    piecePaint.color = if (piece[0].isUpperCase()) {
                        android.graphics.Color.WHITE
                    } else {
                        android.graphics.Color.BLACK
                    }

                    canvas.drawText(unicodeChar, xCenter, yCenter, piecePaint)
                }
            }
        }
    }

    private fun getChessUnicode(piece: String): String {
        return when (piece) {
            "K" -> "♔"
            "Q" -> "♕"
            "R" -> "♖"
            "B" -> "♗"
            "N" -> "♘"
            "P" -> "♙"
            "k" -> "♚"
            "q" -> "♛"
            "r" -> "♜"
            "b" -> "♝"
            "n" -> "♞"
            "p" -> "♟"
            else -> ""
        }
    }
}
