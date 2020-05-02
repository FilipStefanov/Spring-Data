package com.xmlprocessing.serrvices;

import com.xmlprocessing.models.dtos.SupplierSeedDto;

import java.util.List;

public interface SupplierService {
    void seedSuppliers(List<SupplierSeedDto> supplierSeedDtos);
}
