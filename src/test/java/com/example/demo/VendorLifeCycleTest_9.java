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
public class VendorLifeCycleTest_9 {

    @Autowired
    private AssetRepository assetRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        assetRepository.deleteAll();
    }

    @Test
    void saveAssetWithNewVendor_bothInserted() {
        Vendor vendor = new Vendor();
        vendor.setName("New Vendor");

        Asset asset = new Asset();
        asset.setName("Child with new parent");
        asset.setVendor(vendor);

        Asset saved = assetRepository.save(asset);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertNotNull(saved.getVendor().getId());
    }

    @Test
    void persistAssetWithNewVendor_bothInserted() {
        Vendor vendor = new Vendor();
        vendor.setName("New Vendor");

        Asset asset = new Asset();
        asset.setName("Child with new parent");
        asset.setVendor(vendor);

        entityManager.persist(asset);
        entityManager.flush();

        Assertions.assertNotNull(asset.getId());
        Assertions.assertNotNull(vendor.getId());
    }

    @Test
    void mergeAssetWithNewVendor_bothInserted() {
        Vendor vendor = new Vendor();
        vendor.setName("New Vendor");

        Asset asset = new Asset();
        asset.setName("Child with new parent");
        asset.setVendor(vendor);

        Asset merged = entityManager.merge(asset);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertNotNull(merged.getVendor().getId());
    }
}
