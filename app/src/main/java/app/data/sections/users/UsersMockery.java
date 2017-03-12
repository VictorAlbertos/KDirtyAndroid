/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.data.sections.users;

import io.victoralbertos.mockery.api.built_in_mockery.DTOArgs;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

final class UsersMockery {
  static class UserDTO implements DTOArgs.Behaviour<User> {
    @Override public User legal(Object[] args) {
      String userName = (String) args[0];
      User user = new User(1, userName,
          "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png");
      return user;
    }

    @Override public void validate(User candidate) throws AssertionError {
      MatcherAssert.assertThat(candidate, notNullValue());
      MatcherAssert.assertThat(candidate.getId(), is(not(0)));
      MatcherAssert.assertThat(candidate.getLogin().isEmpty(), is(not(true)));
    }
  }

  static class UsersDTO implements DTOArgs.Behaviour<List<User>> {
    @Override public List<User> legal(Object[] args) {
      List<User> users = new ArrayList<>();

      int lastIdQueried = (int) args[0] + 1;
      int perPage = (int) args[1];
      perPage = perPage == 0 ? 30 : perPage;

      for (int i = lastIdQueried; i <= lastIdQueried + perPage; i++) {
        users.add(new User(i, "User " + i,
            "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png"));
      }

      return users;
    }

    @Override public void validate(List<User> candidate) throws AssertionError {
      MatcherAssert.assertThat(candidate, notNullValue());
      MatcherAssert.assertThat(candidate.isEmpty(), is(not(true)));

      User user = candidate.get(0);
      MatcherAssert.assertThat(user.getId(), is(not(0)));
      MatcherAssert.assertThat(user.getLogin().isEmpty(), is(not(true)));
    }
  }
}
