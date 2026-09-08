package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.UserProgress
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [VocabularyWord::class, UserProgress::class],
    version = 1,
    exportSchema = false
)
abstract class OtomiDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: OtomiDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): OtomiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OtomiDatabase::class.java,
                    "otomi_learning_database.db"
                ).addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database)
                    }
                }
            }
        }

        suspend fun populateInitialDatabase(database: OtomiDatabase) {
            val vocabDao = database.vocabularyDao()
            if (vocabDao.getCount() == 0) {
                vocabDao.insertAll(PrepopulatedData.getInitialVocabulary())
            }
            val progressDao = database.userProgressDao()
            if (progressDao.getUserProgressOnce() == null) {
                progressDao.insertOrUpdate(
                    UserProgress(
                        id = 1,
                        xp = 50,
                        streakDays = 1,
                        lastActiveTimestamp = System.currentTimeMillis(),
                        completedLessonsIds = "",
                        totalWordsLearned = 0,
                        miniGameHighScore = 0,
                        pronunciationPracticesCount = 0
                    )
                )
            }
        }
    }
}
