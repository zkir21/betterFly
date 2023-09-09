package com.unitedthinkers.fly.utils.google;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.Strings;
import com.unitedthinkers.fly.data.KnowledgeBase;
import com.unitedthinkers.fly.exception.BetterFlyException;
import com.unitedthinkers.fly.settings.AppSettingsState;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/19
 */
public class GoogleSheetsLive {

	private static Optional<Map<String, String>> tags = Optional.empty();


	public static void add(KnowledgeBase data) {
		try {
			AppSettingsState settings = AppSettingsState.getInstance();
			if (Strings.isNullOrEmpty(settings.googleSheetsDataFile)) {
				throw BetterFlyException.b02("knowledge base data file");
			}
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");
//@comment: Task/Ticket	Tags	Comment	SourceFile	Author	LastUpdate	Screenshots
			ValueRange appendBody = new ValueRange()
					.setValues(Arrays.asList(Arrays.asList(data.getTask(), data.getTags(), data.getComment(), data.getSourceFile(), data.getUserName(), dateFormat.format(data.getCreateDate()), data.getSourceCode())));
			AppendValuesResponse appendResult = SheetsServiceUtil.getSheetsService().spreadsheets().values()
					.append(settings.googleSheetsDataFile, "A1", appendBody)
					.setValueInputOption("USER_ENTERED")
					.setInsertDataOption("INSERT_ROWS")
					.setIncludeValuesInResponse(true)
					.execute();
		} catch (IOException e) {
			throw BetterFlyException.b03(e);
		} catch (GeneralSecurityException e) {
			throw BetterFlyException.b04(e);
		}
	}

	public static Optional<Map<String, String>> getTags() {
		AppSettingsState settings = AppSettingsState.getInstance();
		if (tags.isEmpty() && !Strings.isNullOrEmpty(settings.googleSheetsTagFile)) {
			try {
				List<String> ranges = Arrays.asList("A2:B");
				BatchGetValuesResponse readResult = SheetsServiceUtil.getSheetsService()
						.spreadsheets()
						.values()
						.batchGet(settings.googleSheetsTagFile)
						.setRanges(ranges)
						.execute();
				ValueRange valueRange = readResult.getValueRanges().get(0);

				tags = Optional.of(new HashMap<>(valueRange.getValues().size()));

				for (List<Object> data : valueRange.getValues()) {
					tags.get().put(data.get(1).toString(), data.get(0).toString());
				}
			} catch (IOException e) {
				throw BetterFlyException.b03(e);
			} catch (GeneralSecurityException e) {
				throw BetterFlyException.b04(e);
			}
		}
		return tags;
	}

}
