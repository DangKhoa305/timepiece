package app.timepiece.service;

import app.timepiece.dto.RenewalPackageCreateDTO;
import app.timepiece.entity.RenewalPackage;

import java.util.List;

public interface RenewalPackageService {
    List<RenewalPackage> getAllRenewalPackages();
    RenewalPackage createRenewalPackage(RenewalPackageCreateDTO renewalPackageCreateDTO);

}
