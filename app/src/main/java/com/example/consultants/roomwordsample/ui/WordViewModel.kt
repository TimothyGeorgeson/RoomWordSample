package com.example.consultants.roomwordsample.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.consultants.roomwordsample.data.Word
import com.example.consultants.roomwordsample.data.WordRepository
import com.example.consultants.roomwordsample.data.WordRoomDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

class WordViewModel(application: Application): AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val wordDao = WordRoomDatabase.getDatabase(application, scope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}