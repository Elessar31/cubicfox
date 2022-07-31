package com.cubicfox.restApiTest;

import com.cubicfox.restApiTest.client.UserClient;
import com.cubicfox.restApiTest.service.TestService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WireMockConfig.class})
class RestApiTestIntegrationTest {

    @Autowired
    private WireMockServer mockUserService;

    @Autowired
    private TestService testServiceImpl;

    @Qualifier("com.cubicfox.restApiTest.client.UserClient")
    @Autowired
    private UserClient userClient;

    @BeforeEach
    void setUp() throws IOException {
        UserMock.setupMockUsersResponse(mockUserService);
    }

    @Test
    public void whenGetBooks_thenBooksShouldBeReturned() {
        var users = userClient.getUsers();
        assertThat(userClient.getUsers().isEmpty(), is(false));
    }

    @Test
    void testAllTest() {
        var response = testServiceImpl.fullTest();
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getSuccessCallCount(), is((TestService.NUMBER_OF_TRIES)));
        assertThat(response.getResultNumber(), is(2));
        assertThat(response.getSuccessSaveUser(), is(2));
        assertThat(response.getErrorList().isEmpty(), is(true));
    }

}

