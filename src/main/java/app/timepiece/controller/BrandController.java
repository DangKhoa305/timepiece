package app.timepiece.controller;

import app.timepiece.dto.BrandDTO;
import app.timepiece.entity.Brand;
import app.timepiece.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/createBrand")
    public ResponseEntity<?> createBrand(@RequestParam String brandName) {
        Optional<Brand> brand = brandService.createBrand(brandName);
        if (brand.isPresent()) {
            return new ResponseEntity<>(brand.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Brand name already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public List<BrandDTO> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PutMapping("/{id}/updateBrand")
    public BrandDTO updateBrandName(@PathVariable Long id, @RequestBody String brandName) {
        return brandService.updateBrandName(id, brandName);
    }

}