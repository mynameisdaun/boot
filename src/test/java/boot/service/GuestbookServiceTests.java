package boot.service;

import boot.dto.GuestbookDTO;
import boot.dto.PageRequestDTO;
import boot.dto.PageResultDTO;
import boot.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister() {
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample test..")
                .content("Sample Content..")
                .writer("user0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("prev"+resultDTO.isPrev());
        System.out.println("prev"+resultDTO.isNext());
        System.out.println("TOTAL"+resultDTO.getTotalPage());
        System.out.println("---------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
        System.out.println("=====");
        resultDTO.getPageList().forEach(i->System.out.println(i));
    }

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("한글")
                .build();
        PageResultDTO<GuestbookDTO,Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("prev"+resultDTO.isPrev());
        System.out.println("next"+resultDTO.isNext());
        System.out.println("TOTAL"+resultDTO.getTotalPage());
        System.out.println("---------------");

        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
        System.out.println("=========================");
        resultDTO.getPageList().forEach(i->System.out.println(i));

    }
}
