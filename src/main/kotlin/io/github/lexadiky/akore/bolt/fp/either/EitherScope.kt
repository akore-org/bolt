package io.github.lexadiky.akore.bolt.fp.either

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume

@JvmInline
value class EitherScope<L, R>(private val cont: CancellableContinuation<Either<L, R>>) {

    suspend fun <T> Either<L, T>.bind(): T {
        return when (this) {
            is Either.Left -> {
                cont.resume(this)
                cont.cancel(null)
                awaitCancellation()
            }
            is Either.Right -> return value
        }
    }
}
