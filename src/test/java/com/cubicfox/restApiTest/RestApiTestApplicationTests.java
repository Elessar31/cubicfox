package com.cubicfox.restApiTest;

import com.cubicfox.restApiTest.Util.EmailValidatorHelper;
import com.cubicfox.restApiTest.model.Address;
import com.cubicfox.restApiTest.model.Company;
import com.cubicfox.restApiTest.model.Geo;
import com.cubicfox.restApiTest.model.Users;
import com.cubicfox.restApiTest.provider.UserProvider;
import com.cubicfox.restApiTest.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    private UserProvider userProvider;

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
        Users dbUser = userProvider.getUserById(user.getId());
        assertThat(user.getName(), is(dbUser.getName()));
        assertThat(user.getUsername(), is(dbUser.getUsername()));
        assertThat(user.getEmail(), is(dbUser.getEmail()));
        assertThat(user.getAddress().getStreet(), is(dbUser.getAddress().getStreet()));
        assertThat(user.getAddress().getSuite(), is(dbUser.getAddress().getSuite()));
        assertThat(user.getAddress().getCity(), is(dbUser.getAddress().getCity()));
        assertThat(user.getAddress().getZipcode(), is(dbUser.getAddress().getZipcode()));
        assertThat(user.getAddress().getGeo().getLat(), Matchers.comparesEqualTo(dbUser.getAddress().getGeo().getLat()));
        assertThat(user.getAddress().getGeo().getLng(), Matchers.comparesEqualTo(dbUser.getAddress().getGeo().getLng()));
        assertThat(user.getPhone(), is(dbUser.getPhone()));
        assertThat(user.getWebsite(), is(dbUser.getWebsite()));
        assertThat(user.getCompany().getName(), is(dbUser.getCompany().getName()));
        assertThat(user.getCompany().getCatchPhrase(), is(dbUser.getCompany().getCatchPhrase()));
        assertThat(user.getCompany().getBs(), is(dbUser.getCompany().getBs()));
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
    void cleanDb() {
        Sql2o sql2o = new Sql2o(dataSource);
        try (Connection connection = sql2o.open(); Query cleanQuery = connection.createQuery("TRUNCATE TABLE users")) {
            cleanQuery.executeUpdate();
        }
    }

    private Users createUser() {
        Users users = new Users();
        Address address = new Address();
        Geo geo = new Geo();
        Company company = new Company();
        users.setCompany(company);
        users.setAddress(address);
        users.getAddress().getGeo().setLat(new BigDecimal("222.22"));
        users.getAddress().getGeo().setLng(new BigDecimal("222.22"));
        users.getAddress().setCity("Pécs");
        users.getAddress().setStreet("Majorossy");
        users.getAddress().setZipcode("7625");
        users.getAddress().setSuite("suite");
        users.getCompany().setBs("bs");
        users.getCompany().setName("CubicFox");
        users.getCompany().setCatchPhrase("CatchPhrase");
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



}
