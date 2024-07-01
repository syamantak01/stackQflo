package com.dexcode.stackQflo.repositories;

import com.dexcode.stackQflo.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
