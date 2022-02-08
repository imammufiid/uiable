package com.mufid.layout

enum class DragStyle(val id: Int) {
    COLLISION(0), STICKY_X(1), STICKY_Y(2), STICKY_XY(3);
    companion object {
        fun getById(id: Int): DragStyle {
            return when (id) {
                COLLISION.id -> COLLISION
                STICKY_X.id -> STICKY_X
                STICKY_Y.id -> STICKY_Y
                STICKY_XY.id -> STICKY_XY
                else -> COLLISION
            }
        }
    }
}