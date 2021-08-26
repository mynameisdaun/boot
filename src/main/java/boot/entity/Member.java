package boot.entity;

import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "m_member")
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String mid;

    private String email;

    private String password;

    private String nickname;
}
