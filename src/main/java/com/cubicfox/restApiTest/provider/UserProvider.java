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
        final String insertUserQuery = "INSERT INTO users (id,name,username,email,street,suite,city,zip_code,geo_lat,geo_lng,phone,website,company_name,company_catchphrase,company_bs) VALUES(:id,:name,:username,:email,:street,:suite,:city,:zip_code,:geo_lat,:geo_lng,:phone,:website,:company_name,:company_catchphrase,:company_bs) ".formatted();
        try (Connection connection = sql2o.open(); Query query = connection.createQuery(insertUserQuery)) {
            query.addParameter("id",user.getId());
            query.addParameter("name",user.getName());
            query.addParameter("username",user.getUsername());
            query.addParameter("email",user.getEmail());
            query.addParameter("street",user.getAddress().getStreet());
            query.addParameter("suite",user.getAddress().getSuite());
            query.addParameter("city",user.getAddress().getCity());
            query.addParameter("zip_code",user.getAddress().getZipcode());
            query.addParameter("geo_lat",user.getAddress().getGeo().getLat());
            query.addParameter("geo_lng",user.getAddress().getGeo().getLng());
            query.addParameter("phone",user.getPhone());
            query.addParameter("website",user.getWebsite());
            query.addParameter("company_name",user.getCompany().getName());
            query.addParameter("company_catchphrase",user.getCompany().getCatchPhrase());
            query.addParameter("company_bs",user.getCompany().getBs());
            query.executeUpdate();
        }
    }
}
