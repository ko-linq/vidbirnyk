### What is it

Helper library to navigate across hierarchical data structures using syntax like CSS selectors.

Example for map:

```kotlin
import io.github.vidbirnyk.MapAdapter.text
import io.github.vidbirnyk.find
import io.github.vidbirnyk.findAll

val map = mapOf(
    "html" to mapOf(
        "body" to mapOf(
            "h1" to "Header",
            "p" to "Some text"
        ),
        "footer" to mapOf(
            "p" to "Footer text"
        )
    )
)

fun main() {
    println(map.find("html > body > h1")!!.text)
    println(map.findAll("p"))
}

```

### Naming

Vidbirnyk(відбірник) - is "selector" from :ukraine:

### Usage for your nodes

Implement `Vidbirnyk` subclass. Look at example for map for reference.