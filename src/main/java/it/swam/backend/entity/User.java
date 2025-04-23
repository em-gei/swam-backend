package it.swam.backend.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "user")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String firstname;

    private String lastname;

    @Indexed(unique = true)
    private String email;

    @Field(name = "post_count")
    @Builder.Default
    private Integer postCount = 0;

    private Boolean active;

    @CreatedDate
    private LocalDateTime rowDate;

}
