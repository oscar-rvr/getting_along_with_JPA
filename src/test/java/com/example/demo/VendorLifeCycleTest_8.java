package com.example.demo;

import com.example.demo.model.Asset;
import com.example.demo.model.Vendor;
import com.example.demo.repository.AssetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VendorLifeCycleTest_8 {

    @Autowired
    private AssetRepository assetRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        assetRepository.deleteAll();
    }

    @Test
    void saveAssetWithNewVendor_repositoryInsertsVendor() {
        Vendor vendor = new Vendor();
        vendor.setName("Repo Vendor");

        Asset asset = new Asset(null, "Repo Asset", vendor);

        Asset saved = assetRepository.save(asset);
        assetRepository.flush();

        Assertions.assertNotNull(saved.getId());
        Assertions.assertNotNull(saved.getVendor());
        Assertions.assertNotNull(saved.getVendor().getId());
    }

    @Test
    void persistAssetWithNewVendor_fails() {
        Vendor vendor = new Vendor();
        vendor.setName("Persist Vendor");

        Asset asset = new Asset(null, "Persist Asset", vendor);

        Assertions.assertThrows(Exception.class, () -> {
            entityManager.persist(asset);
            entityManager.flush();
        });
    }

    @Test
    void mergeAssetWithNewVendor_insertsVendor() {
        Vendor vendor = new Vendor();
        vendor.setName("Merge Vendor");

        Asset asset = new Asset(null, "Merge Asset", vendor);

        Asset merged = entityManager.merge(asset);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertNotNull(merged.getVendor());
        Assertions.assertNotNull(merged.getVendor().getId());
    }
}
