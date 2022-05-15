package com.ttnm.chillchatting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private String id;

	private String userName;

	@JsonIgnore
	private String password;

	private String name;

	private Boolean enable = true;

	
}
