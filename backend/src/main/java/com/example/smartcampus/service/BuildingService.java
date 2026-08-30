package com.example.smartcampus.service;

import com.example.smartcampus.entity.Building;
import com.example.smartcampus.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id).orElse(null);
    }
    public Building updateBuilding(Long id, Building updatedBuilding) {
        Building existing = buildingRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setName(updatedBuilding.getName());
        existing.setLocation(updatedBuilding.getLocation());
        existing.setBuildingType(updatedBuilding.getBuildingType());
        return buildingRepository.save(existing);
    }

    public boolean deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            return false;
        }
        buildingRepository.deleteById(id);
        return true;
    }
}