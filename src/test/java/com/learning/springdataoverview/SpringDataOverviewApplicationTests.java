package com.learning.springdataoverview;

import com.learning.springdataoverview.entity.Flight;
import com.learning.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpringDataOverviewApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Test
	void verifyFlightCanBeSaved() {
		final Flight flight =  new Flight();
		flight.setOrigin("London");
		flight.setDestination("New York");
		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

		entityManager.persist(flight);

		final TypedQuery<Flight> results = entityManager
				.createQuery("select f from Flight f", Flight.class);

		final List<Flight> resultList = results.getResultList();

		assertThat(resultList)
				.hasSize(1)
				.first()
				.isEqualTo(flight);

	}

	@Autowired
	private FlightRepository flightRepository;

	@Test
	public void shouldPerformCRUDOperations() {
		final Flight flight =  new Flight();
		flight.setOrigin("London");
		flight.setDestination("New York");
		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

		flightRepository.save(flight);

		assertThat(flightRepository.findAll())
				.hasSize(1)
				.first()
				.isEqualTo(flight);
//				.isEqualToComparingFieldByField(flight);

		flightRepository.deleteById(flight.getId());

		assertThat(flightRepository.count()).isZero();
	}

	@Test
	public void shouldPageResults() {
		for (int i = 0; i < 50; i++) {
			flightRepository.save(createFlight(String.valueOf(i)));
		}

		final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));

		assertThat(page.getTotalElements()).isEqualTo(50);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(10);
		assertThat(page.getContent())
				.extracting(Flight::getDestination)
				.containsExactly("10", "11", "12", "13", "14");
	}

	private Flight createFlight(String destination) {
		return createFlight(destination, LocalDateTime.parse("2011-12-13T12:12:00"));
	}

	private Flight createFlight(String destination, LocalDateTime scheduledAt) {
		Flight flight = new Flight();
		flight.setDestination(destination);
		flight.setOrigin("London");
		flight.setScheduledAt(scheduledAt);
		return flight;
	}

}
