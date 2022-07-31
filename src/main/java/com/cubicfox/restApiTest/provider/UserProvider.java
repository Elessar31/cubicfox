package com.cubicfox.restApiTest.provider;

import com.cubicfox.restApiTest.model.Address;
import com.cubicfox.restApiTest.model.Company;
import com.cubicfox.restApiTest.model.Geo;
import com.cubicfox.restApiTest.model.Users;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.DefaultResultSetHandlerFactoryBuilder;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.Objects;

@Repository
public class UserProvider {
    private final DataSource dataSource;

    public UserProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveUser(Users user){
        Sql2o sql2o = new Sql2o(dataSource);
        final String insertUserQuery = ("INSERT INTO users " +
                "(id,name,username,email,address_street,address_suite,address_city,address_zip_code,address_geo_lat,address_geo_lng,phone,website,company_name,company_catchphrase,company_bs)" +
                " VALUES(:id,:name,:username,:email,:address_street,:address_suite,:address_city,:address_zip_code,:address_geo_lat,:address_geo_lng,:phone,:website,:company_name,:company_catchphrase,:company_bs) ").formatted();
        try (Connection connection = sql2o.open(); Query query = connection.createQuery(insertUserQuery)) {
            query.addParameter("id",user.getId());
            query.addParameter("name",user.getName());
            query.addParameter("username",user.getUsername());
            query.addParameter("email",user.getEmail());
            query.addParameter("address_street",user.getAddress().getStreet());
            query.addParameter("address_suite",user.getAddress().getSuite());
            query.addParameter("address_city",user.getAddress().getCity());
            query.addParameter("address_zip_code",user.getAddress().getZipcode());
            query.addParameter("address_geo_lat",user.getAddress().getGeo().getLat());
            query.addParameter("address_geo_lng",user.getAddress().getGeo().getLng());
            query.addParameter("phone",user.getPhone());
            query.addParameter("website",user.getWebsite());
            query.addParameter("company_name",user.getCompany().getName());
            query.addParameter("company_catchphrase",user.getCompany().getCatchPhrase());
            query.addParameter("company_bs",user.getCompany().getBs());
            query.executeUpdate();
        }
    }

    public Users getUserById(int id){
        Sql2o sql2o = new Sql2o(dataSource);
        try (Connection connection = sql2o.open(); Query query = connection.createQuery("SELECT * FROM users where id = :id")) {
            query.addParameter("id", 1);
            query.addColumnMapping("address_city", "");
            query.setAutoDeriveColumnNames(true);
            var user = query.executeAndFetchTable();

            if (Objects.nonNull(user) && user.rows().size() > 0) {
                Users userRecord = new Users();
                Address address = new Address();
                Geo geo = new Geo();
                Company company = new Company();
                userRecord.setCompany(company);
                userRecord.setAddress(address);

                userRecord.setId(user.rows().get(0).getInteger("id"));
                userRecord.setName(user.rows().get(0).getString("name"));
                userRecord.setUsername(user.rows().get(0).getString("username"));
                userRecord.setEmail(user.rows().get(0).getString("email"));
                userRecord.getAddress().setStreet(user.rows().get(0).getString("address_street"));
                userRecord.getAddress().setCity(user.rows().get(0).getString("address_city"));
                userRecord.getAddress().setSuite(user.rows().get(0).getString("address_suite"));
                userRecord.getAddress().setZipcode(user.rows().get(0).getString("address_zip_code"));
                userRecord.getAddress().getGeo().setLat(user.rows().get(0).getBigDecimal("address_geo_lat"));
                userRecord.getAddress().getGeo().setLng(user.rows().get(0).getBigDecimal("address_geo_lng"));
                userRecord.setPhone(user.rows().get(0).getString("phone"));
                userRecord.setWebsite(user.rows().get(0).getString("website"));
                userRecord.getCompany().setName(user.rows().get(0).getString("company_name"));
                userRecord.getCompany().setCatchPhrase(user.rows().get(0).getString("company_catchphrase"));
                userRecord.getCompany().setBs(user.rows().get(0).getString("company_bs"));
                return userRecord;
            } else {
                throw new RuntimeException("User not found");
            }
        }
    }
}

