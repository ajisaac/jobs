package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
@Setter
@Slf4j
class JobUrl {
	public String value = "";
	private String err = null;

	public boolean isValid() {

		try {
			new URL(value);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			err = e.getMessage();
			return false;
		}

		return true;
	}

	public String errorMsg() {
		return err;
	}
}
