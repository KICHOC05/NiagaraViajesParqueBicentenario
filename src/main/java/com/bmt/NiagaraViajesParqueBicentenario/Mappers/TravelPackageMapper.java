package com.bmt.NiagaraViajesParqueBicentenario.Mappers;

import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackage;
import com.bmt.NiagaraViajesParqueBicentenario.Models.TravelPackageDto;
import org.springframework.stereotype.Component;

@Component
public class TravelPackageMapper {
    
    public TravelPackageDto toDto(TravelPackage travelPackage) {
        if (travelPackage == null) {
            return null;
        }
        
        TravelPackageDto dto = new TravelPackageDto();
        dto.setId(travelPackage.getId());
        dto.setTitulo(travelPackage.getTitulo());
        dto.setPrecio(travelPackage.getPrecio());
        dto.setDescripcion(travelPackage.getDescripcion());
        dto.setDias(travelPackage.getDias());
        dto.setNoches(travelPackage.getNoches());
        dto.setPersonas(travelPackage.getPersonas());
        dto.setImagenUrl(travelPackage.getImagenUrl());
        
        return dto;
    }
    
    public TravelPackage toEntity(TravelPackageDto dto) {
        if (dto == null) {
            return null;
        }
        
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(dto.getId());
        travelPackage.setTitulo(dto.getTitulo());
        travelPackage.setPrecio(dto.getPrecio());
        travelPackage.setDescripcion(dto.getDescripcion());
        travelPackage.setDias(dto.getDias());
        travelPackage.setNoches(dto.getNoches());
        travelPackage.setPersonas(dto.getPersonas());
        travelPackage.setImagenUrl(dto.getImagenUrl());
        
        return travelPackage;
    }
}