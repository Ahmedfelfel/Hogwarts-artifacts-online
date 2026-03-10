package com.felfel.hogwarts_artifacts_online.wizard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Wizard repository.
 */
@Repository
public interface WizardRepository extends JpaRepository<Wizard,Integer> {
}
