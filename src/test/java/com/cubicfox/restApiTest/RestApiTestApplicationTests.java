package com.cubicfox.restApiTest;

import com.cubicfox.restApiTest.Util.EmailValidatorHelper;
import com.cubicfox.restApiTest.model.Address;
import com.cubicfox.restApiTest.model.Company;
import com.cubicfox.restApiTest.model.Geo;
import com.cubicfox.restApiTest.model.Users;
import com.cubicfox.restApiTest.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class RestApiTestApplicationTests {

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private DataSource dataSource;

    private static Stream<Arguments> emailValidatorTest() {
        return Stream.of(
                Arguments.of("attila.blazsek@gmail.com", true),
                Arguments.of("attilablazse@gmail.com", true),
                Arguments.of("attila_blazsek@gmail.com", true),
                Arguments.of("attila...@gmail.com", false),
                Arguments.of("@gmail.com", false),
                Arguments.of("attilagmail.com", false),
                Arguments.of("", false),
                Arguments.of("attila@gmail...com", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void emailValidatorTest(String email, boolean expected) {
        assertThat(EmailValidatorHelper.emailIsValid(email), is(expected));
    }

    @Test
    void saveUserTest() {
        Users user = createUser();
        assertThat(getDBRowCount(), is(0));
        userServiceImpl.save(user);
        assertThat(getDBRowCount(), is(1));
        Users dbUser = getUserFromDb(user.getId());
        assertThat(user.getName(), is(dbUser.getName()));
        assertThat(user.getUsername(), is(dbUser.getUsername()));
        assertThat(user.getEmail(), is(dbUser.getEmail()));
    }

    @Test
    void saveUserWrongEmailTest() {
        Users user = createUser();
        user.setEmail("");
        assertThat(getDBRowCount(), is(0));
        userServiceImpl.save(user);
        assertThat(getDBRowCount(), is(0));
    }
    @Test
    void saveDuplicateUserTest() {
        Users user = createUser();
        assertThat(getDBRowCount(), is(0));
        userServiceImpl.save(user);
        assertThat(getDBRowCount(), is(1));
        userServiceImpl.save(user);
        assertThat(getDBRowCount(), is(1));
    }

    @AfterEach
    void cleanDb(){
        Sql2o sql2o = new Sql2o(dataSource);
        try(Connection connection = sql2o.open(); Query cleanQuery = connection.createQuery("TRUNCATE TABLE users")){
            cleanQuery.executeUpdate();
        }
    }

    private Users createUser() {
        Users users = new Users();
        Address address = new Address();
        Company company = new Company();
        Geo geo = new Geo();
        geo.setLat(new BigDecimal("222.22"));
        geo.setLng(new BigDecimal("222.22"));
        address.setGeo(geo);
        address.setCity("Pécs");
        address.setStreet("Majorossy");
        address.setZipcode("7625");
        address.setSuite("suite");
        users.setAddress(address);
        company.setBs("bs");
        company.setName("CubicFox");
        company.setCatchPhrase("CatchPhrase");
        users.setCompany(company);
        users.setName("Attila");
        users.setUsername("Attila");
        users.setId(1);
        users.setPhone("+3636266028");
        users.setEmail("attila.blazsek@gmail.com");
        users.setWebsite("nincs");
        return users;
    }

    private int getDBRowCount() {
        Sql2o sql2o = new Sql2o(dataSource);
        try (Connection connection = sql2o.open(); Query query = connection.createQuery("SELECT count(1) FROM users")) {
            return query.executeScalar(Integer.class);
        }
    }

    private Users getUserFromDb(int id) {
        Sql2o sql2o = new Sql2o(dataSource);
        try (Connection connection = sql2o.open(); Query query = connection.createQuery("SELECT id, name, username, email FROM users where id = :id")) {
            query.addParameter("id", id);
            query.setAutoDeriveColumnNames(true);
            query.setResultSetHandlerFactoryBuilder(new SfmResultSetHandlerFactoryBuilder());
            return query.executeAndFetchFirst(Users.class);
        }
    }

}
