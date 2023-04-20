package com.ayderbek.springbootexample.token;

import com.ayderbek.springbootexample.token.Token;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
    @Query(value = """
      SELECT t from Token t INNER JOIN User u\s
      ON t.user.id = u.id\s
      WHERE u.id = :id AND (t.expired = false OR t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Integer id);

//    @Transactional
    Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndTokenType(String token, TokenType tokenType);


}
