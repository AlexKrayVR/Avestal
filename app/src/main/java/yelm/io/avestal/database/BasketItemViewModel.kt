package yelm.io.avestal.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class BasketItemViewModel(private val repository: BasketRepository) : ViewModel() {

    val allItems: LiveData<List<BasketItem>> = repository.allItems.asLiveData()

    fun insert(basketItem: BasketItem) = viewModelScope.launch {
        repository.insert(basketItem)
    }

    fun increase(itemID: String) = viewModelScope.launch {
        repository.increase(itemID)
    }

    fun reduce(itemID: String) = viewModelScope.launch {
        repository.reduce(itemID)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteByID(itemID: String) = viewModelScope.launch {
        repository.deleteByID(itemID)
    }

}

class BasketItemModelFactory(private val repository: BasketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BasketItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}