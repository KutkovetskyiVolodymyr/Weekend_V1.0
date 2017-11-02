package weekend.service;

import weekend.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
