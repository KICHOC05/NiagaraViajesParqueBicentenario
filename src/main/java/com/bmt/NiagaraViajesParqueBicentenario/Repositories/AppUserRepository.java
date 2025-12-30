package com.bmt.NiagaraViajesParqueBicentenario.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmt.NiagaraViajesParqueBicentenario.Models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer>{

	public AppUser findByEmail(String email);
}