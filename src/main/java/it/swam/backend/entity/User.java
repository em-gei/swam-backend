package it.swam.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "user")
@Getter
@Setter
@Builder
public class User {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private ObjectId id;

    private String firstname;

    private String lastname;

    @Indexed(unique = true)
    private String email;

    @Field(name = "post_count")
    @Builder.Default
    private Integer postCount = 0;

    private Boolean active;

}
