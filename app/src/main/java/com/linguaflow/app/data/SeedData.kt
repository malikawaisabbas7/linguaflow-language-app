package com.linguaflow.app.data

import com.linguaflow.app.data.database.entity.AchievementEntity
import com.linguaflow.app.data.database.entity.LessonEntity
import com.linguaflow.app.data.database.entity.VocabularyEntity
import com.linguaflow.app.di.AppContainer

/**
 * Seeds a small demo Spanish course + achievement list on first launch so every
 * screen (Home path, Lessons, Flashcards, Quiz, Vocabulary Bank, Progress) has
 * real data to render instead of empty states out of the box.
 */
object SeedData {

    suspend fun seedIfNeeded(container: AppContainer) {
        val lessons = listOf(
            LessonEntity(languageCode = "es", unitNumber = 1, title = "Greetings", category = "Vocabulary", difficulty = "Beginner", estimatedMinutes = 5, isLocked = false, orderIndex = 0),
            LessonEntity(languageCode = "es", unitNumber = 1, title = "Numbers 1-10", category = "Vocabulary", difficulty = "Beginner", estimatedMinutes = 5, isLocked = false, orderIndex = 1),
            LessonEntity(languageCode = "es", unitNumber = 1, title = "Family Members", category = "Vocabulary", difficulty = "Beginner", estimatedMinutes = 6, isLocked = true, orderIndex = 2),
            LessonEntity(languageCode = "es", unitNumber = 2, title = "Present Tense Verbs", category = "Grammar", difficulty = "Beginner", estimatedMinutes = 8, isLocked = true, orderIndex = 3),
            LessonEntity(languageCode = "es", unitNumber = 2, title = "Common Phrases", category = "Phrases", difficulty = "Beginner", estimatedMinutes = 6, isLocked = true, orderIndex = 4),
            LessonEntity(languageCode = "es", unitNumber = 3, title = "Ordering Food", category = "Phrases", difficulty = "Intermediate", estimatedMinutes = 7, isLocked = true, orderIndex = 5),
            LessonEntity(languageCode = "es", unitNumber = 3, title = "Listening Basics", category = "Listening", difficulty = "Intermediate", estimatedMinutes = 6, isLocked = true, orderIndex = 6),
            LessonEntity(languageCode = "es", unitNumber = 4, title = "Mexican Culture Notes", category = "Culture", difficulty = "Beginner", estimatedMinutes = 5, isLocked = true, orderIndex = 7)
        )
        container.lessonRepository.seedLessons(lessons)

        val vocabulary = listOf(
            VocabularyEntity(lessonId = 1, languageCode = "es", word = "Hola", translation = "Hello", pronunciation = "/ˈo.la/", exampleSentence = "¡Hola! ¿Cómo estás?", exampleSentenceTranslation = "Hello! How are you?", category = "Vocabulary"),
            VocabularyEntity(lessonId = 1, languageCode = "es", word = "Buenos días", translation = "Good morning", pronunciation = "/ˈbwe.nos ˈdi.as/", exampleSentence = "Buenos días, profesor.", exampleSentenceTranslation = "Good morning, teacher.", category = "Vocabulary"),
            VocabularyEntity(lessonId = 1, languageCode = "es", word = "Adiós", translation = "Goodbye", pronunciation = "/a.ˈðjos/", exampleSentence = "Adiós, nos vemos mañana.", exampleSentenceTranslation = "Goodbye, see you tomorrow.", category = "Vocabulary"),
            VocabularyEntity(lessonId = 2, languageCode = "es", word = "Uno", translation = "One", pronunciation = "/ˈu.no/", exampleSentence = "Tengo uno.", exampleSentenceTranslation = "I have one.", category = "Vocabulary"),
            VocabularyEntity(lessonId = 2, languageCode = "es", word = "Dos", translation = "Two", pronunciation = "/dos/", exampleSentence = "Tengo dos hermanos.", exampleSentenceTranslation = "I have two brothers.", category = "Vocabulary"),
            VocabularyEntity(lessonId = 2, languageCode = "es", word = "Tres", translation = "Three", pronunciation = "/tɾes/", exampleSentence = "Son las tres.", exampleSentenceTranslation = "It's three o'clock.", category = "Vocabulary"),
            VocabularyEntity(lessonId = null, languageCode = "es", word = "Gracias", translation = "Thank you", pronunciation = "/ˈɡɾa.sjas/", exampleSentence = "Gracias por tu ayuda.", exampleSentenceTranslation = "Thank you for your help.", category = "Phrases"),
            VocabularyEntity(lessonId = null, languageCode = "es", word = "Por favor", translation = "Please", pronunciation = "/poɾ fa.ˈβoɾ/", exampleSentence = "Ayúdame, por favor.", exampleSentenceTranslation = "Help me, please.", category = "Phrases"),
            VocabularyEntity(lessonId = null, languageCode = "es", word = "Familia", translation = "Family", pronunciation = "/fa.ˈmi.lja/", exampleSentence = "Mi familia es grande.", exampleSentenceTranslation = "My family is big.", category = "Vocabulary"),
            VocabularyEntity(lessonId = null, languageCode = "es", word = "Agua", translation = "Water", pronunciation = "/ˈa.ɣwa/", exampleSentence = "Quiero un vaso de agua.", exampleSentenceTranslation = "I want a glass of water.", category = "Vocabulary")
        )
        container.vocabularyRepository.addCustomWordsBulk(vocabulary)

        val achievements = listOf(
            AchievementEntity(id = "first_lesson", title = "First Lesson", description = "Complete your first lesson", emoji = "📖"),
            AchievementEntity(id = "streak_7", title = "7 Day Streak", description = "Study 7 days in a row", emoji = "🔥"),
            AchievementEntity(id = "words_100", title = "100 Words", description = "Learn 100 words", emoji = "💯"),
            AchievementEntity(id = "perfect_quiz", title = "Perfect Score", description = "Get 100% on a quiz", emoji = "🎯"),
            AchievementEntity(id = "streak_30", title = "30 Day Streak", description = "Study 30 days in a row", emoji = "🏆")
        )
        container.progressRepository.seedAchievements(achievements)
    }
}
