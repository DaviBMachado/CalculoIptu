package com.example.calculoiptu.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IptuDao {

    @Insert
    void inserir(IptuRecord record);

    @Query("SELECT * FROM iptu_records ORDER BY id DESC")
    List<IptuRecord> listarTodos();

    @Query("SELECT * FROM iptu_records WHERE cpf = :cpf ORDER BY id DESC")
    List<IptuRecord> listarPorCpf(String cpf);
}
