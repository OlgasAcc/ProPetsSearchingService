package proPets.searching.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "dto is empty")
public class EmptyDataDtoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
