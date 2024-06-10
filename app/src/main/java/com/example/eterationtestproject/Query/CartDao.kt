package com.example.eterationtestproject.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eterationtestproject.models.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cart: CartEntity)

    @Update
    suspend fun update(cart: CartEntity)

    @Delete
    suspend fun delete(cart: CartEntity)

    @Query("SELECT * FROM cart_table WHERE name = :name LIMIT 1")
    fun getCartItemByName(name: String): LiveData<CartEntity?>

    @Query("SELECT * FROM cart_table WHERE name = :name LIMIT 1")
    suspend fun getCartItemByNameSync(name: String): CartEntity?

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): LiveData<List<CartEntity>>
}
