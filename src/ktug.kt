sealed class Slot {
    abstract val duration: Minutes

    fun name(): String {
        fun offset(index: Int) = when (index) {
            0 -> 20
            1 -> 12
            else -> 0
        }

        return when (this) {
            is Speaker -> listOf(name, duration, title)
            is Break -> listOf("  Break", duration)
        }.mapIndexed { idx, it -> it.toString().padEnd(offset(idx)) }
            .joinToString(" ")
    }
}

class Speaker(
    override val duration: Minutes,
    val name: String,
    val title: String
) : Slot()

class Break(
    override val duration: Minutes
) : Slot()

private fun Int.toMinutesString() = when {
    this < 10 -> "0$this"
    else -> "$this"
}

data class Minutes(val m: Int) {
    override fun toString(): String {
        return "$m minutes"
    }
}

val Int.m get() = Minutes(this)

data class Time(val h: Int, val m: Int) {
    override fun toString(): String {
        return "$h:${m.toMinutesString()}"
    }

    operator fun plus(s: Minutes): Time {
        val newM = m + s.m
        return copy(h = h + newM / 60, m = newM % 60)
    }
}

val <T> T.TBD get() = this

fun main() {

    val startTime = Time(15, 0)

    val schedule = listOf(
        Break(30.m),
        Speaker(name = "Florina", duration = 30.m, title = "Extend your vocabulary with Kotlin language features"),
        Speaker(name = "Svetlana Isakova", duration = 45.m, title = "New Type Inference and Related Language Features"),
        Break(25.m),
        Speaker(name = "Shagen", duration = 30.m, title = "What's new in Kotlin/JS interop"),
        Speaker(name = "Jossi", duration = 20.m, title = "Building SDKs - The Kotlin Way"),
        Break(45.m),
        Speaker(name = "Wojtek", duration = 45.m.TBD, title = "Kotlin Multiplatform"),
        Speaker(name = "Marcin", duration = 40.m.TBD, title = "Understanding Kotlin coroutines"),
        Break(30.m)
    )

    var time = startTime
    for (slot in schedule) {
        println("$time  ${slot.name()}")
        time += slot.duration
    }

    println("$time THE END")
}