package boot.repository.search;

import boot.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchRepositoryImpl extends QuerydslRepositorySupport implements SearchRepository {

    public SearchRepositoryImpl() {
        super(Movie.class);
    }

    @Override
    public Page<Object[]> getListPage(Pageable pageable) {
        log.info("search1..");

        QMovie movie = QMovie.movie;
        QReview review = QReview.review;
        QMovieImage movieImage = QMovieImage.movieImage;

        JPQLQuery<MovieImage> query = from(movieImage);
        query.leftJoin(movie).on(movie.eq(movieImage.movie));
        JPQLQuery<Tuple> tuple = query
                .select(movie, movieImage ,
                        JPAExpressions.select(review.countDistinct()).from(review).where(review.movie.eq(movieImage.movie)),
                        JPAExpressions.select(review.grade.avg()).from(review).where(review.movie.eq(movieImage.movie))
                )
                .where( movieImage.inum.in(JPAExpressions.select(movieImage.inum.min()).from(movieImage).where(movie.eq(movieImage.movie))
                        .groupBy(movie, movieImage.movie))
                ).orderBy(movie.mno.desc());

        List<Tuple> result = tuple.fetch();
        long count = result.size();
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        result = tuple.fetch();
        return new PageImpl<Object[]>(result.stream().map(t->t.toArray()).collect(Collectors.toList()));
    }


}
