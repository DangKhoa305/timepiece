package app.timepiece.service;

import app.timepiece.dto.BrandDTO;
import app.timepiece.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    Optional<Brand> createBrand(String brandName);
    List<BrandDTO> getAllBrands();
    BrandDTO updateBrandName(Long id, String brandName);
}

