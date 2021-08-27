package boot.repository.search;

import boot.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchRepository {

    Page<Object[]> getListPage(Pageable pageable);

}
