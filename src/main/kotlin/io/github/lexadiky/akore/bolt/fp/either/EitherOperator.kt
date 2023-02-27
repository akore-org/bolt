package io.github.lexadiky.akore.bolt.fp.either

fun <L, R> Either<L, R>.flip(): Either<R, L> = when (this) {
    is Either.Left -> Either.Right(value)
    is Either.Right -> Either.Left(value)
}

fun <L, R> Either<L, R>.recover(recovery: (L) -> R): R = when (this) {
    is Either.Left -> recovery(value)
    is Either.Right -> value
}

fun <L, R> Either<L, R>.inspect(onLeft: (L) -> Unit = {}, onRight: (R) -> Unit = {}): Either<L, R> {
    when (this) {
        is Either.Left -> onLeft(value)
        is Either.Right -> onRight(value)
    }

    return this
}

fun <L, R> Either<L, R>.onLeft(onLeft: (L) -> Unit = {}): Either<L, R> {
    return inspect(onLeft = onLeft)
}

fun <L, R> Either<L, R>.onRight(onRight: (R) -> Unit = {}): Either<L, R> {
    return inspect(onRight = onRight)
}

fun <L, R, R2> Either<L, R>.map(transformer: (R) -> R2) : Either<L, R2> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(transformer(value))
}

fun <L, R, R2> Either<L, R>.flatMap(transformer: (R) -> Either<L, R2>) : Either<L, R2> = when (this) {
    is Either.Left -> this
    is Either.Right -> transformer(value)
}

