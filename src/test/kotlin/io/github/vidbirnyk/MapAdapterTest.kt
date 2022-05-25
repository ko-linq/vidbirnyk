package io.github.vidbirnyk

import io.github.vidbirnyk.MapAdapter.text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class MapAdapterTest {

    @Test
    fun simpleGet() {
        assertEquals(
            "b",
            mapOf("a" to "b").find("a")?.text
        )
        assertEquals(
            listOf("b"),
            mapOf("a" to "b").findAll("a").text
        )

        assertNull(
            mapOf("a" to "b").find("A")?.text
        )
        assertEquals(
            emptyList<String>(),
            mapOf("a" to "b").findAll("A").text
        )
    }

    @Test
    fun deepGet() {
        assertEquals(
            "1",
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("needle")?.text
        )
    }

    @Test
    fun getInChildren() {
        assertNull(
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("& > needle")?.text
        )
        assertNull(
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("a > needle")?.text
        )
        assertNull(
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("a > a2 > needle")?.text
        )
        assertEquals(
            "1",
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("a2 > needle")?.text
        )
        assertEquals(
            "1",
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("a1 > a2 needle")?.text
        )
        assertEquals(
            "1",
            mapOf(
                "a" to mapOf("a1" to mapOf("a2" to mapOf("needle" to "1")))
            ).find("& > a1 > a2 > needle")?.text
        )
    }

    @Test
    fun classes() {
        assertEquals(
            listOf("b", "d"),
            mapOf(
                "a" to 3,
                "b" to null,
                "c" to mapOf("e" to 5),
                "d" to emptyMap<String, Any>(),
                "e" to "e"
            ).findAll(".empty").map { it.key }
        )
    }
}