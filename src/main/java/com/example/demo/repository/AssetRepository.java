package com.example.demo.repository;

import com.example.demo.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository
        extends JpaRepository<Asset, Long>
{
}
