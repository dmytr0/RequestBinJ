package test.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

public class LogMessage {
	private static final String DEFAULT_START_TAG = "|";
	private static final String DEFAULT_END_TAG = "|";
	private static final String DEFAULT_DELIMITER = "=";

	private StringBuilder fullMsg = new StringBuilder(128);

	LogMessage(@NotNull String moduleName) {
		fullMsg.append(String.format("|%26s|", moduleName));
	}

	@NotNull
	public LogMessage msg(@Nullable String message) {
		return add("msg", message);
	}

	@NotNull
	public LogMessage msg(@NotNull String pattern, @Nullable Object... params) {
		return msg(String.format(pattern, params));
	}

	@NotNull
	public LogMessage msgId(@Nullable Object messageId) {
		return add("msgId", messageId);
	}

	@NotNull
	public LogMessage clientId(@Nullable Object clientId) {
		return add("clientId", clientId);
	}

	@NotNull
	public LogMessage host(@Nullable String host) {
		return add("host", host);
	}

	public @NotNull LogMessage httpStatus(@Nullable Object httpStatus) {
		return add("httpStatus", httpStatus);
	}

	@NotNull
	public LogMessage phone(@Nullable Object phone) {
		return add("phone", phone);
	}

	@NotNull
	public LogMessage alphaName(@Nullable String alphaName) {
		return add("alphaName", alphaName);
	}

	@NotNull
	public LogMessage status(@Nullable Integer status) {
		return add("status", status);
	}

	@NotNull
	public LogMessage retry(int retry) {
		return add("retry", retry);
	}

	@NotNull
	public LogMessage fromQueue(@Nullable String queueName) {
		return add("fromQueue", queueName);
	}

	@NotNull
	public LogMessage toQueue(@Nullable String queueName) {
		return add("toQueue", queueName);
	}

	@NotNull
	public LogMessage route(@Nullable String routeName) {
		return add("route", routeName);
	}

	@NotNull
	public LogMessage route(@NotNull String pattern, @Nullable Object... params) {
		return route(String.format(pattern, params));
	}

	@NotNull
	public LogMessage url(@Nullable String url) {
		return add("url", url);
	}

	@NotNull
	public LogMessage url(@NotNull String url, @Nullable Object... params) {
		return url(String.format(url, params));
	}

	@NotNull
	public LogMessage hrCode(int hyberResponseCode) {
		return add("hrCode", hyberResponseCode);
	}

	@NotNull
	public LogMessage bmsId(@Nullable Object bmsId) {
		return add("bmsId", bmsId);
	}

	@NotNull
	public LogMessage viberServiceId(@Nullable Object viberServiceId) {
		return add("viberServiceId", viberServiceId);
	}

	@NotNull
	public LogMessage finalStatus(@Nullable Integer finalStatus) {
		return add("finalStatus", finalStatus);
	}

	@NotNull
	public LogMessage viberToken(@Nullable Object token) {
		return add("viberToken", token);
	}

	@NotNull
	public LogMessage hyberToken(@Nullable Object token) {
		return add("hyberToken", token);
	}

	@NotNull
	public LogMessage mobileToken(@Nullable Object token) {
		return add("mobileToken", token);
	}

	@NotNull
	public LogMessage mobileSettingsId(@Nullable Object id) {
		return add("mobileSettingsId", id);
	}

	@NotNull
	public LogMessage deviceSessionId(@Nullable Object deviceSessionId) {
		return add("deviceSessionId", deviceSessionId);
	}

	@NotNull
	public LogMessage fcmToken(@Nullable Object token) {
		return add("fcmToken", token);
	}

	@NotNull
	public LogMessage request(@Nullable Object request) {
		return add("request", request);
	}

	@NotNull
	public LogMessage response(@Nullable Object response) {
		return add("response", response);
	}

	@NotNull
	public LogMessage sessionId(@Nullable Object sessionId) {
		return add("sessionId", sessionId);
	}

	@NotNull
	public LogMessage jobId(@Nullable Object jobId) {
		return add("jobId", jobId);
	}

	@NotNull
	public LogMessage extraId(@Nullable Object extraId) {
		return add("extraId", extraId);
	}

	@NotNull
	public LogMessage criteria(@Nullable Object criteria) {
		return add("criteria", criteria);
	}

	@NotNull
	public LogMessage auth(@Nullable String authorization) {
		return add("auth", authorization);
	}

	@NotNull
	public LogMessage batchId(@Nullable Object batchId) {
		return add("batchId", batchId);
	}

	@NotNull
	public LogMessage contentType(@Nullable Object contentType) {
		return add("contentType", contentType);
	}

	@NotNull
	public LogMessage acceptedTime(@Nullable Object time, Integer code) {
		return code < HttpStatus.BAD_REQUEST.value() ? add("acceptedTime", time) : add("rejectedTime", time);
	}


	@NotNull
	public LogMessage headers(@Nullable String headers) {
		return add("headers", headers);
	}

	@NotNull
	public LogMessage exception(@Nullable String message) {
		return add("exception", message);
	}

	@NotNull
	public LogMessage exception(@NotNull String messagePattern, @Nullable Object... params) {
		return exception(String.format(messagePattern, params));
	}

	@NotNull
	public LogMessage deviceId(@Nullable Object deviceId) {
		return add("deviceId", deviceId);
	}

	@NotNull
	public LogMessage subscriberId(@Nullable Object subscriberId) {
		return add("subscriberId", subscriberId);
	}

	@NotNull
	public LogMessage smmpSessionId(Object sessionId) {
		return add("smppSessionId", sessionId);
	}

	@NotNull
	public LogMessage proxyId(@Nullable Object proxyId) {
		return add("proxyId", proxyId);
	}

	@NotNull
	public LogMessage delayedId(@Nullable Object delayedId) {
		return add("delayedId", delayedId);
	}

	@NotNull
	public LogMessage bidirectionalId(@Nullable Object bidirectionalId) {
		return add("bidirectionalId", bidirectionalId);
	}

	@NotNull
	public LogMessage reportId(@Nullable Object reportId) {
		return add("reportId", reportId);
	}

	@NotNull
	public LogMessage add(@NotNull String key, @Nullable Object value) {
		return add(DEFAULT_START_TAG, key, DEFAULT_DELIMITER, value, DEFAULT_END_TAG);
	}

	@NotNull
	public LogMessage add(@Nullable String startTag, @NotNull String key, @Nullable String delimiter, @Nullable Object value, @Nullable String endTag) {
		if (value != null) {
			fullMsg.append(startTag).append(key).append(delimiter).append(value).append(endTag);
		}
		return this;
	}

	@Override
	public String toString() {
		return fullMsg.toString();
	}
}
