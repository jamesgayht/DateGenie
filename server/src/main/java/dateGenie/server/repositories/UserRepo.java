package dateGenie.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import dateGenie.server.models.User;

import static dateGenie.server.repositories.Queries.*;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertNewUser(User user) {
        String hash = "";
        try {
            // Message digest = md5, sha1, sha512
            // Get an instance of MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // Calculate our hash
            // Update our message digest
            md5.update(user.getPassword().getBytes());
            // Get the MD5 digest
            byte[] h = md5.digest();
            // Stringify the MD5 digest
            hash = HexFormat.of().formatHex(h);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("password >>> " + user.getPassword());
        System.out.println("hash >>> " + hash);

        return jdbcTemplate.update(SQL_INSERT_INTO_USERS, user.getUsername(), user.getEmail(), hash) > 0;
    }

    public Optional<User> findUserByUsername(String username) {

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_USERNAME, username);

        User user = new User();

        if (!rs.next()) {
            return Optional.empty();
        } else {
            user = User.createUserFromSql(rs);
            return Optional.of(user);
        }
    }

    public Optional<User> findUserByEmail(String email) {

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_EMAIL, email);

        User user = new User();

        if (!rs.next()) {
            return Optional.empty();
        } else {
            user = User.createUserFromSql(rs);
        }
        return Optional.of(user);
    }

}
