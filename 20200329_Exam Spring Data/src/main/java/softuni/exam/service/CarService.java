package softuni.exam.service;



import softuni.exam.models.entities.Car;

import java.io.IOException;


public interface CarService {
    Car getById(int id);

    boolean areImported();

    String readCarsFileContent() throws IOException;
	
	String importCars() throws IOException;

    String getCarsOrderByPicturesCountThenByMake();
}
