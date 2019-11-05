package dat.pham.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.pham.service.PoskMaskService;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@RestController
public class PoskMaskController {
	@Autowired
	private Environment env;

	@Autowired
	private PoskMaskService poskMaskService;

	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public String get(@RequestPart(name = "ffff", required = false) MultipartFile file) {
		poskMaskService.get(file);
		return "ok";
	}

	@RequestMapping(value = "/ducanh", method = RequestMethod.POST)
	public String toolDucAnh(@RequestPart(name = "ffff", required = false) MultipartFile file) {
		try {
			poskMaskService.toolDucAnh(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String toolExportXLSX() {
		try {
			poskMaskService.getXLSX();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public String OAuth() {
		System.out.println("1");
		return "ok 1";
	}

	@RequestMapping(value = "/oauth1", method = RequestMethod.GET)
	public String OAuth1() {
		System.out.println("2");
		return "ok 2";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		System.out.println("logout");
		SecurityContextHolder.getContext().setAuthentication(null);
		return "logout";
	}
}
