package io.github.lexadiky.akore.bolt.fp.either

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume

fun <R> Either.Companion.catch(runnable: () -> R) : Either<Throwable, R> {
    return try {
        Either.Right(runnable())
    } catch (e: Throwable) {
        Either.Left(e)
    }
}

fun <R> Result<R>.asEither(): Either<Throwable, R> {
    return fold(
        onSuccess = { Either.Right(it) },
        onFailure = { Either.Left(it) }
    )
}

suspend operator fun <L, R> Either.Companion.invoke(fn: suspend EitherScope<L, R>.() -> R): Either<L, R> {

    val context = coroutineContext

    return suspendCancellableCoroutine { cont: CancellableContinuation<Either<L, R>> ->
        val eitherScope = EitherScope(cont)

        CoroutineScope(context).async {
            cont.resume(Either.Right(eitherScope.fn()))
        }
    }
}
