package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.IdeaEvaluation
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Query("SELECT * FROM saved_ideas ORDER BY createdAt DESC")
    fun getAllIdeas(): Flow<List<IdeaEvaluation>>

    @Query("SELECT * FROM saved_ideas WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteIdeas(): Flow<List<IdeaEvaluation>>

    @Query("SELECT * FROM saved_ideas WHERE category = :category ORDER BY createdAt DESC")
    fun getIdeasByCategory(category: String): Flow<List<IdeaEvaluation>>

    @Query("SELECT * FROM saved_ideas WHERE id = :id LIMIT 1")
    suspend fun getIdeaById(id: Long): IdeaEvaluation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: IdeaEvaluation): Long

    @Update
    suspend fun updateIdea(idea: IdeaEvaluation)

    @Delete
    suspend fun deleteIdea(idea: IdeaEvaluation)

    @Query("DELETE FROM saved_ideas WHERE id = :id")
    suspend fun deleteIdeaById(id: Long)
}
