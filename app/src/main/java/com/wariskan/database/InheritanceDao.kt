package com.kenji.waris.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface InheritanceDao {

    @Insert(onConflict = REPLACE)
    fun insert(inheritance: Inheritance)

    @Update
    fun update(inheritance: Inheritance)

    @Delete
    fun delete(inheritance: Inheritance)

    @Query("SELECT * FROM inheritance_table WHERE id = :id")
    fun get(id: Int): Inheritance?

    @Query("SELECT * FROM inheritance_table ORDER BY id DESC LIMIT 1")
    fun getLatest(): Inheritance?

    @Query("SELECT * FROM inheritance_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Inheritance>>

    @Query("DELETE FROM inheritance_table")
    fun clear()
}