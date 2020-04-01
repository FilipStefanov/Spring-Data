package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.GlobalConstants;
import softuni.exam.models.dtos.OfferRootDto;
import softuni.exam.models.dtos.OfferSeedRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.PictureService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final CarService carService;
    private final SellerService sellerService;
    private final PictureService pictureService;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ValidationUtil validationUtil, XmlParser xmlParser, ModelMapper modelMapper, CarService carService, SellerService sellerService, PictureService pictureService) {
        this.offerRepository = offerRepository;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.carService = carService;
        this.sellerService = sellerService;

        this.pictureService = pictureService;
    }


    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(GlobalConstants.OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        OfferSeedRootDto offerSeedRootDtos = this.xmlParser.parseXml(OfferSeedRootDto.class, GlobalConstants.OFFERS_FILE_PATH);

        for (OfferRootDto offerRootDto : offerSeedRootDtos.getOffers()) {

            if (this.validationUtil.isValid(offerRootDto)) {

                LocalDateTime localDateTime =
                        LocalDateTime.parse(
                                offerRootDto.getAddedOn(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                if (this.offerRepository.getByDescriptionAndAddedOn(
                        offerRootDto.getDescription(),
                        localDateTime) == null) {
                    Offer offer = this.modelMapper.map(
                            offerRootDto,
                            Offer.class
                    );

                    Car car = this.carService.getById(offerRootDto.getCar().getId());
                    Seller seller = this.sellerService.getById(offerRootDto.getSeller().getId());

                    if (car != null && seller != null) {

                        offer.setAddedOn(localDateTime);
                        offer.setCar(car);
                        offer.setSeller(seller);

                        offer.setPictures(this.pictureService.findByCar_Id(car.getId()));

                        builder.append(String.format(
                                "Successfully import seller %s - %s",
                                offer.getAddedOn().toString(),
                                offer.isHasGoldStatus()
                        ));

                        this.offerRepository.saveAndFlush(offer);

                    } else {
                        builder.append("Invalid offer");
                    }
                } else {
                    builder.append("Invalid offer");
                }

            } else {
                builder.append("Invalid offer");
            }
            builder.append(System.lineSeparator());
        }


        return builder.toString();
    }
}
