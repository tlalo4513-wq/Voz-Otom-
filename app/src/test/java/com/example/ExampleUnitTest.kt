package com.example

import com.example.data.local.PrepopulatedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testInitialVocabularyIsNotEmpty() {
    val vocabulary = PrepopulatedData.getInitialVocabulary()
    assertTrue(vocabulary.isNotEmpty())
    assertTrue(vocabulary.any { it.otomi == "Haxäi" })
    assertTrue(vocabulary.any { it.otomi == "Jamädi" })
  }

  @Test
  fun testLessonsDataIntegrity() {
    val lessons = PrepopulatedData.getLessons()
    assertEquals(6, lessons.size)
    lessons.forEach { lesson ->
      assertFalse(lesson.title.isBlank())
      assertTrue(lesson.exercises.isNotEmpty())
      assertTrue(lesson.xpReward > 0)
    }
  }
}
