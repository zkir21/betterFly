package com.unitedthinkers.fly.utils.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/19
 */
public class SheetsServiceUtil {

	private static final String APPLICATION_NAME = "KnowlegeBase";
	private static Sheets sheetsService;

	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		if (sheetsService == null) {
			Credential credential = GoogleAuthorizeUtil.getCredentials();
			sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
		}
		return sheetsService;
	}

}