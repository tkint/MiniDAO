package com.thomaskint.minidao.connection;

import java.util.Arrays;

/**
 * Created by tkint on 13/02/2018.
 */
public enum MDDriver {
	MYSQL("com.mysql.jdbc.Driver", "mysql://", "/", "?zeroDateTimeBehavior=convertToNull"),
	ORACLE("oracle.jdbc.driver.OracleDriver", "oracle:thin:@", ":");

	private String value;

	private String urlPart;

	private String databaseSeparator;

	private String complements;

	MDDriver(String value, String urlPart, String databaseSeparator) {
		this(value, urlPart, databaseSeparator, null);
	}

	MDDriver(String value, String urlPart, String databaseSeparator, String complements) {
		this.value = value;
		this.urlPart = urlPart;
		this.databaseSeparator = databaseSeparator;
		this.complements = complements;
	}

	public String getValue() {
		return value;
	}

	public String getUrlPart() {
		return urlPart;
	}

	public String getCompleteUrl(String url, String port, String database) {
		StringBuilder completeUrlBuilder = new StringBuilder("jdbc:");
		completeUrlBuilder.append(urlPart);
		completeUrlBuilder.append(url);
		completeUrlBuilder.append(":");
		completeUrlBuilder.append(port);
		completeUrlBuilder.append(databaseSeparator);
		completeUrlBuilder.append(database);
		if (complements != null) {
			completeUrlBuilder.append(complements);
		}
		return completeUrlBuilder.toString();
	}
}