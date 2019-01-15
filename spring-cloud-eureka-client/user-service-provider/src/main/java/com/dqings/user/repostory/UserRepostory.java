package com.dqings.user.repostory;

import com.dqings.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepostory {

    private Map<Long,User> users = new ConcurrentHashMap<>();

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public boolean save(User user) {
        long id = idGenerator.incrementAndGet();
        user.setId(id);
        return users.putIfAbsent(id,user) ==null;
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
