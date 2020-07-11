package proPets.searching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class EmailMatchedPostsAuthorsDto {
	String postId;
	String flag;
	String [] emails;
}
