package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CarControllerRestIntegrationTest {

    private static final String URL_PATH = "/v1/cars";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;

    @AfterAll
    @Before
    public void resetDb() {
        carRepository.deleteAll();
    }

    private CarDO createTestCar(String licensePlate, Integer seatCount, Boolean convertible, String model, double rating, EngineType engineType, Manufacturer manufacturer) {
        CarDO carDO = new CarDO(licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        return carRepository.save(carDO);
    }

    @Test
    public void whenInvalidInput_thenReturn400_forCreateCar() throws Exception {

        // Missing engine type
        String testCase1 = "{\"convertible\":true,\"licensePlate\":\"string\",\"manufacturer\":\"AUDI\",\"model\":\"string\",\"rating\":0,\"seatCount\":5}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase1))
                .andExpect(status().isBadRequest());
        // @formatter:on

        //// Missing licensePlate and manufacturer
        String testCase2 = "{\"convertible\":true,\"engineType\":\"DIESEL\",\"id\":0,\"model\":\"string\",\"rating\":0,\"seatCount\":0}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase2))
                .andExpect(status().isBadRequest());
        // @formatter:on

        //Invalid seatCount
        String testCase3 = "{\"convertible\":true,\"engineType\":\"DIESEL\",\"id\":0,\"licensePlate\":\"string\",\"manufacturer\":\"AUDI\",\"model\":\"string\",\"rating\":0,\"seatCount\":0}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase3))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSameLicensePlate_thenReturn400_forCreateCar() throws Exception {
        createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        String testCase = "{\"convertible\":true,\"engineType\":\"DIESEL\",\"licensePlate\":\"12AXCD12\",\"manufacturer\":\"AUDI\",\"model\":\"2019\",\"rating\":0,\"seatCount\":5}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenValidInput_thenReturn201_forCreateCar() throws Exception {

        String testCase = "{\"convertible\":true,\"engineType\":\"DIESEL\",\"licensePlate\":\"12AXCD12\",\"manufacturer\":\"AUDI\",\"model\":\"2019\",\"rating\":0,\"seatCount\":5}";

        // @formatter:off
        mvc.perform(post(URL_PATH).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate", is("12AXCD12")))
                .andExpect(jsonPath("$.engineType", is("DIESEL")));
        // @formatter:on
    }

    @Test
    public void whenMissingLicensePlateOrEngineType_thenReturn400_forUpdateCar() throws Exception {

        String testCase = "{\"rating\":9.4}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/1").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenSameLicensePlate_thenReturn400_forUpdateCar() throws Exception {
        CarDO carDO1 = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        CarDO carDO2 = createTestCar(
                "12AXCD13",
                5,
                false,
                "2016",
                0.0,
                EngineType.ELECTRIC,
                Manufacturer.TOYOTA
        );

        String testCase = "{\"engineType\":\"DIESEL\",\"licensePlate\":\"12AXCD13\",\"rating\":3.0}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + carDO1.getId()).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenInvalidRating_thenReturn400_forUpdateCar() throws Exception {
        //Min rating check
        String testCase1 = "{\"engineType\":\"DIESEL\",\"licensePlate\":\"12AXCD12\",\"rating\":-1.3}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/1").contentType(MediaType.APPLICATION_JSON).content(testCase1))
                .andExpect(status().isBadRequest());
        // @formatter:on

        //Max rating check
        String testCase2 = "{\"engineType\":\"DIESEL\",\"licensePlate\":\"12AXCD12\",\"rating\":11.0}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/1").contentType(MediaType.APPLICATION_JSON).content(testCase2))
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void whenValidInput_thenReturn200_forUpdateCar() throws Exception {

        Iterable<CarDO> x = carRepository.findAll();
        CarDO carDO = createTestCar(
                "15SRX45",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        String testCase = "{\"engineType\":\"GAS\",\"licensePlate\":\"15SRX45\",\"rating\":9.0}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/" + carDO.getId()).contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void whenInvalidCarId_thenReturn404_forUpdateCar() throws Exception {

        String testCase = "{\"engineType\":\"GAS\",\"licensePlate\":\"13AXCD12\",\"rating\":9.0}";

        // @formatter:off
        mvc.perform(put(URL_PATH + "/20").contentType(MediaType.APPLICATION_JSON).content(testCase))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenInvalidCarId_thenReturn404_forDeleteCar() throws Exception {

        // @formatter:off
        mvc.perform(delete(URL_PATH + "/20"))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenValidCarId_thenReturn200_forDeleteCar() throws Exception {
        CarDO carDO = createTestCar(
                "77AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        // @formatter:off
        mvc.perform(delete(URL_PATH + "/" + carDO.getId()))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void whenInvoked_thenReturnCarList() throws Exception {
        CarDO carDO1 = createTestCar(
                "13AXER22",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        CarDO carDO2 = createTestCar(
                "13AXCD41",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        // @formatter:off
        mvc.perform(get(URL_PATH + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].id", is(carDO1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(carDO2.getId().intValue())));
        // @formatter:on
    }

    @Test
    public void whenInvalidCarId_thenReturn404_forGetCar() throws Exception {

        // @formatter:off
        mvc.perform(get(URL_PATH + "/12"))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    public void whenValidCarId_thenReturn200_forGetCar() throws Exception {
        CarDO carDO = createTestCar(
                "13AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        // @formatter:off
        mvc.perform(get(URL_PATH + "/" + carDO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(carDO.getId().intValue())));
        // @formatter:on
    }
}
