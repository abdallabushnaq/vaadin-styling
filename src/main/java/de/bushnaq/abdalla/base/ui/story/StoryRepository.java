package de.bushnaq.abdalla.base.ui.story;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoryRepository extends JpaRepository<Story, Long>, JpaSpecificationExecutor<Story> {

    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<Story> findAllBy(Pageable pageable);
}

