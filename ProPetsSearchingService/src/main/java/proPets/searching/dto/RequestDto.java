package proPets.searching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class RequestDto { //тот пост, что приходит из ЛФ для конвертации (пост из Монго)
	
	String id; //post id
	String flag;
	String type;
	String address;
	String distinctiveFeatures;
	String[] picturesURLs;
	

}
