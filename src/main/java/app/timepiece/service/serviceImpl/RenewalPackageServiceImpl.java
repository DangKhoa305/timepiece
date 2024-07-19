package app.timepiece.service.serviceImpl;

import app.timepiece.dto.RenewalPackageCreateDTO;
import app.timepiece.entity.RenewalPackage;
import app.timepiece.repository.RenewalPackageRepository;
import app.timepiece.service.RenewalPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenewalPackageServiceImpl implements RenewalPackageService {

    private final RenewalPackageRepository renewalPackageRepository;

    @Autowired
    public RenewalPackageServiceImpl(RenewalPackageRepository renewalPackageRepository) {
        this.renewalPackageRepository = renewalPackageRepository;
    }

    @Override
    public List<RenewalPackage> getAllRenewalPackages() {
        return renewalPackageRepository.findAll();
    }
    @Override
    public RenewalPackage createRenewalPackage(RenewalPackageCreateDTO renewalPackageCreateDTO) {
        RenewalPackage renewalPackage = RenewalPackage.builder()
                .name(renewalPackageCreateDTO.getName())
                .duration(renewalPackageCreateDTO.getDuration())
                .price(renewalPackageCreateDTO.getPrice())
                .build();
        return renewalPackageRepository.save(renewalPackage);
    }
}

