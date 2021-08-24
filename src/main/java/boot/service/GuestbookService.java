package boot.service;

import boot.dto.GuestbookDTO;
import boot.dto.PageRequestDTO;
import boot.dto.PageResultDTO;
import boot.entity.Guestbook;

public interface GuestbookService {
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO,Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO dto);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
       Guestbook entity = Guestbook.builder()
       .gno(dto.getGno())
       .title(dto.getTitle())
       .content(dto.getContent())
       .writer(dto.getWriter())
       .build();
       return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
