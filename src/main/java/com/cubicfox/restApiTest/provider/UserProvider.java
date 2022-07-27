package com.cubicfox.restApiTest.provider;

import com.cubicfox.restApiTest.model.Users;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Repository
public class UserProvider {
    private final DataSource dataSource;

    public UserProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveUser(Users user){
        Sql2o sql2o = new Sql2o(dataSource);
        final String insertUserQuery = "INSERT INTO users (" +
                "id," +
                "name," +
                "username," +
                "email," +
                "street," +
                "suite" +
                "city," +
                "zip_code," +
                "geo_lat," +
                "geo_lng," +
                "phone," +
                "website," +
                "company_name," +
                "company_catchphrase" +
                "company_bs" +
                ") VALUES(" +
                ":id," +
                ":name," +
                ":username," +
                ":email," +
                ":street," +
                ":suite" +
                ":city," +
                ":zip_code," +
                ":geo_lat," +
                ":geo_lng," +
                ":phone," +
                ":website," +
                ":company_name," +
                ":company_catchphrase" +
                ":company_bs) ";
        try (Connection connection = sql2o.open(); Query query = connection.createQuery(insertUserQuery).bind(user)) {
            query.executeUpdate();
        }
    }
}
