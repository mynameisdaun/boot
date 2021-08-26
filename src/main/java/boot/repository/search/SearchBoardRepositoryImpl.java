package boot.repository.search;

import boot.entity.Board;
import boot.entity.QBoard;
import boot.entity.QMember;
import boot.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("serach1..");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;
        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member.email,reply.count());
        tuple.groupBy(board.bno,member.email,board.modDate,board.regDate,board.content,board.title,board.writer.email);
        log.info("-----------------------");
        log.info(tuple);
        log.info("-----------------------");
        List<Tuple> result = tuple.fetch();
        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage..");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member,reply.count());
        JPQLQuery<Tuple> tuple2 = jpqlQuery.select(board,member,reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);
        booleanBuilder.and(expression);


        if(type != null){
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for(String t : typeArr) {
                switch(t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach( order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String prop = order.getProperty();
                    PathBuilder orderByExpression = new PathBuilder(Board.class,"board");
                    tuple.orderBy(new OrderSpecifier(direction,orderByExpression.get(prop)));
                });
        tuple.groupBy(board.bno,member.email,board.modDate,board.regDate,board.content,board.title,board.writer.email,member.modDate,member.regDate,member.name,member.password);

        /* for total */
        List<Tuple> result = tuple.fetch();
        long count = result.size();
        log.info("COUNT: "+count);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        result = tuple.fetch();
        log.info(result);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),pageable,count);

    }
}
