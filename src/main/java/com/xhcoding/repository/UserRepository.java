package com.xhcoding.repository;

import com.xhcoding.entity.TopicsUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by xin on 2017/11/28.
 */
@Repository
public interface UserRepository extends JpaRepository<TopicsUsers,Long>, JpaSpecificationExecutor<TopicsUsers> {
}
