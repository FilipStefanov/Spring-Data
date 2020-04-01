package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.GlobalConstants;
import softuni.exam.models.dtos.SellerRootDto;
import softuni.exam.models.dtos.SellerSeedRootDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final ValidationUtil validationUtil;
    private  final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, ValidationUtil validationUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.sellerRepository = sellerRepository;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public Seller getById(int id) {
        return this.sellerRepository.getById(id);
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(GlobalConstants.SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        SellerSeedRootDto sellerSeedRootDtos = this.xmlParser.parseXml(SellerSeedRootDto.class, GlobalConstants.SELLERS_FILE_PATH);

        for (SellerRootDto sellerRootDto : sellerSeedRootDtos.getSellers()) {
            if (this.validationUtil.isValid(sellerRootDto)){
                if (this.sellerRepository.findByEmail(sellerRootDto.getEmail()) == null){

                    Seller seller = this.modelMapper.map(sellerRootDto, Seller.class);

                    builder.append(String.format(
                            "Successfully import seller %s - %s",
                            seller.getLastName(),
                            seller.getEmail()
                    ));

                    this.sellerRepository.saveAndFlush(seller);

                }else {
                    builder.append("Invalid seller");
                }
            }else {
                builder.append("Invalid seller");
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }
}
