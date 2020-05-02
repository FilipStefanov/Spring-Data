package com.xmlprocessing.controllers;

import com.xmlprocessing.constants.GlobalConstants;
import com.xmlprocessing.models.dtos.SupplierSeedRootDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

@Component
public class AppController implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        this.seedSuppliers();

    }

    private void seedSuppliers() throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext =  JAXBContext.newInstance(SupplierSeedRootDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        System.out.println();
        URL fileURL = this.getClass().getResource(GlobalConstants.SUPPLIERS_FILE_PATH);
        System.out.println();
        File file = new File(fileURL.getPath());
        SupplierSeedRootDto supplierSeedRootDto =
                (SupplierSeedRootDto) unmarshaller
                        .unmarshal(new FileReader(file));

        System.out.println();
    }
}
