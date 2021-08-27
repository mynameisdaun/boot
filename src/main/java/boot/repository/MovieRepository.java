package boot.repository;

import boot.entity.Movie;
import boot.repository.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long>, SearchRepository {

/*
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) from Movie m "+
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "group by m.mno, mi.inum, m.modDate, m.regDate, m.title, mi.imgName, mi.movie.mno, mi.path, mi.uuid")
    Page<Object[]> getListPage(Pageable pageable);
*/

    @Query(" select m, mi, avg(coalesce(r.grade,0)), count(r) " +
           " from Movie m left outer join MovieImage mi on mi.movie = m " +
           " left outer join Review r on r.movie = m " +
           " where m.mno = :mno " +
           " group by mi, m, m.modDate, m.regDate, m.title, mi.imgName, mi.movie.mno, mi.path, mi.uuid ")
    List<Object[]> getMovieWithAll(Long mno);

}
