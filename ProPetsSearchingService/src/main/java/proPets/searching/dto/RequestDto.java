package proPets.searching.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class RequestDto implements Serializable{ /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//тот пост, что приходит из ЛФ для конвертации (пост из Монго)
	
	String id; //post id
	String email; //authorId
	String flag;
	String type;
	String address;
	String distinctiveFeatures;
	String[] picturesURLs;
	

}
