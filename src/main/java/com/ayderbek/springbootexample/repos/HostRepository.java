package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HostRepository extends JpaRepository<Host,Long> {
    @Query("SELECT h FROM Host h JOIN h.properties p JOIN p.reservations r WHERE r.status = 'COMPLETED' GROUP BY h ORDER BY COUNT(r) DESC")
    List<Host> findMostActiveHosts();
}
