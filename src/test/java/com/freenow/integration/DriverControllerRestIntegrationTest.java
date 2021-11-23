package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class DriverControllerRestIntegrationTest {

    private static final String URL_PATH = "/v1/drivers";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CarRepository carRepository;

    @AfterAll
    @Before
    public void resetDb() {
        driverRepository.deleteAll();
        carRepository.deleteAll();
    }

    private CarDO createTestCar(String licensePlate, Integer seatCount, Boolean convertible, String model, double rating, EngineType engineType, Manufacturer manufacturer) {
        CarDO carDO = new CarDO(licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        return carRepository.save(carDO);
    }

    private DriverDO createTestDriver(String username, String password) {
        DriverDO driverDO = new DriverDO(username, password);
        return driverRepository.save(driverDO);
    }

    @Test
    public void whenInvalidInput_thenReturn400_forCreateDriver() throws Exception {
        // Missing username
        String testCase1 = "{\"password\":\"1234\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase1))
                .andExpect(status().isBadRequest());
        // @formatter:on

        // Missing password
        String testCase2 = "{\"username\":\"driver01\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase2))
                .andExpect(status().isBadRequest());
        // @formatter:on

        //Missing username and password
        String testCase3 = "{}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase3))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturn400_forCreateDriver() throws Exception {
        // Missing username
        String testCase1 = "{\"password\":\"1234\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase1))
                .andExpect(status().isBadRequest());
        // @formatter:on

        // Missing password
        String testCase2 = "{\"username\":\"driver01\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase2))
                .andExpect(status().isBadRequest());
        // @formatter:on

        //Missing username and password
        String testCase3 = "{}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase3))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSameUsername_thenReturn400_forCreateDriver() throws Exception {
        createTestDriver("driver01", "12345");

        String testCase = "{\"username\":\"driver01\",\"password\":\"12121\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenValidInput_thenReturn201_forCreateDriver() throws Exception {

        String testCase = "{\"username\":\"driver01\",\"password\":\"12121\"}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("driver01")))
                .andExpect(jsonPath("$.password", is("12121")));
        // @formatter:on
    }

    @Test
    public void whenMissingRequiredInputFields_thenReturn400_forUpdateLocation() throws Exception {
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/1").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenValidInputFields_thenReturn200_forUpdateLocation() throws Exception {
        DriverDO driverDO = createTestDriver("driver01", "12345");
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + driverDO.getId() + "?longitude=40.32&latitude=-73.32").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void whenInvalidDriverId_thenReturn404_forUpdateLocation() throws Exception {
        DriverDO driverDO = createTestDriver("driver01", "12345");
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/110" + "?longitude=40.32&latitude=-73.32").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenInvalidDriverId_thenReturn404_forSelectCar() throws Exception {
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/80/car/1/select").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenDriverIsOffline_thenReturn400_forSelectCar() throws Exception {
        DriverDO driverDO = createTestDriver("driver01", "12345");
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + driverDO.getId() + "/car/1/select").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenCanIsAlreadyInUse_thenReturn400_forSelectCar() throws Exception {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO1.setCar(carDO);

        DriverDO targetDriverDO = createTestDriver("driver02", "12345");

        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + targetDriverDO.getId() + "/car/" + carDO.getId() + "/select").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenCarAvailableAndOnlineStatus_thenReturn200_forSelectCar() throws Exception {
        // Given
        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        DriverDO targetDriverDO = createTestDriver("driver02", "12345");
        targetDriverDO.setOnlineStatus(
                OnlineStatus.ONLINE
        );

        driverRepository.save(targetDriverDO);

        String testCase = "{}";

        // When
        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + targetDriverDO.getId() + "/car/" + carDO.getId() + "/select").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void whenInvoked_thenReturn200_forDeselectCar() throws Exception {
        DriverDO driverDO = createTestDriver("driver01", "12345");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO.setCar(carDO);

        driverRepository.save(driverDO);

        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + driverDO.getId() + "/car/deselect").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void whenInvalidDriverId_thenReturn404_forDeselectCar() throws Exception {
        String testCase = "{}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/50/car/deselect").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenInvalidDriverId_thenReturn404_forGetDriver() throws Exception {
        createTestDriver("driver01", "12345");

        // @formatter:off
        mvc.perform(get(URL_PATH + "/100"))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenValidDriverId_thenReturn200_forGetDriver() throws Exception {
        DriverDO driverDO = createTestDriver("driver01", "12345");

        // @formatter:off
        mvc.perform(get(URL_PATH + "/" + driverDO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(driverDO.getUsername())))
                .andExpect(jsonPath("$.password", is(driverDO.getPassword())));
        // @formatter:on
    }

    @Test
    public void whenInvoked_thenReturn200_forGetDriverList() throws Exception {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "54321");

        // @formatter:off
        mvc.perform(get(URL_PATH + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is(driverDO1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(driverDO2.getUsername())));
        // @formatter:on
    }

    @Test
    public void filterByUsername_forGetDriverList() throws Exception {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "54321");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        // @formatter:off
        mvc.perform(get(URL_PATH + "/?username=driver01"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(1))))
                .andExpect(jsonPath("$[0].username", is(driverDO1.getUsername())));
        // @formatter:on
    }

    @Test
    public void filterByOnlineStatus_forGetDriverList() throws Exception {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "54321");

        // @formatter:off
        mvc.perform(get(URL_PATH + "/?onlineStatus=OFFLINE"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(2))))
                .andExpect(jsonPath("$[0].username", is(driverDO1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(driverDO2.getUsername())));
        // @formatter:on
    }

    @Test
    public void filterByEngineTypeOrManufacturer_forGetDriverList() throws Exception {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "54321");
        DriverDO driverDO3 = createTestDriver("driver03", "54321");

        CarDO carDO1 = createTestCar(
                "12AXCD13",
                5,
                false,
                "2017",
                7.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        CarDO carDO2 = createTestCar(
                "12AXCD14",
                5,
                false,
                "2016",
                5.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        CarDO carDO3 = createTestCar(
                "12AXCD16",
                5,
                false,
                "2016",
                5.0,
                EngineType.GAS,
                Manufacturer.AUDI
        );

        driverDO1.setCar(carDO1);
        driverDO2.setCar(carDO2);
        driverDO3.setCar(carDO3);

        driverRepository.saveAll(Arrays.asList(driverDO1, driverDO2, driverDO3));

        // @formatter:off
        mvc.perform(get(URL_PATH + "/?engineType=DIESEL"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(2))))
                .andExpect(jsonPath("$[0].username", is(driverDO1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(driverDO2.getUsername())));
        // @formatter:on

        // @formatter:off
        mvc.perform(get(URL_PATH + "/?manufacturer=AUDI"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(2))))
                .andExpect(jsonPath("$[0].username", is(driverDO2.getUsername())))
                .andExpect(jsonPath("$[1].username", is(driverDO3.getUsername())));
        // @formatter:on

        // @formatter:off
        mvc.perform(get(URL_PATH + "/?manufacturer=AUDI&engineType=DIESEL"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", hasSize(1))))
                .andExpect(jsonPath("$[0].username", is(driverDO2.getUsername())));
        // @formatter:on
    }
}
