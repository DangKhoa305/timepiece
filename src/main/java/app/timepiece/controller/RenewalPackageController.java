package app.timepiece.controller;

import app.timepiece.dto.RenewalPackageCreateDTO;
import app.timepiece.entity.RenewalPackage;
import app.timepiece.service.RenewalPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renewal-packages")
public class RenewalPackageController {

    private final RenewalPackageService renewalPackageService;

    @Autowired
    public RenewalPackageController(RenewalPackageService renewalPackageService) {
        this.renewalPackageService = renewalPackageService;
    }

    @GetMapping("/GetAllRenewalPakage")
    public List<RenewalPackage> getAllRenewalPackages() {
        return renewalPackageService.getAllRenewalPackages();
    }

    @PostMapping
    public RenewalPackage createRenewalPackage(@RequestBody RenewalPackageCreateDTO renewalPackageCreateDTO) {
        return renewalPackageService.createRenewalPackage(renewalPackageCreateDTO);
    }
}
