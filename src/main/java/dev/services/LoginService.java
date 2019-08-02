package dev.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.domains.User;
import dev.exceptions.AppException;
import dev.utils.BcryptUtils;
import dev.utils.DbUtils;

public class LoginService {

	public Optional<User> connect(String login, String password) {

		List<User> results2 = new ArrayList<>();

		List<User> results = new DbUtils().executeSelect(String.format("select * from user where login='%s'", login),
				resultSet -> new DbUtils().resultSetToUser(resultSet));

		if (results.size() > 1) {
			throw new AppException("at least 2 users with same login");
		}

		if (BcryptUtils.checkPw(password, results.get(0).getPassword())) {
			results2 = new DbUtils().executeSelect(String.format("select * from user where login='%s'", login),
					resultSet -> new DbUtils().resultSetToUser(resultSet));
		}

		return results2.stream().findAny();

	}

}
