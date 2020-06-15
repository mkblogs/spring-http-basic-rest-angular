package com.tech.mkblogs.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech.mkblogs.model.Authorities;

@Repository
public interface UserAuthoritiesRepository extends JpaRepository<Authorities, Integer> {

}
