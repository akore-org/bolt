package io.github.lexadiky.akore.bolt.fp.either

sealed interface Either<out L, out R> {

    val value: Any?

    data class Left<out L>(override val value: L): Either<L, Nothing> {

        override fun toString(): String = "Left($value)"
    }

    data class Right<out R>(override val value: R): Either<Nothing, R> {

        override fun toString(): String = "Right($value)"
    }

    companion object
}
