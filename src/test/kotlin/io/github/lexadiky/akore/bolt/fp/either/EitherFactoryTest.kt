package io.github.lexadiky.akore.bolt.fp.either

import io.github.lexadiky.akore.bolt.fp.either.Either
import io.github.lexadiky.akore.bolt.fp.either.asEither
import io.github.lexadiky.akore.bolt.fp.either.catch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class EitherFactoryTest {

    @Nested
    inner class `Result asEither` {

        @Test
        fun `construct from successful result`() {
            assertEquals(
                Either.Right("bolt"),
                Result.success("bolt").asEither()
            )
        }

        @Test
        fun `construct from failed result`() {
            val exception = Exception("bolt")

            assertEquals(
                Either.Left(exception),
                Result.failure<String>(exception).asEither()
            )
        }
    }

    @Nested
    inner class catch {

        @Test
        fun `construct from successful runnable`() {
            assertEquals(
                Either.Right("bolt"),
                Either.catch { "bolt" }
            )
        }

        @Test
        fun `construct from throwing runnable`() {
            val exception = Exception("bolt")

            assertEquals(
                Either.Right(exception),
                Either.catch { throw exception }
            )
        }
    }

    @Nested
    inner class scoped {

        @Test
        fun `construct from lambda with no bindings`() = runBlocking {
            assertEquals(
                Either.Right("bolt"),
                Either<Throwable, String> { "bolt" }
            )
        }

        @Test
        fun `construct from lambda with right binding`() = runBlocking {
            val dependency = Either.Right("bolt")

            assertEquals(
                Either.Right("bolt"),
                Either<Throwable, String> { dependency.bind() }
            )
        }

        @Test
        @Timeout(5, unit = TimeUnit.SECONDS)
        fun `construct from lambda with left binding`() = runBlocking {
            val exception = Exception("bolt")
            val dependency = Either.Left(exception)

            assertEquals(
                Either.Left(exception).value,
                Either<Throwable, String> { dependency.bind() }.value
            )
        }
    }
}