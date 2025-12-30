package com.bmt.NiagaraViajesParqueBicentenario.Repositories;

import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
    List<TravelPackage> findByTituloContainingIgnoreCase(String titulo);
    List<TravelPackage> findByPrecioLessThanEqual(Double precioMaximo);
}