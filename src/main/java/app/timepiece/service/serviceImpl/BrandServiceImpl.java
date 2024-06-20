package app.timepiece.service.serviceImpl;

import app.timepiece.dto.BrandDTO;
import app.timepiece.entity.Brand;
import app.timepiece.repository.BrandRepository;
import app.timepiece.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Optional<Brand> createBrand(String brandName) {
        if (brandRepository.findByBrandName(brandName).isPresent()) {
            return Optional.empty();
        }
        Brand brand = new Brand();
        brand.setBrandName(brandName);
        brand.setStatus("wait");
        return Optional.of(brandRepository.save(brand));
    }

    @Override
    public List<BrandDTO> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand -> new BrandDTO(brand.getId(), brand.getBrandName()))
                .collect(Collectors.toList());
    }

    @Override
    public BrandDTO updateBrandName(Long id, String brandName) {
        Optional<Brand> brandOptional = brandRepository.findById(id);

        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brand.setBrandName(brandName);
            Brand updatedBrand = brandRepository.save(brand);
            return new BrandDTO(updatedBrand.getId(), updatedBrand.getBrandName());
        } else {
            throw new RuntimeException("Brand not found");
        }


    }
}