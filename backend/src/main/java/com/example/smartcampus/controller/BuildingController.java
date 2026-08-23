package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Building;
import com.example.smartcampus.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    @PostMapping
    public Building createBuilding(@RequestBody Building building) {
        return buildingService.createBuilding(building);
    }

    @GetMapping("/{id}")
    public Building getBuildingById(@PathVariable Long id) {
        return buildingService.getBuildingById(id);
    }
}