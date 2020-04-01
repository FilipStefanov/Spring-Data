package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.GlobalConstants;
import softuni.exam.models.dtos.CarSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static java.lang.Integer.compare;

@Service
@Transactional
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Car getById(int id) {
        return this.carRepository.getById(id);
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(GlobalConstants.CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder builder = new StringBuilder();

        CarSeedDto[] carSeedDtos = this.gson.fromJson(new FileReader(GlobalConstants.CARS_FILE_PATH), CarSeedDto[].class);
        for (CarSeedDto carSeedDto : carSeedDtos) {
            if (this.validationUtil.isValid(carSeedDto)) {
                if (this.carRepository.findByMakeAndModelAndKilometers(
                        carSeedDto.getMake(),
                        carSeedDto.getModel(),
                        carSeedDto.getKilometers()
                ) == null) {
                    Car car = this.modelMapper.map(carSeedDto, Car.class);

                    LocalDate localDate = LocalDate.parse(
                            carSeedDto.getRegisteredOn(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    );

                    car.setRegisteredOn(localDate);
                    builder.append(String.format(
                            "Successfully imported car - %s - %s",
                            car.getMake(),
                            car.getModel()
                    ));

                    this.carRepository.saveAndFlush(car);

                } else {
                    builder.append("Invalid car");
                }
            } else {
                builder.append("Invalid car");
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder builder = new StringBuilder();
       LinkedHashSet<Car> cars = this.carRepository.getAllByPicturesNotNullOrderByMakeAsc();
        cars.stream().sorted((car1, car2) -> compare(car2.getPictures().size(), car1.getPictures().size()))
                .forEach(car -> {
                    builder.append(String.format(
                            "Car make - %s, model - %s\n" +
                                    "\tKilometers - %d\n" +
                                    "\tRegistered on - %s\n" +
                                    "\tNumber of pictures - %d\n" +
                                    ". . . \n",
                            car.getMake(),
                            car.getModel(),
                            car.getKilometers(),
                            car.getRegisteredOn(),
                            car.getPictures().size()

                    ));
                });
        return builder.toString();
    }
}
