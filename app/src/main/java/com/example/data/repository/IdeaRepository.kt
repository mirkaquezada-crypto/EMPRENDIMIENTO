package com.example.data.repository

import com.example.data.db.IdeaDao
import com.example.data.model.IdeaEvaluation
import kotlinx.coroutines.flow.Flow

class IdeaRepository(private val ideaDao: IdeaDao) {
    val allIdeas: Flow<List<IdeaEvaluation>> = ideaDao.getAllIdeas()
    val favoriteIdeas: Flow<List<IdeaEvaluation>> = ideaDao.getFavoriteIdeas()

    fun getIdeasByCategory(category: String): Flow<List<IdeaEvaluation>> =
        ideaDao.getIdeasByCategory(category)

    suspend fun getIdeaById(id: Long): IdeaEvaluation? =
        ideaDao.getIdeaById(id)

    suspend fun insert(idea: IdeaEvaluation): Long =
        ideaDao.insertIdea(idea)

    suspend fun update(idea: IdeaEvaluation) =
        ideaDao.updateIdea(idea)

    suspend fun toggleFavorite(idea: IdeaEvaluation) {
        ideaDao.updateIdea(idea.copy(isFavorite = !idea.isFavorite))
    }

    suspend fun delete(idea: IdeaEvaluation) =
        ideaDao.deleteIdea(idea)

    suspend fun deleteById(id: Long) =
        ideaDao.deleteIdeaById(id)
}
