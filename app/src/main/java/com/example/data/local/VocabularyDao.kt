package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_words ORDER BY otomi ASC")
    fun getAllWords(): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE category = :category ORDER BY otomi ASC")
    fun getWordsByCategory(category: String): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE isBookmarked = 1 ORDER BY otomi ASC")
    fun getBookmarkedWords(): Flow<List<VocabularyWord>>

    @Query("""
        SELECT * FROM vocabulary_words 
        WHERE otomi LIKE '%' || :query || '%' 
           OR spanish LIKE '%' || :query || '%' 
           OR phoneticSpanish LIKE '%' || :query || '%'
        ORDER BY otomi ASC
    """)
    fun searchWords(query: String): Flow<List<VocabularyWord>>

    @Query("SELECT COUNT(*) FROM vocabulary_words")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<VocabularyWord>)

    @Update
    suspend fun updateWord(word: VocabularyWord)

    @Query("UPDATE vocabulary_words SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmark(id: Int, isBookmarked: Boolean)
}
