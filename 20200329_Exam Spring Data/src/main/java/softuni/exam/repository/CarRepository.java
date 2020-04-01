package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Car;

import java.util.HashSet;
import java.util.LinkedHashSet;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Car findByMakeAndModelAndKilometers(String make, String model, int kilometers);
    Car getById(int id);

    LinkedHashSet<Car> getAllByPicturesNotNullOrderByMakeAsc();

    @Query("SELECT c from Car as c inner join Picture as p on p.car.id = c.id GROUP BY c.id order by count(p.id) desc , c.make")
    HashSet<Car> findAllByPicturesIsNotNullOrderByMake();


}
