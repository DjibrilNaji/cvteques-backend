package com.cvteques.repository;

import com.cvteques.dto.CvListRow;
import com.cvteques.entity.Cv;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepository extends JpaRepository<Cv, Long> {
  Optional<Cv> findByIntervenantId(Long intervenantId);

  @Query(
      """
        SELECT new com.cvteques.dto.CvListRow(
            c.id,
            u.firstname,
            u.lastname,
            c.title,
            c.updatedAt,
            c.url
        )
        FROM Cv c
        JOIN User u ON u.id = c.intervenantId
        """)
  List<CvListRow> findAllWithUser();
}
