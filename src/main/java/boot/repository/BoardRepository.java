package boot.repository;

import boot.entity.Board;
import boot.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    //for deprecated getOne!
    @Query("select b from Board b where b.bno =:bno")
    Object getBoardByBno2(@Param("bno") Long bno);

    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value=" select b, w, count(r) " +
                 " FROM Board b " +
                 " left join b.writer w " +
                 " left join Reply r on r.board = b " +
                 " group by b.bno, w.email, b.modDate,b.regDate,b.content,b.title,b.writer.email, w.modDate,w.regDate,w.name,w.password ",
                 countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("select b, w, count(r.rno) "+
            "from Board b left join b.writer w "+
            "LEFT OUTER join Reply r on r.board = b "+
            "group by b.bno, w.email, b.modDate,b.regDate,b.content,b.title,b.writer.email, w.modDate,w.regDate,w.name,w.password " +
            "having b.bno =:bno")
    Object getBoardByBno(@Param("bno") Long bno);



}
