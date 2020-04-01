package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.GlobalConstants;
import softuni.exam.models.dtos.PictureSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final CarService carService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarService carService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.pictureRepository = pictureRepository;
        this.carService = carService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(GlobalConstants.PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {

        StringBuilder builder = new StringBuilder();
        PictureSeedDto[] pictureSeedDtos =
                this.gson.fromJson(
                        new FileReader(GlobalConstants.PICTURES_FILE_PATH),
                        PictureSeedDto[].class);

        for (PictureSeedDto pictureSeedDto : pictureSeedDtos) {
            if (this.validationUtil.isValid(pictureSeedDto)) {
                if (this.pictureRepository.findByName(pictureSeedDto.getName()) == null) {
                    Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);

                    Car car = this.carService.getById(pictureSeedDto.getCar());
                    LocalDateTime localDateTime = LocalDateTime.parse(pictureSeedDto.getDateAndTime(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    if (car != null) {
                        picture.setCar(car);
                        picture.setDateAndTime(localDateTime);
                        builder.append(String.format(
                                "Successfully import picture - %s",
                                picture.getName()
                        ));

                        this.pictureRepository.saveAndFlush(picture);
                    } else {
                        builder.append("Invalid picture");
                    }
                } else {
                    builder.append("Invalid picture");
                }
            } else {
                builder.append("Invalid picture");
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    public List<Picture> findByCar_Id(int id) {
        return this.pictureRepository.findByCar_Id(id);
    }
}
