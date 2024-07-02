package app.timepiece.controller;

import app.timepiece.dto.BrandDTO;
import app.timepiece.entity.Brand;
import app.timepiece.service.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private Validator validator;

    @PostMapping("/createBrand")
    public ResponseEntity<?> createBrand(@Valid @RequestParam @NotBlank(message = "Brand name is required") String brandName) {
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
    public ResponseEntity<?> updateBrandName(@PathVariable Long id, @Valid @RequestBody @NotBlank(message = "Brand name is required") String brandName) {
        try {
            BrandDTO updatedBrand = brandService.updateBrandName(id, brandName);
            return ResponseEntity.ok(updatedBrand);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}